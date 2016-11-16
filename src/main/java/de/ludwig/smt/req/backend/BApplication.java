package de.ludwig.smt.req.backend;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.ludwig.rdd.Requirement;
import de.ludwig.smt.req.frontend.FOverview;
import de.ludwig.smt.tec.ElasticSearch;
import jodd.json.JsonSerializer;
import jodd.petite.meta.PetiteBean;
import jodd.petite.meta.PetiteInject;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;

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

	private static Logger LOG = LoggerFactory.getLogger(BApplication.class); 
	
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
		Spark.get("/", (req, res) -> fOverview.showWelcomePage().apply(req, res), new HandlebarsTemplateEngine());
		
		Spark.exception(Exception.class, (exception, req, resp) -> this.handleSparkRoutingException(exception));
	}

	public final void startElasticsearch()
	{
		es.startElasticsearch();
	}
	
	public void handleSparkRoutingException(Exception e) {

	}
}
