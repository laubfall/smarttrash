package de.ludwig.smt.app.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Describes the child-parent relations of Flows.
 * 
 * @author Daniel
 *
 */
public class SubFlowPath implements Serializable
{
	/**
	 * The serial version uid.
	 */
	private static final long serialVersionUID = 9217888641211204680L;

	/**
	 * Full path to the flow.
	 */
	private List<FlowId> path = new ArrayList<>();

	/**
	 * Constructor used in case there is no other subflow where we want to add the new subflow.
	 * 
	 * @param id id of the new sublfow.
	 */
	public SubFlowPath(FlowId id) {
		this.path.add(id);
	}

	/**
	 * Constructor.
	 * @param parent
	 * @param id
	 */
	public SubFlowPath(SubFlowPath parent, FlowId id) {
		this.path = new ArrayList<>(parent.path);
		this.path.add(id);
	}
	
	/**
	 * Constructor.
	 * @param subFlow
	 */
	public SubFlowPath(SubFlow subFlow) {
		this(subFlow.getId());
	}
	
	/**
	 * Constructor.
	 * @param parent
	 * @param subFlow
	 */
	public SubFlowPath(SubFlowPath parent, SubFlow subFlow) {
		this(parent, subFlow.getId());
	}

	/**
	 * Ad-hoc constructor. Internal use only.
	 * 
	 * @param ids
	 */
	SubFlowPath(Collection<FlowId> ids) {
		path.addAll(ids);
	}

	/**
	 * Returns the current path as a new list.
	 * 
	 * @return new instance of this path as a linked list. A linked list give us some advantages when operationg on flow paths. 
	 */
	LinkedList<FlowId> currentPath()
	{
		return new LinkedList<>(path);
	}

	LinkedList<FlowId> parentPath() {
		LinkedList<FlowId> linkedList = new LinkedList<>(path);
		if(path.size() == 1) {
			return linkedList; 
		}
		
		linkedList.pollLast();
		return linkedList;
	}
	
	SubFlowPath parentSubFlowPath() {
		return new SubFlowPath(parentPath());
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SubFlowPath other = (SubFlowPath) obj;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}
}