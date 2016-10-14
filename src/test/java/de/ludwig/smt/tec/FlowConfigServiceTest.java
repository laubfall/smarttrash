package de.ludwig.smt.tec;

import java.util.Iterator;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;

import de.ludwig.smt.app.config.Config;
import de.ludwig.smt.app.config.ConfiguredFlow;
import de.ludwig.smt.app.config.SubFlow;
import de.ludwig.smt.app.config.SubFlowPath;
import de.ludwig.smt.jodd.JoddPowered;
import de.ludwig.smt.jodd.JoddPoweredTest;


/**
 * Test for {@link FlowConfigService}.
 * 
 * @author Daniel
 *
 */
public class FlowConfigServiceTest extends JoddPoweredTest {

	@Test
	public void saveAndLoad() {
		final FlowConfigService configService = JoddPowered.petite.getBean(FlowConfigService.class);
		Config flowConfig = configService.loadFlowConfig();
		Assert.assertNotNull(flowConfig);
		Assert.assertEquals(0, flowConfig.getFlows().size());
		
		ConfiguredFlow flow = new ConfiguredFlow();
		flowConfig.addParentFlow(flow);
		
		SubFlow sub1 = new SubFlow();
		flowConfig.addSubFlow(flow, sub1, null);
		
		configService.saveFlowConfig(flowConfig);

		
		flowConfig = configService.loadFlowConfig();
		Assert.assertNotNull(flowConfig);
		Assert.assertEquals(1, flowConfig.getFlows().size());
		final Iterator<Entry<SubFlowPath, SubFlow>> subFlowIterator = flowConfig.getFlows().get(0).subFlowIterator();
		Assert.assertTrue(subFlowIterator.hasNext());
	}

	@Override
	public void setup() throws Exception {
		JoddPowered.petite.getBean(FlowConfigService.class).deleteFlowConfig();
	}
}
