package de.ludwig.smt.tec.frontend;

import java.util.HashMap;

import de.ludwig.smt.req.frontend.tec.ModalService;

/**
 * Model object for the smarttrash modal, that provides some convienience Methods to fill standard Modal Object values.
 * 
 * @author Daniel
 *
 */
public class ModalModelObject extends HashMap<String, Object>
{
	/**
	 * The serial version uid.
	 */
	private static final long serialVersionUID = 4625832409526099963L;

	/**
	 * Set the name of the thymeleaf template that should be shown inside the modal.
	 * @param templateName name of the thymeleaf template.
	 * @return this.
	 */
	public final ModalModelObject modalContentName(String templateName)
	{
		put("modalContent", templateName);
		return this;
	}

	/**
	 * Set the name of the form action. The modal has a form that is submitted if the user hits the ok button.
	 * @param formActionName  name of the form action (form attribute action).
	 * @return this.
	 */
	public final ModalModelObject formActionName(String formActionName)
	{
		put(ModalService.REQ_PARAM_ACTION_NAME, formActionName);
		return this;
	}

	/**
	 * The model object for the view that represents the content of the modal window.
	 * @param modelObject the model object.
	 * @return this.
	 */
	public final ModalModelObject modelObject(Object modelObject)
	{
		put("model", modelObject);
		return this;
	}
	
	public final ModalModelObject title(String title) {
		put("title", title);
		return this;
	}
}
