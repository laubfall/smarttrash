package de.ludwig.jodd;

import static de.ludwig.jodd.JoddPowered.initPetite;
import static de.ludwig.jodd.JoddPowered.initProps;
import static de.ludwig.jodd.JoddPowered.settings;

import org.junit.Before;

import de.ludwig.jodd.PropsProfiles;

/**
 * Base Test class for unit tests that depends on jodd components like configuration and DI.
 * @author Daniel
 *
 */
public abstract class JoddPoweredTest {

	static {
		initPetite();
		initProps();
		settings.setActiveProfiles(PropsProfiles.JUNIT.getProfileName());
	}
		
	/**
	 * Do any additional required setup for your tests here. Add this point all Jodd services are available.
	 * @throws Exception give implementations a chance to declare exceptions rather then catch them.
	 */
	public abstract void setup() throws Exception;
	
	
	@Before
	public void _setup() throws Exception {
		setup();
	}
}
