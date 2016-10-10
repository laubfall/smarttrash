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

	private Map<SubFlowPath, SubFlow> subFlows = new HashMap<>();
	
	boolean addSubFlow(final SubFlowPath path, final SubFlow subFlow) {
		if(subFlows.containsKey(path)) {
			return false;
		}
		
		subFlows.put(new SubFlowPath(path, subFlow.getId()), subFlow);
		
		return true;
	}
}
