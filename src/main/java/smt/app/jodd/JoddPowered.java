package smt.app.jodd;

import java.io.IOException;

import jodd.petite.PetiteContainer;
import jodd.petite.config.AutomagicPetiteConfigurator;
import jodd.props.Props;
import smt.app.SmartTrashException;
import smt.app.jodd.proxetta.ProxettaPetiteContainer;

/**
 * Tools provided by jodd.
 * 
 * @author daniel
 *
 */
public abstract class JoddPowered
{
	/**
	 * The petite container.
	 */
	public static PetiteContainer petite;

	/**
	 * Jodd powered settings.
	 */
	public static Props settings;

	/**
	 * Init the jodd powered settings.
	 */
	public static void initProps()
	{
		settings = new Props();
		try {
			settings.load(JoddPowered.class.getResourceAsStream("/settings.properties"));
		} catch (IOException e) {
			throw new SmartTrashException("Unable to load Settings", e);
		}
	}

	/**
	 * Init Petite.
	 */
	public static void initPetite()
	{
		petite = new ProxettaPetiteContainer();
		AutomagicPetiteConfigurator petiteConfigurator = new AutomagicPetiteConfigurator();
		petiteConfigurator.configure(petite);
	}
}
