package de.ludwig.smt.tec.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 
 * @author Daniel
 *
 */
public class ValidationContext<VO>
{
	private VO validatedObject;

	private Map<String, List<ValidationMessage>> messages = new HashMap<>();
	
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
