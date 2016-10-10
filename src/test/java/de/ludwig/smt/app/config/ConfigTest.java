package de.ludwig.smt.app.config;

import org.junit.Test;

// TODO implement this test. Actually it does nothing.
public class ConfigTest {
	@Test
	public void createConfig() {
		Config conf = new Config();
		ConfiguredFlow cf1 = new ConfiguredFlow();
		conf.addParentFlow(cf1);
	}
}
