package smt.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jodd.petite.meta.PetiteBean;
import jodd.petite.meta.PetiteInject;
import smt.app.frontend.AjaxTriggeredResponseTransformer;
import smt.app.frontend.JsonResponseTransformer;
import smt.app.jodd.proxetta.CallStackContext;
import smt.app.rdd.Requirement;
import smt.repo.es.ElasticSearch;
import smt.repo.es.FlowService;
import smt.service.EditCreateFlowViewService;
import smt.service.EditCreateNoteViewService;
import smt.service.FlowOverviewResponse;
import smt.service.LatestNoteViewService;
import smt.service.ModalService;
import smt.service.OverviewService;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

/**
 * Everything to start the whole application.
 * 
 * @author daniel
 *
 */
@Requirement
@PetiteBean
public class ApplicationService
{

	@PetiteInject
	protected ElasticSearch es;

	@PetiteInject
	protected OverviewService fOverview;

	@PetiteInject
	protected ModalService modalService;

	@PetiteInject
	protected EditCreateFlowViewService editCreateFlow;

	@PetiteInject
	protected EditCreateNoteViewService editCreateNote;

	@PetiteInject
	protected LatestNoteViewService latestNoteView;

	@PetiteInject
	protected FlowService flowService;
	
	private static final Logger LOG = LoggerFactory.getLogger(ApplicationService.class);

	public void startApplication()
	{
		startSpark();
		startElasticsearch();
	}

	/**
	 * Register all Spark Request Handler.
	 */
	public final void startSpark()
	{
		Spark.staticFileLocation("web");

		// New logging context with every request
		Spark.before((req, res) -> CallStackContext.callStackCtx.set(new CallStackContext()));

		final ThymeleafTemplateEngine thymeLeafEngine = new ThymeleafTemplateEngine();
		Spark.get("/", (req, res) -> fOverview.showWelcomePage().apply(req, res), thymeLeafEngine);
		Spark.get("/modal/:name", (req, res) -> modalService.openModal().apply(req, res), thymeLeafEngine);
		Spark.get("/createLatestNotesView", (req, res) -> latestNoteView.createLatestNotesView().apply(req, res),
				new AjaxTriggeredResponseTransformer());

		Spark.get("/createFlowOverview", (req, res) -> fOverview.createFlowOverview().apply(req, res),
				new AjaxTriggeredResponseTransformer());

		// Form Handlers
		Spark.post("/editCreateFlow", (req, res) -> editCreateFlow.saveDocument().apply(req, res),
				new AjaxTriggeredResponseTransformer());

		Spark.post("/editCreateNote", (req, res) -> editCreateNote.saveDocument().apply(req, res),
				new AjaxTriggeredResponseTransformer());
		// END Form Handlers.

		
		// KNOCKOUT Test
		JsonDataService.registerProvider("flowOverview", (param) -> new FlowOverviewResponse(flowService.loadFlows()));
		JsonDataService.registerProvider("editCreateFlow", param -> flowService.getFlow(param));
		Spark.get("/data/:viewModel/:param", "application/json", JsonDataService::provideData, new JsonResponseTransformer());
		
		// <-- KNOCKOUT Test
		
		
		// handle exceptions that were not caught.
		Spark.exception(Exception.class, (exception, req, resp) -> this.handleSparkRoutingException(exception));

		// Cleanup after a request-response-cycle
		Spark.after((req, res) -> CallStackContext.callStackCtx.set(new CallStackContext()));
	}

	public final void startElasticsearch()
	{
		es.startElasticsearch();
	}

	/**
	 * Logs unhandled exceptions.
	 *
	 * @param e exception to handle.
	 */
	public void handleSparkRoutingException(Exception e)
	{
		LOG.error("unhandled exception occured", e);
	}
}
