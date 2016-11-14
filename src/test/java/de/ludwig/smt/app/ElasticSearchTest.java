package de.ludwig.smt.app;

import static de.ludwig.jodd.JoddPowered.petite;

import java.io.IOException;

import org.apache.commons.io.FileUtils;

import de.ludwig.jodd.JoddPoweredTest;
import de.ludwig.smt.tec.ElasticSearch;

/**
 * Base test functionality for tests that requires Elasticsearch Support.
 * 
 * @author daniel
 *
 */
public abstract class ElasticSearchTest extends JoddPoweredTest {

	@Override
	public void setup() throws IOException {
		ElasticSearch es = petite.getBean(ElasticSearch.class);
		FileUtils.deleteDirectory(es.pathHome());
		es.startElasticsearch();
	}
	
}
