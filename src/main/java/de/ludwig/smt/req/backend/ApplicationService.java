package de.ludwig.smt.req.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.ludwig.jodd.proxetta.CallStackContext;
import de.ludwig.rdd.Requirement;
import de.ludwig.smt.req.frontend.EditCreateFlowViewService;
import de.ludwig.smt.req.frontend.EditCreateNoteViewService;
import de.ludwig.smt.req.frontend.LatestNoteViewService;
import de.ludwig.smt.req.frontend.OverviewService;
import de.ludwig.smt.req.frontend.tec.ModalService;
import de.ludwig.smt.tec.ElasticSearch;
import de.ludwig.smt.tec.frontend.AjaxTriggeredResponseTransformer;
import jodd.petite.meta.PetiteBean;
import jodd.petite.meta.PetiteInject;
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
