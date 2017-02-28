package de.ludwig.smt.app;

import static de.ludwig.jodd.JoddPowered.petite;
import static de.ludwig.jodd.JoddPowered.settings;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import de.ludwig.jodd.JoddPoweredTest;
import de.ludwig.jodd.PropsElasticsearchProps;
import de.ludwig.smt.tec.ElasticSearch;

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
			File fDataDir = new File(es.pathHome(), dataDir);
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
