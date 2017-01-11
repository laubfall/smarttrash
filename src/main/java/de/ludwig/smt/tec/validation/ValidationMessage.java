package de.ludwig.smt.tec.validation;

/**
 * An Object that is created in case of a validation error. Holds the message and the severity of the validation.
 * 
 * @author Daniel
 *
 */
public class ValidationMessage
{
	private final String i18nKey;

	private ValidationState state = ValidationState.ERROR;
	
	public ValidationMessage(String i18nKey) {
		super();
		this.i18nKey = i18nKey;
	}

	public ValidationMessage(String i18nKey, ValidationState state) {
		super();
		this.i18nKey = i18nKey;
		this.state = state;
	}

	public String getI18nKey()
	{
		return i18nKey;
	}

	public ValidationState getState()
	{
		return state;
	}
}
