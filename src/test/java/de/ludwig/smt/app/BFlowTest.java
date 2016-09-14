package de.ludwig.smt.app;

import org.junit.Before;
import org.junit.Test;

import de.ludwig.smt.jodd.JoddPowered;
import de.ludwig.smt.jodd.PropsProfiles;

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
		
		bFlow = petite.getBean(BFlow.class);
		
		settings.setActiveProfiles(PropsProfiles.JUNIT.getProfileName());
	}

	@Test
	public void createAndLoadFlow() {
		bFlow.createFlow();
		bFlow.loadFlows();
	}
}
