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

	private Long startedAt;
	
	private Long duration;
	
	public final void incrementCallCnt()
	{
		if(callStackCnt == 0){
			startedAt = System.currentTimeMillis();
		}
		
		callStackCnt++;
	}

	public final void decrementCallCnt()
	{
		callStackCnt--;
		
		if(callStackCnt == 0) {
			duration = System.currentTimeMillis() - startedAt;
		}
	}

	public Integer getCallStackCnt()
	{
		return callStackCnt;
	}

	public String getCallStackName()
	{
		return callStackName;
	}

	public Long getDuration()
	{
		return duration;
	}
}