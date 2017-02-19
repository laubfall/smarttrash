package de.ludwig.smt.req.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.TypeQueryBuilder;

import de.ludwig.jodd.JoddPowered;
import de.ludwig.jodd.PropsElasticsearchProps;
import de.ludwig.rdd.Requirement;
import de.ludwig.rdd.RequirementMapping;
import de.ludwig.smt.app.config.FlowBase;
import de.ludwig.smt.app.config.FlowId;
import de.ludwig.smt.app.data.Flow;
import de.ludwig.smt.app.data.Hit;
import de.ludwig.smt.req.backend.tec.ElasticSearchDocumentService;
import de.ludwig.smt.req.backend.tec.FlowConfigService;
import jodd.petite.meta.PetiteBean;
import jodd.petite.meta.PetiteInject;

/**
 * Business requirements flows placed in the backend.
 * 
 * @author daniel
 *
 */
@Requirement(mappings = { // 
		@RequirementMapping(name = "isNewFlow", target = "isNewDocument"),
		@RequirementMapping(name = "createFlow", target = "indexDocument"),
		@RequirementMapping(name = "updateFlow", target = "updateDocument"),
		@RequirementMapping(name = "validateFlow", target = "validateDocument")
})
@PetiteBean
public class FlowService extends ElasticSearchDocumentService<Flow>
{
	/**
	 * ES Index-Name for the Flow.
	 */
	private String indexName = JoddPowered.settings.getValue(PropsElasticsearchProps.INDEX.getPropertyName());

	/**
	 * ES Type description for the Flow.
	 */
	private static String esFlowType = "flow";

	@PetiteInject
	protected FlowConfigService flowConfig;

	/**
	 * Creates a new flow.
	 * 
	 * @param flow required. flow the flow to create. TODO check if the parameter is set.
	 * @param parents optional. List of IDs that represents the chain of parent flows.
	 * @param esDocumentId ID of the elasticsearch document that stores information for the given flow. This is
	 *            optional. If given, this methods tries to load a document with that id.
	 * 
	 * @return ES Document ID.
	 */
	public String saveFlow(final Flow flow, final List<FlowId> parents, String esDocumentId)
	{
		if (isNewDocument(esDocumentId)) {
			final FlowBase flowBase = flowConfig.createFlow(parents);
			flow.setId(flowBase.getId());
		}

		return saveDocument(flow, esDocumentId, null);
	}

	/**
	 * Loads all stored flows.
	 * 
	 * TODO actually this method is not that useful because it loads all flows.
	 * 
	 * @return loaded flows.
	 */
	public List<Hit<Flow>> loadFlows()
	{
		final SearchResponse searchResponse = es.esClient().prepareSearch(indexName)
				.setQuery(new TypeQueryBuilder(esFlowType)).setSize(100).setTypes(esFlowType).get();
		final List<Hit<Flow>> result = new ArrayList<>();

		searchResponse.getHits().forEach(hit -> {
			Hit<Flow> hitR = new Hit<>();
			hitR.setDocumentId(hit.getId());
			hitR.setDocument(Flow.fromJson(hit.getSourceRef().utf8ToString()));
			result.add(hitR);
		});
		;

		return result;
	}

	public Hit<Flow> getFlow(final String esDocumentId)
	{
		return es.documentById(esDocumentId, Flow::fromJson);
	}

	@Override
	public Hit<Flow> getDocument(String esDocumentId)
	{
		return es.documentById(esDocumentId, Flow::fromJson);
	}

	@Override
	public String documentType()
	{
		return esFlowType;
	}

	@Override
	public Function<Flow, String> jsonifier()
	{
		return Flow::toJson;
	}
}
