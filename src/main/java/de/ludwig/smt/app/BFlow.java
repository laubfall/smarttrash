package de.ludwig.smt.app;

import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.collect.ImmutableOpenMap;

import de.ludwig.rdd.Requirement;
import de.ludwig.rdd.RequirementExecutionException;
import de.ludwig.smt.app.data.Flow;
import de.ludwig.smt.jodd.JoddPowered;
import de.ludwig.smt.jodd.PropsElasticsearchProps;
import de.ludwig.smt.tec.ElasticSearch;
import jodd.petite.meta.PetiteBean;
import jodd.petite.meta.PetiteInject;

/**
 * Business requirements flows placed in the backend.
 * 
 * @author daniel
 *
 */
@Requirement
@PetiteBean
public class BFlow {

	private String indexName = JoddPowered.settings.getValue(PropsElasticsearchProps.INDEX.getPropertyName());
	
	@PetiteInject
	protected ElasticSearch es;

	public void loadFlows() {
		SearchResponse searchResponse = es.esClient().prepareSearch(indexName).setTypes("flow").get();
		searchResponse.getContext();
	}

	public void createFlow(final Flow flow) {
		IndexRequest request = es.esClient()
				.prepareIndex(indexName, "flow")
				.setSource(flow.toElasticSearch())
				.request();
		final ActionFuture<IndexResponse> indexFuture = es.esClient().index(request);
		try {
			final IndexResponse response = indexFuture.get();
			ImmutableOpenMap<Object, Object> context = response.getContext();
		} catch (InterruptedException | ExecutionException e) {
			throw new RequirementExecutionException(e);
		}
	}
}
