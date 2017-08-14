package smt.app.jodd;

import static smt.app.jodd.JoddPowered.initPetite;
import static smt.app.jodd.JoddPowered.initProps;
import static smt.app.jodd.JoddPowered.settings;

import org.junit.Before;

import smt.app.jodd.PropsProfiles;

/**
 * Base Test class for unit tests that depends on jodd components like configuration and DI.
 * 
 * @author Daniel
 *
 */
public abstract class JoddPoweredTest
{

	static {
		initPetite();
		initProps();
		settings.setActiveProfiles(PropsProfiles.JUNIT.getProfileName());
	}

	/**
	 * Do any additional required setup for your tests here. Add this point all Jodd services are available.
	 * 
	 * @throws Exception give implementations a chance to declare exceptions rather then catch them.
	 */
	public abstract void setup() throws Exception;

	@Before
	public void _setup() throws Exception
	{
		setup();
	}
}
