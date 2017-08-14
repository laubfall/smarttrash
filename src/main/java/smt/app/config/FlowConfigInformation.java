package smt.app.config;

/**
 * Holds information about the parent flow, the flow path and the subflow, when made a lookup for a flow id.
 * @author Daniel
 *
 */
public class FlowConfigInformation {
	/**
	 * The parent flow, never null.
	 */
	private ConfiguredFlow parent;
	
	/**
	 * Subflow, not null if the id is the id of a subflow. Optional.
	 */
	private SubFlow subFlow;
	
	/**
	 * Path to the subflow. Null if there is no subflow.
	 */
	private SubFlowPath path;

	/**
	 * Used if the {@link FlowId} to look for is a {@link ConfiguredFlow}.
	 * @param parent the configured flow that has the given id.
	 */
	public FlowConfigInformation(ConfiguredFlow parent) {
		super();
		this.parent = parent;
	}

	/**
	 * Used if the {@link FlowId} to look for is {@link SubFlow}. 
	 * @param parent
	 * @param subFlow
	 * @param path
	 */
	public FlowConfigInformation(ConfiguredFlow parent, SubFlow subFlow, SubFlowPath path) {
		super();
		this.parent = parent;
		this.subFlow = subFlow;
		this.path = path;
	}

	public SubFlow getSubFlow() {
		return subFlow;
	}

	public void setSubFlow(SubFlow subFlow) {
		this.subFlow = subFlow;
	}

	public ConfiguredFlow getParent() {
		return parent;
	}

	public SubFlowPath getPath() {
		return path;
	}
	
}
