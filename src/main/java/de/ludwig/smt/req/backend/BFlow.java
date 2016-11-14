package de.ludwig.smt.req.backend;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.xcontent.XContentBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.ludwig.jodd.JoddPowered;
import de.ludwig.jodd.PropsElasticsearchProps;
import de.ludwig.rdd.Requirement;
import de.ludwig.rdd.RequirementExecutionException;
import de.ludwig.smt.SmartTrashException;
import de.ludwig.smt.app.data.Flow;
import de.ludwig.smt.app.data.Hit;
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

	public List<Hit<Flow>> loadFlows() {
		final SearchResponse searchResponse = es.esClient().prepareSearch(indexName).setTypes("flow").get();
		final List<Hit<Flow>> result = new ArrayList<>();

		searchResponse.getHits().forEach(hit -> {
			try {
				Hit<Flow> hitR = new Hit<>();
				hitR.setDocumentId(hit.getId());
				hitR.setDocument(fromElasticSearch(hit.getSourceRef()));
				result.add(hitR);
			} catch (IOException e) {
				throw new RequirementExecutionException("Unable to parse Json", e);
			}
		});
		;

		return result;
	}

	public String createFlow(final Flow flow) {

		final IndexRequest request = es.esClient().prepareIndex(indexName, "flow").setSource(toElasticSearch(flow))
				.request() //
				.refresh(true); // in case of an immediately call to loadFlows,
								// otherwise we will not find any result.
		final ActionFuture<IndexResponse> indexFuture = es.esClient().index(request);
		try {
			final IndexResponse response = indexFuture.get();
			return response.getId();
		} catch (InterruptedException | ExecutionException e) {
			throw new RequirementExecutionException(e);
		}
	}
	
	public void updateFlow(final Flow flow){
		
	}

	private Flow fromElasticSearch(BytesReference ref) throws JsonParseException, JsonMappingException, IOException {
		// ObjectMapper om = new ObjectMapper();
		ObjectMapper om = new ObjectMapper();

		Flow readValue = om.readValue(ref.toBytes(), Flow.class);
		return readValue;

	}
	
	private BytesReference toElasticSearch(final Flow flow){

		try {
			XContentBuilder builder = jsonBuilder()
			    .startObject()
			        .field("name", flow.getName())
			    .endObject();
			return builder.bytes();
		} catch (IOException e) {
			throw new SmartTrashException(e);
		}
	}
}
