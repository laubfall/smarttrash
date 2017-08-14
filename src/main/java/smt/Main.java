package smt;

import smt.jodd.JoddPowered;
import smt.jodd.PropsProfiles;
import smt.req.backend.ApplicationService;

/**
 * Smarttrash App, starts the webserver and elasticsearch.
 * 
 * @author daniel
 *
 */
public class Main extends JoddPowered
{
	public static void main(String[] args)
	{
		new Main().start();
	}

	private void start()
	{
		initProps();
		initPetite();

		settings.setActiveProfiles(PropsProfiles.APPLICATION.getProfileName());

		final ApplicationService bApplication = petite.getBean(ApplicationService.class);
		bApplication.startApplication();
	}

}
