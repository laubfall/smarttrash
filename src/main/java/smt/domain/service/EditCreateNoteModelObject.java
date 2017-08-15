package smt.domain.service;

import smt.domain.model.Note;
import spark.Request;

public class EditCreateNoteModelObject extends EditCreateDocumentModelObject<Note>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8522016539160341424L;

	private String flowId;

	@Override
	public void consumRequestParameters(Request req)
	{
		super.consumRequestParameters(req);
		
		setFlowId(req.queryMap("flowId").value());
	}

	public String getFlowId()
	{
		return flowId;
	}

	public void setFlowId(String flowId)
	{
		this.flowId = flowId;
	}
}
