package de.ludwig.smt.app.config;

import java.util.HashMap;
import java.util.Map;

/**
 * A Flow stored in the configuration.
 * 
 * @author Daniel
 *
 */
public class ConfiguredFlow extends FlowBase {

	/**
	 * The serial version uuid.
	 */
	private static final long serialVersionUID = -778492061460783490L;

	/**
	 * Configuration of subflows. The key is the path-definition to the subflow.
	 * The value is the subflow itself
	 */
	private Map<SubFlowPath, SubFlow> subFlows = new HashMap<>();

	/**
	 * Adds a subflow for a given path. For every {@link FlowId} inside
	 * {@link SubFlowPath} there has to be a {@link SubFlow} inside
	 * {@link #subFlows}. Otherwise the new subflow is not added to the map.
	 * 
	 * @param path
	 *            Path where we want to add a new {@link SubFlow}. Do not add
	 *            the {@link FlowId} of the new {@link SubFlow} by yourself to
	 *            the path. This is done by this method.
	 * @param subFlow
	 *            {@link SubFlow} to add.
	 * @return false if there is already a subflow for the given path, or if the
	 *         path contains {@link FlowId}s that do not have a {@link SubFlow}.
	 */
	final boolean addSubFlow(final SubFlowPath path, final SubFlow subFlow) {
		final SubFlowPath newPath = new SubFlowPath(path, subFlow.getId());
		if (subFlows.containsKey(newPath)) {
			return false;
		}

		// Check the existance of flows described by the SubFlowPath
		
		
		subFlows.put(newPath, subFlow);

		return true;
	}
}
