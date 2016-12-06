package de.ludwig.smt.req.backend;

import static de.ludwig.jodd.JoddPowered.petite;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.ludwig.smt.app.ElasticSearchTest;
import de.ludwig.smt.app.data.Flow;
import de.ludwig.smt.app.data.Hit;
import de.ludwig.smt.req.backend.FlowService;

/**
 * BFlow Test.
 * 
 * @author daniel
 *
 */
public class BFlowTest extends ElasticSearchTest {
	private FlowService bFlow;

	@Test
	public void createAndLoadFlow() throws InterruptedException {
		final Flow flow = new Flow("createAndLoadFlow");

		bFlow.saveFlow(flow, null);

		List<Hit<Flow>> loadFlows = bFlow.loadFlows();
		Assert.assertFalse(loadFlows.isEmpty());
		Assert.assertEquals(1, loadFlows.size());
		
		bFlow.saveFlow(flow, null);
		
		loadFlows = bFlow.loadFlows();
		Assert.assertFalse(loadFlows.isEmpty());
		Assert.assertEquals(2, loadFlows.size());
	}

	@Override
	public void setup() {
		bFlow = petite.getBean(FlowService.class);
	}
}
