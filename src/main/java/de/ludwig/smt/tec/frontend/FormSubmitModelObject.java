package de.ludwig.smt.tec.frontend;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Model Object class, providing some base functionality in case of submitted forms.
 * @author Daniel
 *
 */
public abstract class FormSubmitModelObject implements Serializable
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
	public final FormSubmitModelObject addMessage(FormMessage message) {
		messages.add(message);
		return this;
	}
	
	public Set<FormMessage> getMessages()
	{
		return messages;
	}
}
