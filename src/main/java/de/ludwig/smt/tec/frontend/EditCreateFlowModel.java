package de.ludwig.smt.tec.frontend;

import de.ludwig.smt.app.data.Flow;

/**
 * Model object for the editCreateFlow Form.
 * 
 * @author Daniel
 *
 */
public class EditCreateFlowModel extends ModalFormResult
{
	/**
	 * The serial version uid.
	 */
	private static final long serialVersionUID = 9064995216756266394L;
	
	/**
	 * Flow to edit.
	 */
	private Flow flow;

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
