package de.ludwig.smt.req.backend;

import static de.ludwig.jodd.JoddPowered.petite;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.ludwig.smt.app.ElasticSearchTest;
import de.ludwig.smt.app.data.Flow;
import de.ludwig.smt.app.data.Hit;
import de.ludwig.smt.req.backend.FlowService;

/**
 * Testing flow service functionality.
 * 
 * @author daniel
 *
 */
public class FlowServiceTest extends ElasticSearchTest {
	private FlowService bFlow;

	@Test
	public void createAndLoadFlow() throws InterruptedException {
		final Flow flow = new Flow("createAndLoadFlow");
		flow.setDescription("a flow create by FlowServiceTest");
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
	public void setup() throws IOException {
		super.setup();
		bFlow = petite.getBean(FlowService.class);
	}
}
