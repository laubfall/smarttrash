package de.ludwig.smt.tec.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Object that holds information about the validation of another object.
 * 
 * @author Daniel
 *
 */
public class ValidationContext<VO>
{
	/**
	 * The validated object.
	 */
	private VO validatedObject;

	/**
	 * Validation messages, created while validating the {@link #validatedObject}.
	 */
	private Map<String, List<ValidationMessage>> messages = new HashMap<>();
	
	/**
	 * Constructor.
	 * @param validatedObject Object to validate.
	 */
	public ValidationContext(VO validatedObject) {
		this.validatedObject = validatedObject;
	}

	/**
	 * 
	 * @param field Optional. Name of the bean property that was validated.
	 * @param msg the validation message.
	 */
	public final void addValidationMessage(final String field, final ValidationMessage msg)
	{
		if(messages.containsKey(field) == false) {
			messages.put(field, new ArrayList<>());
		}
		
		messages.get(field).add(msg);
	}
	
	/**
	 * If there are any validation messages then the validated object seems to be valid.
	 * @return s. description.
	 */
	public final boolean isValid() {
		if(messages.isEmpty()) {
			return true;
		}
		
		return false;
	}
	
	public Set<Entry<String, List<ValidationMessage>>> messages() {
		return messages.entrySet();
	}

	public VO getValidatedObject()
	{
		return validatedObject;
	}
}
