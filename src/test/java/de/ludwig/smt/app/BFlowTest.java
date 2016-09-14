package de.ludwig.smt.app;

import org.junit.Before;
import org.junit.Test;

import de.ludwig.smt.app.data.Flow;
import de.ludwig.smt.jodd.JoddPowered;
import de.ludwig.smt.jodd.PropsProfiles;
import de.ludwig.smt.tec.ElasticSearch;

/**
 * BFlow Test.
 * 
 * @author daniel
 *
 */
public class BFlowTest extends JoddPowered {
	private BFlow bFlow;

	@Before
	public void setup() {
		initPetite();
		initProps();
		settings.setActiveProfiles(PropsProfiles.JUNIT.getProfileName());
		
		bFlow = petite.getBean(BFlow.class);
		
		petite.getBean(ElasticSearch.class).startElasticsearch();
		
	}

	@Test
	public void createAndLoadFlow() {
		final Flow flow = new Flow("craeteAndLoadFlow");
		
		bFlow.createFlow(flow);
		bFlow.loadFlows();
	}
}
