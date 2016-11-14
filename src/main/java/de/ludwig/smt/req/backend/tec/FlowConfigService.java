package de.ludwig.smt.req.backend.tec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import de.ludwig.jodd.JoddPowered;
import de.ludwig.jodd.PropsFlowConfig;
import de.ludwig.rdd.Requirement;
import de.ludwig.smt.SmartTrashException;
import de.ludwig.smt.app.config.Config;
import jodd.petite.meta.PetiteBean;

/**
 * Provides operations on a flow configuration.
 * 
 * @author Daniel
 *
 */
@PetiteBean
@Requirement // TODO provide specifiation for this requirement
public class FlowConfigService
{
	/**
	 * Save a flow config. Place and config name is defined in the jodd settings file, Properties are here:
	 * {@link PropsFlowConfig}.
	 * 
	 * @param config config to save.
	 */
	public void saveFlowConfig(final Config config)
	{
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(configFile()))) {
			oos.writeObject(config);
		} catch (IOException e) {
			new SmartTrashException("Exception while trying to save the flow config", e);
		}
	}

	/**
	 * Loads a config file. Does a check if there is any config file. If not this method returns an empty config object.
	 * 
	 * @return see description.
	 */
	public Config loadFlowConfig()
	{
		File configFile = configFile();
		if (configFile.exists() == false) {
			return JoddPowered.petite.getBean(Config.class);
		}

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(configFile()))) {
			Object readObject = ois.readObject();
			return (Config) readObject;
		} catch (IOException | ClassNotFoundException e) {
			throw new SmartTrashException("Exception while trying to load flow config", e);
		}
	}

	boolean deleteFlowConfig()
	{
		return configFile().delete();
	}

	private File configFile()
	{
		final String configPath = JoddPowered.settings.getValue(PropsFlowConfig.PATH.getPropName());
		final File file = new File(configPath, JoddPowered.settings.getValue(PropsFlowConfig.NAME.getPropName()));
		return file;
	}
}
