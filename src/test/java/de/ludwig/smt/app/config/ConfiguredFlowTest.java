package de.ludwig.smt.app.config;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.Test;


/**
 * 
 * @author Daniel
 *
 */
public class ConfiguredFlowTest
{
	@Test
	public void findChildsByPath() {
		final ConfiguredFlow configuredFlow = new ConfiguredFlow();
		Optional<SubFlowPath> optPath = configuredFlow.addSubFlow(new SubFlow());
		
		List<SubFlow> childsByPath = configuredFlow.findChildsByPath(optPath.get());
		assertFalse(childsByPath.isEmpty());
		assertEquals(1, childsByPath.size());
		
		optPath = configuredFlow.addSubFlow(optPath.get(), new SubFlow());
		assertTrue(optPath.isPresent());
		
		childsByPath = configuredFlow.findChildsByPath(new SubFlowPath(optPath.get().parentPath()));
		assertFalse(childsByPath.isEmpty());
		assertEquals(2, childsByPath.size());
		
		Optional<SubFlowPath> thirdChildPath = configuredFlow.addSubFlow(optPath.get(), new SubFlow()); // configuredflow-subflow-subflow-sublfow
		childsByPath = configuredFlow.findChildsByPath(new SubFlowPath(optPath.get().parentPath()));
		assertFalse(childsByPath.isEmpty());
		assertEquals(2, childsByPath.size());
		
		childsByPath = configuredFlow.findChildsByPath(thirdChildPath.get().parentSubFlowPath());
		assertFalse(childsByPath.isEmpty());
		assertEquals(1, childsByPath.size());
	}
}
