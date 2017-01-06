package de.ludwig.smt.req.backend;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.IndexNotFoundException;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TypeQueryBuilder;

import de.ludwig.jodd.JoddPowered;
import de.ludwig.jodd.PropsElasticsearchProps;
import de.ludwig.rdd.Requirement;
import de.ludwig.smt.app.config.FlowBase;
import de.ludwig.smt.app.config.FlowId;
import de.ludwig.smt.app.data.Flow;
import de.ludwig.smt.app.data.Hit;
import de.ludwig.smt.req.backend.tec.FlowConfigService;
import de.ludwig.smt.tec.ElasticSearch;
import de.ludwig.smt.tec.validation.ValidationContext;
import de.ludwig.smt.tec.validation.ValidationMessage;
import jodd.petite.meta.PetiteBean;
import jodd.petite.meta.PetiteInject;
import spark.utils.StringUtils;

/**
 * Business requirements flows placed in the backend.
 * 
 * @author daniel
 *
 */
@Requirement
@PetiteBean
public class FlowService
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
	protected ElasticSearch es;

	@PetiteInject
	protected FlowConfigService flowConfig;

	/**
	 * Creates a new flow.
	 * 
	 * @param required. flow the flow to create. TODO check if the parameter is set.
	 * @param parents optional. List of IDs that represents the chain of parent flows.
	 * @return TODO only the id as return value?
	 */
	public String saveFlow(final Flow flow, final List<FlowId> parents)
	{
		if (isNewFlow(flow)) {
			createFlow(flow, parents);
		} else {
			updateFlow(flow, parents);
		}

		return null;
	}

	public void createFlow(final Flow flow, final List<FlowId> parents)
	{
		final FlowBase flowBase = flowConfig.createFlow(parents);

		flow.setId(flowBase.getId());

		IndexResponse indexResponse = es.esClient().prepareIndex(indexName, esFlowType).setSource(Flow.toJson(flow)).setRefresh(true).get();

	}

	public void updateFlow(final Flow flow, final List<FlowId> parents)
	{

	}

	/**
	 * Tries to find a flow with its id inside the elastichsearch index.
	 * 
	 * @return true if there is already a flow for the given id.
	 */
	public boolean isNewFlow(final Flow flow)
	{
		BoolQueryBuilder bqb = new BoolQueryBuilder();
		bqb.must(new TypeQueryBuilder(esFlowType)).must(new TermQueryBuilder("id", flow.getId()));
		try {
			SearchResponse actionGet = es.esClient().prepareSearch(indexName).setQuery(bqb).execute().actionGet();
			long totalHits = actionGet.getHits().totalHits();
			return totalHits == 0;
		} catch (IndexNotFoundException e) {
			return true;
		}
	}

	public ValidationContext<Flow> validateFlow(final Flow flow)
	{
		ValidationContext<Flow> ctx = new ValidationContext<Flow>(flow);
		if (StringUtils.isBlank(flow.getName())) {
			// TODO correct key
			ctx.addValidationMessage("name", new ValidationMessage("empty.name"));
		}
		return ctx;
	}

	/**
	 * Loads all stored flows.
	 * 
	 * TODO actually this method is not that usefull because it loads all flows.
	 * 
	 * @return loaded flows.
	 */
	public List<Hit<Flow>> loadFlows()
	{
		final SearchResponse searchResponse = es.esClient().prepareSearch(indexName).setQuery(new TypeQueryBuilder(esFlowType))
				.setTypes(esFlowType).get();
		final List<Hit<Flow>> result = new ArrayList<>();

		searchResponse.getHits().forEach(hit -> {
			Hit<Flow> hitR = new Hit<>();
			hitR.setDocumentId(hit.getId());
			hitR.setDocument(Flow.fromJson(hit.getSourceRef().toUtf8()));
			result.add(hitR);
		});
		;

		return result;
	}
}
