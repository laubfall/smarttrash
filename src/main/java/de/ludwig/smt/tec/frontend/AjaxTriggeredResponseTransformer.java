package de.ludwig.smt.tec.frontend;

import de.ludwig.smt.SmartTrashException;
import de.ludwig.smt.req.frontend.tec.AjaxTriggeredResponse;
import spark.ModelAndView;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

/**
 * Expects an model object of type {@link AjaxTriggeredResponse}. If this model contains a {@link ModelAndView} object
 * the transformer tries to render html markup with the thymeleaf engine. The resulting markup is stored inside a json
 * object (the serialized AjaxTriggeredResponse).
 * 
 * @author Daniel
 *
 */
public class AjaxTriggeredResponseTransformer extends JsonResponseTransformer
{

	@Override
	public String render(Object model) throws Exception
	{
		if (model instanceof AjaxTriggeredResponse == false) {
			throw new SmartTrashException("Invalid use of transformer, expected AjaxTriggeredResponse");
		}

		final AjaxTriggeredResponse atr = (AjaxTriggeredResponse) model;
		if (atr.getMav() != null) {
			ThymeleafTemplateEngine tte = new ThymeleafTemplateEngine();
			atr.setAppendableMarkup(tte.render(atr.getMav()));
		}

		return super.render(atr);
	}

}
