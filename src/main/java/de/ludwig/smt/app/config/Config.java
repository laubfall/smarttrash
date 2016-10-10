package de.ludwig.smt.app.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Some kind of root configuration element. Containing information about the
 * created flows and their childs.
 * 
 * @author Daniel
 *
 */
public class Config implements Serializable {
	/**
	 * The serial version uid;
	 */
	private static final long serialVersionUID = 1473442523415540443L;

	/**
	 * All configured flows.
	 */
	private List<ConfiguredFlow> flows = new ArrayList<ConfiguredFlow>();

	public final void addParentFlow(ConfiguredFlow flow) {
		flows.add(flow);
	}

	public final boolean addSubFlow(ConfiguredFlow parent, SubFlow subFlow, SubFlowPath parentPath) {
		return parent.addSubFlow(parentPath, subFlow);
	}

	/**
	 * The elasticsearch flow object only contains the flow id provided by the
	 * config flow. To get further information about the positioning inside the
	 * flow hierachie it is necessary to make a lookup inside the flow
	 * configuration.
	 * 
	 * @param id
	 *            the flow id.
	 */
	public FlowConfigInformation findById(FlowId id) {

		return null;
	}
}
