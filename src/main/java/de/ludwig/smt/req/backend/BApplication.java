package de.ludwig.smt.req.backend;

import de.ludwig.jodd.CallStackContext;
import de.ludwig.rdd.Requirement;
import de.ludwig.smt.req.frontend.EditCreateFlow;
import de.ludwig.smt.req.frontend.FOverview;
import de.ludwig.smt.req.frontend.tec.ModalService;
import de.ludwig.smt.tec.ElasticSearch;
import de.ludwig.smt.tec.frontend.JsonResponseTransformer;
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
public class BApplication
{

	@PetiteInject
	protected ElasticSearch es;

	@PetiteInject
	protected FOverview fOverview; 
	
	@PetiteInject
	protected ModalService modalService;
	
	@PetiteInject
	protected EditCreateFlow editCreateFlow;
	
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

		// Form Handlers
		Spark.post("/editCreateFlow", "application/json", (req, res) -> editCreateFlow.saveFlow(), new JsonResponseTransformer());
		
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
	 * Does nothing, but the call is logged by logging framework.
	 * @param e exception to handle.
	 */
	public void handleSparkRoutingException(Exception e) {
		// NOOP
	}
}
