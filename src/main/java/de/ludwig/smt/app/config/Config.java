package de.ludwig.smt.app.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import de.ludwig.rdd.Requirement;
import jodd.petite.meta.PetiteBean;

/**
 * Some kind of root configuration element. Containing information about the created flows and their childs.
 * 
 * @author Daniel
 *
 */
@PetiteBean
public class Config implements Serializable
{
	/**
	 * The serial version uid;
	 */
	private static final long serialVersionUID = 1473442523415540443L;

	/**
	 * All configured flows. Configured flows are also known as parent flows.
	 */
	private List<ConfiguredFlow> flows = new ArrayList<ConfiguredFlow>();

	@Requirement
	public void addParentFlow(ConfiguredFlow flow)
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
	@Requirement
	public Optional<SubFlowPath> addSubFlow(ConfiguredFlow parent, SubFlow subFlow, SubFlowPath parentPath)
	{
		if (parentPath == null) {
			return parent.addSubFlow(subFlow);
		}
		return parent.addSubFlow(parentPath, subFlow);
	}

	public Optional<SubFlowPath> addSubFlowWithIds(ConfiguredFlow parent, SubFlow subFlow, List<FlowId> pathIds)
	{
		SubFlowPath sfp = null;
		for(FlowId id : pathIds) {
			if(sfp == null) {
				sfp = new SubFlowPath(id);
			} else {
				sfp = new SubFlowPath(sfp, id);
			}
		}
		
		return addSubFlow(parent, subFlow, sfp);
	}

	/**
	 * The elasticsearch flow object only contains the flow id provided by the config flow. To get further information
	 * about the positioning inside the flow hierarchy it is necessary to make a lookup inside the flow configuration.
	 * 
	 * @param id the flow id.
	 */
	@Requirement
	public FlowConfigInformation findById(FlowId id)
	{

		return null;
	}

	/**
	 * Initially we looking for all the parent flows that exists. This method provides access to these parent flows.
	 * 
	 * @return Iterator over all parent flows.
	 */
	@Requirement
	public Iterator<ConfiguredFlow> parentFlowIterator()
	{
		return flows.iterator();
	}

	/**
	 * Gives you access to all the {@link SubFlow} stored under a given {@link SubFlowPath} that represents a parent
	 * {@link SubFlow}.
	 * 
	 * @param path path of the parent {@link SubFlow}.
	 * @return Iterator over all {@link SubFlow}s that are childs of the parent {@link SubFlow}.
	 */
	@Requirement
	public Iterator<SubFlow> childFlowIterator(final SubFlowPath path)
	{
		// parentFlowIterator().forEachRemaining(action);

		return null;
	}

	@Override
	public String toString()
	{
		return "Config [flows=" + flows + "]";
	}
}
