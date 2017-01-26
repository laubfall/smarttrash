package de.ludwig.smt.tec;

import java.io.File;
import java.util.function.Consumer;
import java.util.function.Function;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.ludwig.jodd.JoddPowered;
import de.ludwig.jodd.PropsElasticsearchProps;
import de.ludwig.smt.app.data.Hit;
import jodd.petite.meta.PetiteBean;

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
		node = nodeBuilder().build();
		node.start();

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
			public void onFailure(Throwable e)
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
	 */
	public void indexDocument(String jsonified, String indexName, String documentType,
			Consumer<IndexResponse> onSuccess, Consumer<Throwable> onFailure)
	{
		try {
			ListenableActionFuture<IndexResponse> execute = node.client().prepareIndex(indexName, documentType)
					.setSource(jsonified).setRefresh(true).execute();

			execute.addListener(new ActionListener<IndexResponse>() {

				@Override
				public void onResponse(IndexResponse response)
				{
					onSuccess.accept(response);
				}

				@Override
				public void onFailure(Throwable e)
				{
					onFailure.accept(e);
				}
			});

			execute.actionGet();
		} catch (Throwable t) { // providing an action listener does not prevent exceptions bubbling up in the call
								// hierachy.
			LOG.error("Exception occured while indexing document", t);
		}
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

	private NodeBuilder nodeBuilder()
	{
		return NodeBuilder.nodeBuilder()
				.settings(NodeBuilder.nodeBuilder().getSettings().loadFromStream("ONLY-THE-EXT-MATTERS.json",
						getClass().getResourceAsStream(
								JoddPowered.settings.getValue(PropsElasticsearchProps.CONFIG.getPropertyName()))));
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
			pathHome = nodeBuilder().getSettings().get("path.home");
		} else {
			pathHome = node.client().settings().get("path.home");
		}
		return new File(pathHome);
	}
}
