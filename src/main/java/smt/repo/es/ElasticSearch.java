package smt.repo.es;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeValidationException;
import org.elasticsearch.node.internal.InternalSettingsPreparer;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.transport.Netty4Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jodd.petite.meta.PetiteBean;
import jodd.util.StringUtil;
import smt.app.SmartTrashException;
import smt.app.jodd.JoddPowered;
import smt.app.jodd.PropsElasticsearchProps;
import smt.model.Hit;

/**
 * Provides low-level elasticsearch functionality.
 * 
 * @author daniel
 *
 */
@PetiteBean
public class ElasticSearch
{
	private static final Logger LOG = LoggerFactory.getLogger(ElasticSearch.class);

	private Node node = null;

	/**
	 * Starts an elasticsearch node.
	 */
	public final void startElasticsearch()
	{
		// https://discuss.elastic.co/t/unsupported-http-type-netty3-when-trying-to-start-embedded-elasticsearch-node/69669/8
		final List<Class<? extends Plugin>> plugins = new ArrayList<>();
		plugins.add(Netty4Plugin.class);
		node = new PluginConfigurableNode(loadSettings(), plugins);
		try {
			node.start();
		} catch (NodeValidationException e) {
			throw new SmartTrashException("Node validation failed during startup", e);
		}

		prepareApplicationIndex();
	}

	private void prepareApplicationIndex()
	{
		// Prepare the index so we do not get an IndexNotFoundException if the index does not exist.
		final String idx = JoddPowered.settings.getValue(PropsElasticsearchProps.INDEX.getPropertyName());

		node.client().admin().indices().prepareExists(idx).execute(new ActionListener<IndicesExistsResponse>() {

			@Override
			public void onResponse(IndicesExistsResponse response)
			{
				if (response.isExists() == false) {
					node.client().admin().indices().prepareCreate(idx).get();
				}
			}

			@Override
			public void onFailure(Exception e)
			{

			}
		});
	}

	/**
	 * TODO check if it is useful to hold such a method inside this class instead of implementing it over and over again
	 * in services classes like FlowService etc.
	 * 
	 * @param jsonified
	 * @param indexName
	 * @param documentType
	 * @return
	 */
	public IndexResponse indexDocument(String jsonified, String indexName, String documentType,
			Consumer<IndexResponse> onSuccess, Consumer<Throwable> onFailure)
	{
		try {
			ListenableActionFuture<IndexResponse> execute = node.client().prepareIndex(indexName, documentType)
					.setSource(jsonified).setRefreshPolicy(RefreshPolicy.WAIT_UNTIL).execute();

			final Object lock = new Object();

			execute.addListener(new ActionListener<IndexResponse>() {

				@Override
				public void onResponse(IndexResponse response)
				{
					synchronized (lock) {
						try {
							onSuccess.accept(response);
						} finally {
							lock.notify();
						}
					}
				}

				@Override
				public void onFailure(Exception e)
				{
					synchronized (lock) {
						try {
							onFailure.accept(e);
						} finally {
							lock.notify();
						}
					}
				}
			});

			execute.get();
			synchronized (lock) {
				lock.wait();
			}

		} catch (Throwable t) { // providing an action listener does not prevent exceptions bubbling up in the call
								// hierachy.
			LOG.error("Exception occured while indexing document", t);
		}

		return null;
	}

	/**
	 * Do a search on a given type of es documents. The kind of search is defined by the given queryBuilder.
	 * 
	 * @param type type of the es document. Mandatory.
	 * @param queryBuilder the query to execute. Mandatory.
	 * @return List search results.
	 */
	public Collection<SearchHit> searchDocuments(String type, QueryBuilder queryBuilder)
	{
		return searchDocuments(type, queryBuilder, null);
	}

	public Collection<SearchHit> searchDocuments(String type, QueryBuilder queryBuilder, SortBuilder<?> sort)
	{
		SearchRequestBuilder searchQuery = node.client()
				.prepareSearch(JoddPowered.settings.getValue(PropsElasticsearchProps.INDEX.getPropertyName()))
				.setQuery(queryBuilder);
		
		if(sort != null) {
			searchQuery.addSort(sort);
		}
		
		SearchResponse searchResponse = searchQuery.get();
		SearchHits hits = searchResponse.getHits();

		return Arrays.stream(hits.getHits()).collect(Collectors.toList());
	}

	public <D> Collection<D> multiGet(String type, Function<String, D> fromJsonConverter, String... esDocumentIds)
	{
		final MultiGetResponse multiGetResponse = node.client().prepareMultiGet()
				.add(JoddPowered.settings.getValue(PropsElasticsearchProps.INDEX.getPropertyName()), type,
						esDocumentIds)
				.get();
		return Arrays.stream(multiGetResponse.getResponses()).map(mResp -> mResp.getResponse())
				.map(gResp -> gResp.getSourceAsString()).map(fromJsonConverter).collect(Collectors.toList());
	}

	/**
	 * Get a document by its ES Id.
	 * 
	 * @param esDocumentId the ES document ID.
	 * @param jsonConverter Takes care of converting the json to the correct Java-Type.
	 * @return Document with its source.
	 */
	public <D> Hit<D> documentById(final String esDocumentId, Function<String, D> jsonConverter)
	{
		final GetResponse getResponse = node.client()
				.prepareGet(JoddPowered.settings.getValue(PropsElasticsearchProps.INDEX.getPropertyName()), null,
						esDocumentId)
				.get();

		final Hit<D> hitR = new Hit<>();
		hitR.setDocumentId(esDocumentId);
		hitR.setDocument(jsonConverter.apply(getResponse.getSourceAsString()));
		return hitR;
	}

	private Settings loadSettings()
	{
		Map<String, String> target = new HashMap<>();
		JoddPowered.settings.extractSubProps(target, "elasticsearch.node.*");
		final Map<String, String> res = new HashMap<>();

		target.entrySet().stream()
				.forEach(entry -> res.put(StringUtil.remove(entry.getKey(), "elasticsearch.node."), entry.getValue()));
		Settings build = Settings.builder().put(res).build();
		return build;
	}

	/**
	 * Grants access to a node client. The client provides an API accessing the elasticsearch service.
	 * 
	 * @return an es client. Null if there is no es node.
	 */
	public final Client esClient()
	{
		if (node == null) {
			LOG.error("Elasticsearch Node not available, unable to get Client");
			return null;
		}

		return node.client();
	}

	public File pathHome()
	{
		String pathHome;
		if (node == null) {
			pathHome = loadSettings().get("path.home");
		} else {
			pathHome = node.client().settings().get("path.home");
		}
		return new File(pathHome);
	}

	/**
	 * Used to start elasticsearch by the application.
	 * 
	 * https://discuss.elastic.co/t/unsupported-http-type-netty3-when-trying-to-start-embedded-elasticsearch-node/69669/8
	 *
	 */
	private static class PluginConfigurableNode extends Node
	{
		public PluginConfigurableNode(Settings settings, Collection<Class<? extends Plugin>> classpathPlugins) {
			super(InternalSettingsPreparer.prepareEnvironment(settings, null), classpathPlugins);
		}
	}
}
