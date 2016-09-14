package de.ludwig.smt.app;

import static de.ludwig.smt.jodd.JoddPowered.petite;

import org.junit.Test;

import de.ludwig.smt.app.data.Flow;

/**
 * BFlow Test.
 * 
 * @author daniel
 *
 */
public class BFlowTest extends ElasticSearchTest {
	private BFlow bFlow;

	@Test
	public void createAndLoadFlow() {
		final Flow flow = new Flow("createAndLoadFlow");

		bFlow.createFlow(flow);
		bFlow.loadFlows();
	}

	@Override
	void setup() {
		bFlow = petite.getBean(BFlow.class);
	}
}
