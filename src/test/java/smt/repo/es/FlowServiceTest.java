package smt.repo.es;

import static smt.app.jodd.JoddPowered.petite;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import smt.app.AbstractElasticSearchTest;
import smt.domain.model.Flow;
import smt.domain.model.Hit;
import smt.persistence.es.FlowService;

/**
 * Testing flow service functionality.
 * 
 * @author daniel
 *
 */
public class FlowServiceTest extends AbstractElasticSearchTest {
	private FlowService bFlow;

	@Test
	public void createAndLoadFlow() throws InterruptedException {
		final Flow flow = new Flow("createAndLoadFlow");
		flow.setDescription("a flow create by FlowServiceTest");
		bFlow.saveFlow(flow, null, null);

		List<Hit<Flow>> loadFlows = bFlow.loadFlows();
		Assert.assertFalse(loadFlows.isEmpty());
		Assert.assertEquals(1, loadFlows.size());
		
		bFlow.saveFlow(flow, null, null);
		
		loadFlows = bFlow.loadFlows();
		Assert.assertFalse(loadFlows.isEmpty());
		Assert.assertEquals(2, loadFlows.size());
	}

	@Override
	public void setup() throws IOException {
//		super.setup();
		bFlow = petite.getBean(FlowService.class);
	}
}
