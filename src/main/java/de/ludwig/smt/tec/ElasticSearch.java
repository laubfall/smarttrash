package de.ludwig.smt.tec;

import java.io.File;

import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.ludwig.smt.jodd.JoddPowered;
import de.ludwig.smt.jodd.PropsElasticsearchProps;
import jodd.petite.meta.PetiteBean;

/**
 * 
 * @author daniel
 *
 */
@PetiteBean
public class ElasticSearch {
	private static final Logger LOG = LoggerFactory.getLogger(ElasticSearch.class);

	private Node node = null;

	public final void startElasticsearch() {
		node = nodeBuilder().build();
		node.start();
	}

	private NodeBuilder nodeBuilder() {
		return NodeBuilder.nodeBuilder()
				.settings(NodeBuilder.nodeBuilder().getSettings().loadFromStream("ONLY-THE-EXT-MATTERS.json",
						getClass().getResourceAsStream(
								JoddPowered.settings.getValue(PropsElasticsearchProps.CONFIG.getPropertyName()))));
	}

	/**
	 * 
	 * @return an es client. Null if there is no es node.
	 */
	public final Client esClient() {
		if (node == null) {
			LOG.error("Elasticsearch Node not available, unable to get Client");
			return null;
		}

		return node.client();
	}

	public File pathHome() {
		String pathHome;
		if (node == null) {
			pathHome = nodeBuilder().getSettings().get("path.home");
		} else {
			pathHome = node.client().settings().get("path.home");
		}
		return new File(pathHome);
	}
}
