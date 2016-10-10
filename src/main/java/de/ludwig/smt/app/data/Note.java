package de.ludwig.smt.app.data;

import de.ludwig.smt.app.config.FlowId;

/**
 * A Note.
 * @author daniel
 *
 */
public class Note {
	private String content;
	
	private FlowId flow;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public FlowId getFlow() {
		return flow;
	}

	public void setFlow(FlowId flow) {
		this.flow = flow;
	}
}
