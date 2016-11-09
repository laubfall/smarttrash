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
