package de.ludwig.smt.tec;

import org.junit.Assert;
import org.junit.Test;

import de.ludwig.smt.app.config.Config;
import de.ludwig.smt.app.config.ConfiguredFlow;
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
		configService.saveFlowConfig(flowConfig);
		
		
		flowConfig = configService.loadFlowConfig();
		Assert.assertNotNull(flowConfig);
		Assert.assertEquals(1, flowConfig.getFlows().size());
	}

	@Override
	public void setup() throws Exception {
		// NOOP
	}
}
