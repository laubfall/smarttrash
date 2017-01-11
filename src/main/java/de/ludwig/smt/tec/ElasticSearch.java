package de.ludwig.smt.tec;

import java.io.File;
import java.util.function.Function;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.get.GetResponse;
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
				// TODO Logging
			}
		});

	}

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
