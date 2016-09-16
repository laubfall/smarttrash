package de.ludwig.smt.app.data;

/**
 * A Flow.
 * 
 * @author daniel
 *
 */
public class Flow {

	private String name;
	
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
		return "Flow [name=" + name + "]";
	}
}
