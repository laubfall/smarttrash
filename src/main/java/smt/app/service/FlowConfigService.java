package smt.app.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import jodd.petite.meta.PetiteBean;
import smt.app.SmartTrashException;
import smt.app.config.Config;
import smt.app.config.ConfiguredFlow;
import smt.app.config.FlowBase;
import smt.app.config.FlowId;
import smt.app.config.SubFlow;
import smt.app.config.SubFlowPath;
import smt.app.jodd.JoddPowered;
import smt.app.jodd.PropsFlowConfig;
import smt.app.rdd.Requirement;
import smt.model.Flow;

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
	 * Loads a config file. Does a check if there is any config file. If not this method returns an empty config object
	 * and automatically saves this one.
	 * 
	 * @return see description.
	 */
	public Config loadFlowConfig()
	{
		File configFile = configFile();
		if (configFile.exists() == false) {
			Config newConfig = JoddPowered.petite.getBean(Config.class);
			saveFlowConfig(newConfig);
			return newConfig;
		}

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(configFile()))) {
			Object readObject = ois.readObject();
			return (Config) readObject;
		} catch (IOException | ClassNotFoundException e) {
			throw new SmartTrashException("Exception while trying to load flow config", e);
		}
	}

	/**
	 * 
	 * @param parentFlows Optional.
	 * @return
	 */
	public FlowBase createFlow(List<FlowId> parentFlows)
	{
		Config config = loadFlowConfig();
		if(parentFlows == null || parentFlows.isEmpty()) {			
			ConfiguredFlow configuredFlow = new ConfiguredFlow();
			config.addParentFlow(configuredFlow);
			return configuredFlow;
		}
		
		final SubFlow subFlow = new SubFlow();
		
		// TODO resolve parent flow or redesign that method. Why not simply put in the list of ids that contains the id of the parent?
		config.addSubFlowWithIds(null, subFlow, parentFlows);
		return subFlow;
	}

	public void updateFlow(Flow flow)
	{

	}

	public boolean isNewFlow() {
		return false;
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
