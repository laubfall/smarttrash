package smt.tec.frontend;

import java.io.Serializable;

import spark.ModelAndView;

/**
 * To json serializable object that is used as a response for an ajax request (GET, POST, etc).
 * 
 * If you use this class in combination with the {@link AjaxTriggeredResponseTransformer} the field {@link #mav} is used
 * to create the markup based on the view.
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

	/**
	 * Used by {@link AjaxTriggeredResponseTransformer} to create markup from the view.
	 */
	private transient ModelAndView mav;

	public String getAppendableMarkup()
	{
		return appendableMarkup;
	}

	public AjaxTriggeredResponse appendableMarkup(String appendableMarkup)
	{
		setAppendableMarkup(appendableMarkup);
		return this;
	}

	public void setAppendableMarkup(String appendableMarkup)
	{
		this.appendableMarkup = appendableMarkup;
	}

	public String getEvaluatableJS()
	{
		return evaluatableJS;
	}

	public AjaxTriggeredResponse evaluatableJS(String evaluatableJS)
	{
		setEvaluatableJS(evaluatableJS);
		return this;
	}

	public void setEvaluatableJS(String evaluatableJS)
	{
		this.evaluatableJS = evaluatableJS;
	}

	public Usage getUsage()
	{
		return usage;
	}

	public AjaxTriggeredResponse usage(Usage usage)
	{
		setUsage(usage);
		return this;
	}

	public void setUsage(Usage usage)
	{
		this.usage = usage;
	}

	public ModelAndView getMav()
	{
		return mav;
	}

	public AjaxTriggeredResponse mav(ModelAndView mav)
	{
		setMav(mav);
		return this;
	}

	public void setMav(ModelAndView mav)
	{
		this.mav = mav;
	}

	/**
	 * Controll structure to signal possible callers what to do with this response.
	 * 
	 * @author Daniel
	 *
	 */
	public static enum Usage
	{
		MARKUP_ONLY, JS_ONLY, BOTH,;
	}
}
