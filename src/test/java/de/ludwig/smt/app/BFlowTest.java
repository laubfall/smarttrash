package de.ludwig.smt.app;

import static de.ludwig.smt.jodd.JoddPowered.petite;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.ludwig.smt.app.data.Flow;
import de.ludwig.smt.app.data.Hit;

/**
 * BFlow Test.
 * 
 * @author daniel
 *
 */
public class BFlowTest extends ElasticSearchTest {
	private BFlow bFlow;

	@Test
	public void createAndLoadFlow() throws InterruptedException {
		final Flow flow = new Flow("createAndLoadFlow");

		bFlow.createFlow(flow);

		List<Hit<Flow>> loadFlows = bFlow.loadFlows();
		Assert.assertFalse(loadFlows.isEmpty());
		Assert.assertEquals(1, loadFlows.size());
		
		bFlow.createFlow(flow);
		
		loadFlows = bFlow.loadFlows();
		Assert.assertFalse(loadFlows.isEmpty());
		Assert.assertEquals(2, loadFlows.size());
	}

	@Override
	public void setup() {
		bFlow = petite.getBean(BFlow.class);
	}
}
