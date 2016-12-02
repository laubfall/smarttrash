package de.ludwig.smt.tec.frontend;

import de.ludwig.smt.app.data.Flow;

/**
 * 
 * @author Daniel
 *
 */
public class EditCreateFlowModel extends ModalFormResult
{
	private Flow flow;

	public Flow getFlow()
	{
		return flow;
	}

	public void setFlow(Flow flow)
	{
		this.flow = flow;
	}
}
