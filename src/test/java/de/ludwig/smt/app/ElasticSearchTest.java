package de.ludwig.smt.app;

import static de.ludwig.smt.jodd.JoddPowered.initPetite;
import static de.ludwig.smt.jodd.JoddPowered.initProps;
import static de.ludwig.smt.jodd.JoddPowered.petite;
import static de.ludwig.smt.jodd.JoddPowered.settings;

import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;

import de.ludwig.smt.jodd.PropsProfiles;
import de.ludwig.smt.tec.ElasticSearch;

/**
 * Base test functionality for tests that requires Elasticsearch Support.
 * 
 * @author daniel
 *
 */
public abstract class ElasticSearchTest {

	@Before
	public void _setup() throws IOException {
		initPetite();
		initProps();
		settings.setActiveProfiles(PropsProfiles.JUNIT.getProfileName());
		
		ElasticSearch es = petite.getBean(ElasticSearch.class);
		FileUtils.deleteDirectory(es.pathHome());
		es.startElasticsearch();
		
		
		setup();
	}
	
	abstract void setup();
}
