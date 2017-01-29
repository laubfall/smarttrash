package de.ludwig.smt.req.frontend.tec;

import de.ludwig.smt.app.data.Flow;
import de.ludwig.smt.tec.frontend.AjaxFormSubmitResult;

/**
 * Model object for the editCreateFlow Form.
 * 
 * @author Daniel
 *
 */
public class EditCreateDocumentModelObject extends AjaxFormSubmitResult
{
	/**
	 * The serial version uid.
	 */
	private static final long serialVersionUID = 9064995216756266394L;
	
	/**
	 * Flow to edit.
	 */
	private Flow flow;

	/**
	 * ES Document ID. If null we assume that this model does not represent a saved Flow.
	 */
	private String esDocumentId;
	
	public Flow getFlow()
	{
		return flow;
	}

	public void setFlow(Flow flow)
	{
		this.flow = flow;
	}
	
	public String getJsonified() {
		return Flow.toJson(flow);
	}

	public String getEsDocumentId()
	{
		return esDocumentId;
	}

	public void setEsDocumentId(String esDocumentId)
	{
		this.esDocumentId = esDocumentId;
	}
}
