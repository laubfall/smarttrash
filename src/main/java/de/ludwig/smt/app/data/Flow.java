package de.ludwig.smt.app.data;

import java.util.Date;

import de.ludwig.smt.app.config.FlowBase;
import de.ludwig.smt.app.config.FlowId;
import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import jodd.vtor.constraint.NotBlank;
import jodd.vtor.constraint.NotNull;

/**
 * A Flow as known by elastic search.
 * 
 * @author daniel
 *
 */
public class Flow
{
	/**
	 * Mandatory.
	 * Name of the flow.
	 */
	@NotBlank(message="flow.name.notblank")
	private String name;

	/**
	 * Optional.
	 * Description of the flow.
	 */
	private String description;

	/**
	 * The flowid of a {@link FlowBase} as saved in the configuration.
	 */
	@NotNull(message="flow.id.notnull")
	private FlowId id;

	private Date createdAt;
	
	public Flow() {

	}

	public Flow(String name) {
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public FlowId getId()
	{
		return id;
	}

	public void setId(FlowId id)
	{
		this.id = id;
	}

	public Date getCreatedAt()
	{
		return createdAt;
	}

	public void setCreatedAt(Date createdAt)
	{
		this.createdAt = createdAt;
	}

	@Override
	public String toString()
	{
		return "Flow [name=" + name + ", id=" + id + "]";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Flow other = (Flow) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public static String toJson(final Flow flow)
	{
		JsonSerializer js = new JsonSerializer();
		return js.serialize(flow);
	}

	public static Flow fromJson(String ref)
	{
		JsonParser jsonParser = JsonParser.create();
		return jsonParser.parse(ref, Flow.class);
	}
}
