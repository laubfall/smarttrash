package de.ludwig.smt.app.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * A Flow stored in the configuration.
 * 
 * @author Daniel
 *
 */
public class ConfiguredFlow extends FlowBase
{

	/**
	 * The serial version uuid.
	 */
	private static final long serialVersionUID = -778492061460783490L;

	/**
	 * Configuration of subflows. The key is the path-definition to the subflow. The value is the subflow itself
	 */
	private Map<SubFlowPath, SubFlow> subFlows = new HashMap<>();

	/**
	 * Provides access to the subflows, without giving the possibility to modify the map.
	 * 
	 * @return iterator for {@link SubFlow}.
	 */
	public final Iterator<Map.Entry<SubFlowPath, SubFlow>> subFlowIterator()
	{
		return subFlows.entrySet().iterator();
	}
	
	/**
	 * 
	 * @param path path to look for, not optional.
	 * @return null if there is no subflow for the given path, otherwise the searched subflow.
	 */
	public final SubFlow findByPath(final SubFlowPath path) {
		return subFlows.get(path);
	}

	/**
	 * Adds a subflow for a given path. For every {@link FlowId} inside {@link SubFlowPath} there has to be a
	 * {@link SubFlow} inside {@link #subFlows}. Otherwise the new subflow is not added to the map.
	 * 
	 * @param path Optional. Path where we want to add a new {@link SubFlow}. Do not add the {@link FlowId} of the new
	 *            {@link SubFlow} by yourself to the path. This is done by this method. If the path is null the new
	 *            subflow is going to be the first subflow in the hierachy.
	 * @param subFlow {@link SubFlow} to add.
	 * @return false if there is already a subflow for the given path, or if the path contains {@link FlowId}s that do
	 *         not have a {@link SubFlow}.
	 */
	final boolean addSubFlow(final SubFlowPath path, final SubFlow subFlow)
	{
		final SubFlowPath newPath = new SubFlowPath(path, subFlow.getId());
		if (subFlows.containsKey(newPath)) {
			return false;
		}

		// Check the existance of flows described by the SubFlowPath
		if (subFlowExists(path.currentPath()) == false) {
			// TODO maybe some logging?
			return false;
		}

		subFlows.put(newPath, subFlow);

		return true;
	}

	final boolean addSubFlow(final SubFlow subFlow)
	{
		final SubFlowPath newPath = new SubFlowPath(subFlow.getId());

		if (subFlows.containsKey(newPath)) {
			return false;
		}

		subFlows.put(newPath, subFlow);

		return true;
	}

	private boolean subFlowExists(LinkedList<FlowId> currentPath)
	{
		if (subFlows.containsKey(new SubFlowPath(currentPath)) == false) {
			return false;
		}

		if (currentPath.size() == 1) { // end of path, that is the first element
										// in the linked list.
			return true;
		}

		currentPath.pollLast(); // remove the last element so we can check the
								// subpath.
		return subFlowExists(currentPath);
	}
}
