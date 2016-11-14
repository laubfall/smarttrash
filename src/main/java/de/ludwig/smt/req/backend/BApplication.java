package de.ludwig.smt.req.backend;

import de.ludwig.rdd.Requirement;
import de.ludwig.smt.req.frontend.FOverview;
import de.ludwig.smt.tec.ElasticSearch;
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
public class BApplication {
	
	@PetiteInject
	protected ElasticSearch es;
	
	@PetiteInject
	protected FOverview fOverview;
	
	public final void startApplication() {
		startSpark();
		startElasticsearch();
	}

	/**
	 * Register all Spark Request Handler.
	 */
	public final void startSpark() {
		Spark.get("/", (req, res) -> fOverview.showWelcomePage().apply(req, res), new ThymeleafTemplateEngine());
	}

	public final void startElasticsearch() {
		es.startElasticsearch();
	}
}
