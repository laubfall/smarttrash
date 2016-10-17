package de.ludwig.smt.jodd;

/**
 * Information about the current callstack of proxied services. Used to group calls to those services by an ID.
 * 
 * @author Daniel
 *
 */
public class CallStackContext
{
	private Integer callStackCnt = 0;

	private final String callStackName = Thread.currentThread().getName() + "-" + System.currentTimeMillis();

	public final void incrementCallCnt()
	{
		callStackCnt++;
	}

	public final void decrementCallCnt()
	{
		callStackCnt--;
	}

	public Integer getCallStackCnt()
	{
		return callStackCnt;
	}

	public String getCallStackName()
	{
		return callStackName;
	}
}