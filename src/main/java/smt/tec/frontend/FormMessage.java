package smt.tec.frontend;

import java.io.Serializable;

/**
 * A message created in order of a form submit.
 * @author Daniel
 *
 */
public class FormMessage implements Serializable
{

	/**
	 * The serial version uid.
	 */
	private static final long serialVersionUID = -8892838259368795943L;

	/**
	 * The human readable message.
	 */
	private final String message;
	
	/**
	 * Simple indication of the relevance of this message. It is up to the gui interpreting this value.
	 * Example values: 0 - Info, 1 - Warn, 2 - Error
	 */
	private final int level;

	/**
	 * If not null this message is a message that targets a specific input element
	 */
	private String inputElementId;
	
	/**
	 * Constructor.
	 * @param message human readable message.
	 * @param level severity of the message
	 */
	public FormMessage(String message, int level) {
		super();
		this.message = message;
		this.level = level;
	}

	/**
	 * Constructor.
	 * @param message human readable message.
	 * @param level severity of the message
	 * @param inputElementId TODO whats that for?
	 */
	public FormMessage(String message, int level, String inputElementId) {
		super();
		this.message = message;
		this.level = level;
		this.inputElementId = inputElementId;
	}

	public String getMessage()
	{
		return message;
	}

	public int getLevel()
	{
		return level;
	}

	public String getInputElementId()
	{
		return inputElementId;
	}

	public String getModalMsgCssClass() {
		switch (level) {
		case 1:
			return "bg-info";
		case 2:
			return "bg-warning";
		case 3:
			return "bg-danger";
		default:
			return "";
		}
	}
}
