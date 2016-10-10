package de.ludwig.smt.jodd;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Load all elasticsearch properties and validate if there is a value.
 * @author Daniel
 *
 */
public class PropsElasticsearchPropsTest extends JoddPowered{
	@Before
	public void init(){
		initProps();
	}
	
	@Test
	public void loadAllProps(){
		for (PropsProfiles propsProfiles : PropsProfiles.values()) {
			settings.setActiveProfiles(propsProfiles.getProfileName());
			for (PropsElasticsearchProps propsElasticsearchProps : PropsElasticsearchProps.values()) {
				if (propsElasticsearchProps.isSection()){
					continue;
				}
				String value = settings.getValue(propsElasticsearchProps.getPropertyName());
				Assert.assertNotNull("no value for prop " + propsElasticsearchProps.getPropertyName() + " and profile " + propsProfiles.getProfileName(), value);
			}			
		}
	}
}
