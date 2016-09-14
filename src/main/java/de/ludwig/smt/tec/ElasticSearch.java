package de.ludwig.smt.tec;

import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jodd.petite.meta.PetiteBean;

/**
 * 
 * @author daniel
 *
 */
@PetiteBean
public class ElasticSearch {
	private static final Logger LOG = LoggerFactory.getLogger(ElasticSearch.class);

	private Node node;

	public final void startElasticsearch() {
		node = NodeBuilder
				.nodeBuilder().settings(NodeBuilder.nodeBuilder().getSettings()
						.loadFromStream("ONLY-THE-EXT-MATTERS.json", getClass().getResourceAsStream("/es-config.json")))
				.build();
		node.start();
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
	
	public final void saveDocument(){
//		IndexRequest request = new IndexRequest();
//		esClient().index(request);
//		esClient().
	}
}
