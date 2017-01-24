package de.ludwig.jodd.proxetta;

/**
 * Information about the current callstack of proxied services. Used to group calls to those services by an ID.
 * 
 * @author Daniel
 *
 */
public class CallStackContext
{
	/**
	 * Count of method calls in the current call stack. This is equivalent to the count of logged methods by
	 * {@link AppLogAdvice}.
	 */
	private Integer callStackCnt = 0;

	// TODO time millis are not enough in case of methods that runs "0" millis. In such cases we cannot decide between
	// different callstacks based on the callstack name because it is equals.
	private final String callStackName = Thread.currentThread().getName() + "-" + System.currentTimeMillis();

	private Long startedAt;

	private Long duration;

	/**
	 * This Threadlocal contains the logging context of the current Stack.
	 */
	public static final ThreadLocal<CallStackContext> callStackCtx = new ThreadLocal<>();

	/**
	 * Increment current callstack count and set the field {@link #startedAt} when this callstack is new.
	 */
	public final void incrementCallCnt()
	{
		if (callStackCnt == 0) {
			startedAt = System.currentTimeMillis();
		}

		callStackCnt++;
	}

	public final void decrementCallCnt()
	{
		callStackCnt--;

		if (callStackCnt == 0) {
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