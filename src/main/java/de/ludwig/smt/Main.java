package de.ludwig.smt;

import de.ludwig.smt.app.BApplication;
import jodd.petite.PetiteContainer;
import jodd.petite.config.AutomagicPetiteConfigurator;

/**
 * Smarttrash App, starts the webserver and elasticsearch.
 * @author daniel
 *
 */
public class Main {

	private PetiteContainer petite;
	
	public static void main(String[] args) {
		new Main().start();
	}

	private void start(){
		initPetite();
		
		final BApplication bApplication = petite.getBean(BApplication.class);
		bApplication.startApplication();
	}
	
	private void initPetite(){
		petite = new ProxettaPetiteContainer();
	    AutomagicPetiteConfigurator petiteConfigurator =
	        new AutomagicPetiteConfigurator();
	    petiteConfigurator.configure(petite);
	}
}
