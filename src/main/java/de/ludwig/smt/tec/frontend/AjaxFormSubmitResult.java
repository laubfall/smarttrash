package de.ludwig.smt.tec.frontend;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Base class for response results for ajax triggered form submits.
 * @author Daniel
 *
 */
public abstract class AjaxFormSubmitResult implements Serializable
{
	/**
	 * The serial version uid.
	 */
	private static final long serialVersionUID = -6299878068149413937L;

	/**
	 * Messages.
	 */
	private final Set<FormMessage> messages = new HashSet<>();

	/**
	 * Shortcut fluent method for adding a new message.
	 * @param message
	 * @return
	 */
	public final AjaxFormSubmitResult addMessage(FormMessage message) {
		messages.add(message);
		return this;
	}
	
	public Set<FormMessage> getMessages()
	{
		return messages;
	}
}
