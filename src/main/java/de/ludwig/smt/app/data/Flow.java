package de.ludwig.smt.app.data;

import de.ludwig.smt.app.config.FlowId;

/**
 * A Flow as known by elastic search.
 * 
 * @author daniel
 *
 */
public class Flow {

	private String name;
	
	private String description;
	
	/**
	 * A unique ID identifying this flow in the configuration.
	 */
	private FlowId id;

	public Flow(){
		
	}
	
	public Flow(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		// TODO recreate with new properties
		return "Flow [name=" + name + "]";
	}
}
