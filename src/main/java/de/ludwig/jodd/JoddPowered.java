package de.ludwig.jodd;

import java.io.IOException;

import de.ludwig.smt.SmartTrashException;
import jodd.petite.PetiteContainer;
import jodd.petite.config.AutomagicPetiteConfigurator;
import jodd.props.Props;

/**
 * Tools provided by jodd.
 * 
 * @author daniel
 *
 */
public abstract class JoddPowered {
	public static PetiteContainer petite;
	
	public static Props settings;
	
	public static void initProps(){
		settings = new Props();
		try {
			settings.load(JoddPowered.class.getResourceAsStream("/settings.properties"));
		} catch (IOException e) {
			throw new SmartTrashException("Unable to load Settings", e);
		}
	}
	
	public static void initPetite(){
		petite = new ProxettaPetiteContainer();
	    AutomagicPetiteConfigurator petiteConfigurator =
	        new AutomagicPetiteConfigurator();
	    petiteConfigurator.configure(petite);
	}
}
