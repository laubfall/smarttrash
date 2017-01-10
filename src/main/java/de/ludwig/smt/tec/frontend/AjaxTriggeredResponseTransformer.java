package de.ludwig.smt.tec.frontend;

import de.ludwig.smt.SmartTrashException;
import de.ludwig.smt.req.frontend.tec.AjaxTriggeredResponse;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class AjaxTriggeredResponseTransformer extends JsonResponseTransformer
{

	@Override
	public String render(Object model) throws Exception
	{
		if(model instanceof AjaxTriggeredResponse == false) {
			throw new SmartTrashException("Invalid use of transformer, expected AjaxTriggeredResponse");
		}
		
		final AjaxTriggeredResponse atr = (AjaxTriggeredResponse) model;
		if(atr.getMav() != null) {
			ThymeleafTemplateEngine tte = new ThymeleafTemplateEngine();
			atr.setAppendableMarkup(tte.render(atr.getMav()));
		}
		
		return super.render(atr);
	}

}
