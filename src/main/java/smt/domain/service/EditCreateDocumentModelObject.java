package smt.domain.service;

import smt.app.frontend.FormSubmitModelObject;
import spark.Request;

/**
 * Model object for the editCreateFlow Form.
 * 
 * @author Daniel
 *
 */
public class EditCreateDocumentModelObject<D> extends FormSubmitModelObject
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

	public void setDocument(D document)
	{
		this.document = document;
	}

	public String getEsDocumentId()
	{
		return esDocumentId;
	}

	public void setEsDocumentId(String esDocumentId)
	{
		this.esDocumentId = esDocumentId;
	}

	public void consumRequestParameters(Request req)
	{

	}
}
