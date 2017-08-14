package smt.req.backend.tec;

import java.util.Iterator;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;

import smt.app.config.Config;
import smt.app.config.ConfiguredFlow;
import smt.app.config.SubFlow;
import smt.app.config.SubFlowPath;
import smt.jodd.JoddPowered;
import smt.jodd.JoddPoweredTest;
import smt.req.backend.tec.FlowConfigService;

/**
 * Test for {@link FlowConfigService}.
 * 
 * @author Daniel
 *
 */
public class FlowConfigServiceTest extends JoddPoweredTest
{

	@Test
	public void saveAndLoad()
	{
		final FlowConfigService configService = JoddPowered.petite.getBean(FlowConfigService.class);
		Config flowConfig = configService.loadFlowConfig();
		Assert.assertNotNull(flowConfig);
		Iterator<ConfiguredFlow> flowIterator = flowConfig.parentFlowIterator();
		Assert.assertFalse(flowIterator.hasNext());
		
		ConfiguredFlow flow = new ConfiguredFlow();
		flowConfig.addParentFlow(flow);

		SubFlow sub1 = new SubFlow();
		flowConfig.addSubFlow(flow, sub1, null);

		configService.saveFlowConfig(flowConfig);

		flowConfig = configService.loadFlowConfig();
		Assert.assertNotNull(flowConfig);
		flowIterator = flowConfig.parentFlowIterator();
		Assert.assertTrue(flowIterator.hasNext());
		final Iterator<Entry<SubFlowPath, SubFlow>> subFlowIterator = flowIterator.next().subFlowIterator();
		Assert.assertTrue(subFlowIterator.hasNext());
	}

	@Override
	public void setup() throws Exception
	{
		JoddPowered.petite.getBean(FlowConfigService.class).deleteFlowConfig();
	}
}
