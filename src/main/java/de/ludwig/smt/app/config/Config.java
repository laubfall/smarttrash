package de.ludwig.smt.app.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Some kind of root configuration element. Containing information about the created flows and their childs.
 * 
 * @author Daniel
 *
 */
public class Config implements Serializable
{
	/**
	 * The serial version uid;
	 */
	private static final long serialVersionUID = 1473442523415540443L;

	/**
	 * All configured flows.
	 */
	private List<ConfiguredFlow> flows = new ArrayList<ConfiguredFlow>();

	public final void addParentFlow(ConfiguredFlow flow)
	{
		flows.add(flow);
	}

	/**
	 * Adds Subflows to a given parent flow.
	 * 
	 * @param parent Not optional.
	 * @param subFlow Not optional.
	 * @param parentPath Optional.
	 * @return true if the subflow was successfuly added to the parent flow for a given path (if there is one)
	 */
	public final boolean addSubFlow(ConfiguredFlow parent, SubFlow subFlow, SubFlowPath parentPath)
	{
		if (parentPath == null) {
			return parent.addSubFlow(subFlow);
		}
		return parent.addSubFlow(parentPath, subFlow);
	}

	/**
	 * The elasticsearch flow object only contains the flow id provided by the config flow. To get further information
	 * about the positioning inside the flow hierachie it is necessary to make a lookup inside the flow configuration.
	 * 
	 * @param id the flow id.
	 */
	public FlowConfigInformation findById(FlowId id)
	{

		return null;
	}

	public Iterator<ConfiguredFlow> flowIterator()
	{
		return flows.iterator();
	}

	@Override
	public String toString()
	{
		return "Config [flows=" + flows + "]";
	}
}
