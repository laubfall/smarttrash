package de.ludwig.smt.app.config;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;

/**
 * Config test, testing adding Flows, SubFlow, finding Flows and SubFlows.
 * 
 * @author Daniel
 *
 */
public class ConfigTest
{
	@Test
	public void addingFlows()
	{
		Config conf = new Config();
		conf.addParentFlow(new ConfiguredFlow());
		Iterator<ConfiguredFlow> flowIterator = conf.flowIterator();
		Assert.assertTrue(flowIterator.hasNext());
		flowIterator.next();
		Assert.assertFalse(flowIterator.hasNext());

		conf.addParentFlow(new ConfiguredFlow());
		flowIterator = conf.flowIterator();
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
		Config conf = new Config();
		ConfiguredFlow cf1 = new ConfiguredFlow();
		conf.addParentFlow(cf1);
		SubFlow sf1 = new SubFlow();
		boolean addingResult = conf.addSubFlow(cf1, sf1, null);
		Assert.assertTrue(addingResult);
		
		SubFlow sf1_1 = new SubFlow();
		SubFlowPath sf1_path = new SubFlowPath(sf1.getId());
		addingResult = conf.addSubFlow(cf1, sf1_1, sf1_path);
		Assert.assertTrue(addingResult);
		
		SubFlow sf1_2 = new SubFlow();
		addingResult = conf.addSubFlow(cf1, sf1_2, sf1_path);
		Assert.assertTrue(addingResult);
		
		SubFlow result = cf1.findByPath(new SubFlowPath(sf1));
		Assert.assertNotNull(result);
		
		cf1.findByPath(new SubFlowPath(sf1_path, sf1_1));
		Assert.assertNotNull(result);
		
		cf1.findByPath(new SubFlowPath(sf1_path, sf1_2));
		Assert.assertNotNull(result);
	}
}
