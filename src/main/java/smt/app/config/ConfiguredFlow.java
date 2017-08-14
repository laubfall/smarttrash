package smt.app.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
	final SubFlow findByPath(final SubFlowPath path)
	{
		return subFlows.get(path);
	}

	/**
	 * Find all SubFlows of a given {@link ConfiguredFlow} or {@link SubFlow}.
	 * 
	 * @param path Not optional. Path to the parent flow object.
	 * @return all the child flows, never null.
	 */
	final List<SubFlow> findChildsByPath(final SubFlowPath path)
	{
		return subFlows.keySet().parallelStream().filter(subPath -> subPath.parentSubFlowPath().equals(path))
				.map(subPath -> subFlows.get(subPath)).collect(Collectors.toList());
	}

	/**
	 * Adds a subflow for a given path. For every {@link FlowId} inside {@link SubFlowPath} there has to be a
	 * {@link SubFlow} inside {@link #subFlows}. Otherwise the new subflow is not added to the map.
	 * 
	 * @param path Optional. Path where we want to add a new {@link SubFlow}. Do not add the {@link FlowId} of the new
	 *            {@link SubFlow} by yourself to the path. This is done by this method. If the path is null the new
	 *            subflow is going to be the first subflow in the hierachy.
	 * @param subFlow {@link SubFlow} to add.
	 * @return TODO javadoc
	 */
	final Optional<SubFlowPath> addSubFlow(final SubFlowPath path, final SubFlow subFlow)
	{
		final SubFlowPath newPath = new SubFlowPath(path, subFlow.getId());
		if (subFlows.containsKey(newPath)) {
			return Optional.empty();
		}

		// Check the existance of flows described by the SubFlowPath
		if (subFlowExists(path.currentPath()) == false) {
			// TODO maybe some logging?
			return Optional.empty();
		}

		subFlows.put(newPath, subFlow);

		return Optional.of(newPath);
	}

	final Optional<SubFlowPath> addSubFlow(final SubFlow subFlow)
	{
		final SubFlowPath newPath = new SubFlowPath(subFlow.getId());

		if (subFlows.containsKey(newPath)) {
			return Optional.empty();
		}

		subFlows.put(newPath, subFlow);

		return Optional.of(newPath);
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
