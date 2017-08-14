package smt.app.config;

import java.io.Serializable;

/**
 * Base class, representing a flow inside the config.
 * 
 * @author Daniel
 *
 */
public class FlowBase implements Serializable
{
	/**
	 * The serial version uuid.
	 */
	private static final long serialVersionUID = 7109349041263465844L;

	/**
	 * Unique identifier of this flow.
	 */
	private FlowId id = new FlowId();

	/**
	 * Getter.
	 * 
	 * @return the unique identifier, never null.
	 */
	public FlowId getId()
	{
		return id;
	}

	@Override
	public String toString()
	{
		return "FlowBase [id=" + id + "]";
	}
}
