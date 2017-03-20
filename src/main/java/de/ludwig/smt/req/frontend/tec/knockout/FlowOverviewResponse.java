package de.ludwig.smt.req.frontend.tec.knockout;

import java.io.Serializable;
import java.util.List;

import de.ludwig.smt.app.data.Flow;
import de.ludwig.smt.app.data.Hit;

public class FlowOverviewResponse implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5311687089710972600L;
	private final List<Hit<Flow>> flows;

	public FlowOverviewResponse(List<Hit<Flow>> flows) {
		super();
		this.flows = flows;
	}

	public List<Hit<Flow>> getFlows()
	{
		return flows;
	}
}
