package de.ludwig.smt;

import de.ludwig.jodd.JoddPowered;
import de.ludwig.smt.req.backend.BApplication;

/**
 * Smarttrash App, starts the webserver and elasticsearch.
 * 
 * @author daniel
 *
 */
public class Main extends JoddPowered {

	public static void main(String[] args) {
		new Main().start();
	}

	private void start() {
		initPetite();

		final BApplication bApplication = petite.getBean(BApplication.class);
		bApplication.startApplication();
	}

}
