package de.ludwig.smt.req.frontend.tec;

import java.io.Serializable;

import spark.ModelAndView;

/**
 * To json serializable object that is used as a response for an ajax request (GET, POST, etc).
 * 
 * @author Daniel
 *
 */
public class AjaxTriggeredResponse implements Serializable
{

	/**
	 * The serial version uid.
	 */
	private static final long serialVersionUID = -2140780629373355977L;

	/**
	 * Markup that is ready for use.
	 */
	private String appendableMarkup;
	
	/**
	 * JS that can be evaluated by the browser.
	 */
	private String evaluatableJS;
	
	private Usage usage = Usage.MARKUP_ONLY;
	
	private transient ModelAndView mav;
	
	public String getAppendableMarkup()
	{
		return appendableMarkup;
	}

	public void setAppendableMarkup(String appendableMarkup)
	{
		this.appendableMarkup = appendableMarkup;
	}

	public String getEvaluatableJS()
	{
		return evaluatableJS;
	}

	public void setEvaluatableJS(String evaluatableJS)
	{
		this.evaluatableJS = evaluatableJS;
	}

	public Usage getUsage()
	{
		return usage;
	}

	public void setUsage(Usage usage)
	{
		this.usage = usage;
	}

	public ModelAndView getMav()
	{
		return mav;
	}

	public void setMav(ModelAndView mav)
	{
		this.mav = mav;
	}

	/**
	 * Controll structure to signal possible callers what to do with this response.
	 * @author Daniel
	 *
	 */
	public static enum Usage {
		MARKUP_ONLY,
		JS_ONLY,
		BOTH,
		;
	}
}
