package de.ludwig.smt.jodd;

import static de.ludwig.smt.jodd.JoddPowered.initPetite;
import static de.ludwig.smt.jodd.JoddPowered.initProps;
import static de.ludwig.smt.jodd.JoddPowered.settings;

import org.junit.Before;

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
	@Before
	public abstract void setup() throws Exception;
}
