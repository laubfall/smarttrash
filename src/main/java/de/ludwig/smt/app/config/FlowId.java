package de.ludwig.smt.app.config;

import java.io.Serializable;

import jodd.util.RandomString;

/**
 * A flow ID is a "unique" identifier for a flow. This id is used to create a
 * relationship between Flows stored inside the config and flows stored inside
 * Elasticsearch.
 * 
 * Remember: actually there is no unique key authority that ensures the
 * uniqueness of the generated id.
 * 
 * @author Daniel
 *
 */
public final class FlowId implements Serializable {
	/**
	 * The serial version uid.
	 */
	private static final long serialVersionUID = 7660173547699807213L;

	/**
	 * Random-ID.
	 */
	private String id = RandomString.getInstance().randomAlphaNumeric(10);

	public FlowId() {

	}

	public final String getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FlowId other = (FlowId) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}