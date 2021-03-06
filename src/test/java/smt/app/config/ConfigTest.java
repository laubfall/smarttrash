package smt.app.config;

import java.util.Iterator;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

import smt.app.config.Config;
import smt.app.config.ConfiguredFlow;
import smt.app.config.FlowId;
import smt.app.config.SubFlow;
import smt.app.config.SubFlowPath;
import smt.app.jodd.JoddPowered;
import smt.app.jodd.JoddPoweredTest;

/**
 * Config test, testing adding Flows, SubFlow, finding Flows and SubFlows.
 * 
 * @author Daniel
 *
 */
public class ConfigTest extends JoddPoweredTest
{
	@Test
	public void addingFlows()
	{
		Config conf = JoddPowered.petite.getBean(Config.class);
		conf.addParentFlow(new ConfiguredFlow());
		Iterator<ConfiguredFlow> flowIterator = conf.parentFlowIterator();
		Assert.assertTrue(flowIterator.hasNext());
		flowIterator.next();
		Assert.assertFalse(flowIterator.hasNext());

		conf.addParentFlow(new ConfiguredFlow());
		flowIterator = conf.parentFlowIterator();
		FlowId id1 = flowIterator.next().getId();
		FlowId id2 = flowIterator.next().getId();

		Assert.assertNotNull(id1);
		Assert.assertNotNull(id2);

		Assert.assertFalse(id1.getId().equals(id2.getId()));
		Assert.assertFalse(id1.equals(id2));
	}

	@Test
	public void addingSubFlows()
	{
		Config conf = JoddPowered.petite.getBean(Config.class);
		ConfiguredFlow cf1 = new ConfiguredFlow();
		conf.addParentFlow(cf1);
		SubFlow sf1 = new SubFlow();
		Optional<SubFlowPath> addingResult = conf.addSubFlow(cf1, sf1, null);
		Assert.assertTrue(addingResult.isPresent());
		
		SubFlow sf1_1 = new SubFlow();
		SubFlowPath sf1_path = new SubFlowPath(sf1.getId());
		addingResult = conf.addSubFlow(cf1, sf1_1, sf1_path);
		Assert.assertTrue(addingResult.isPresent());
		
		SubFlow sf1_2 = new SubFlow();
		addingResult = conf.addSubFlow(cf1, sf1_2, sf1_path);
		Assert.assertTrue(addingResult.isPresent());
		
		SubFlow result = cf1.findByPath(new SubFlowPath(sf1));
		Assert.assertNotNull(result);
		
		cf1.findByPath(new SubFlowPath(sf1_path, sf1_1));
		Assert.assertNotNull(result);
		
		cf1.findByPath(new SubFlowPath(sf1_path, sf1_2));
		Assert.assertNotNull(result);
	}

	@Override
	public void setup() throws Exception
	{
		// NOOP
	}
}
