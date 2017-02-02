package de.ludwig.smt.req.frontend.tec;

import de.ludwig.smt.tec.frontend.AjaxFormSubmitResult;

/**
 * Model object for the editCreateFlow Form.
 * 
 * @author Daniel
 *
 */
public class EditCreateDocumentModelObject<D> extends AjaxFormSubmitResult
{
	/**
	 * The serial version uid.
	 */
	private static final long serialVersionUID = 9064995216756266394L;
	
	/**
	 * Flow to edit.
	 */
	private D document;

	/**
	 * ES Document ID. If null we assume that this model does not represent a saved Flow.
	 */
	private String esDocumentId;
	
	public D getDocument()
	{
		return document;
	}

	public void setDocument(D flow)
	{
		this.document = flow;
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
