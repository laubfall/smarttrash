package smt.app;

import static smt.app.jodd.JoddPowered.petite;
import static smt.app.jodd.JoddPowered.settings;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import smt.app.jodd.JoddPoweredTest;
import smt.app.jodd.PropsElasticsearchProps;
import smt.persistence.es.ElasticSearch;

/**
 * Base test functionality for tests that requires Elasticsearch Support. This class starts an ES node once if the class
 * is loaded.
 * 
 * @author daniel
 *
 */
public abstract class AbstractElasticSearchTest extends JoddPoweredTest
{

	// only start the ES once.
	static {
		ElasticSearch es = petite.getBean(ElasticSearch.class);
		try {
			String dataDir = settings.getValue(PropsElasticsearchProps.NODE_DATA.getPropertyName());
			File fDataDir = new File(dataDir);
			FileUtils.deleteDirectory(fDataDir);
		} catch (IOException e) {
			throw new RuntimeException("Exception occured while deleting old ES Data Directory", e);
		}
		es.startElasticsearch();
	}

	@Override
	public void setup() throws Exception
	{
		// NOOP
	}

}
