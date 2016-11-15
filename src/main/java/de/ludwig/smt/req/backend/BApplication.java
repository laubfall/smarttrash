package de.ludwig.smt.req.backend;

import de.ludwig.rdd.Requirement;
import de.ludwig.smt.req.frontend.FOverview;
import de.ludwig.smt.tec.ElasticSearch;
import jodd.json.JsonSerializer;
import jodd.petite.meta.PetiteBean;
import jodd.petite.meta.PetiteInject;
import spark.Spark;

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

	public final void startApplication()
	{
		startSpark();
		startElasticsearch();
	}

	/**
	 * Register all Spark Request Handler.
	 */
	public final void startSpark()
	{
		final JsonSerializer jsonSerializer = JsonSerializer.create();
		Spark.get("/", (req, res) -> fOverview.showWelcomePage().apply(req, res), jsonSerializer::serialize);
	}

	public final void startElasticsearch()
	{
		es.startElasticsearch();
	}
}
