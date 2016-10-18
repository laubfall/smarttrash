package de.ludwig.smt.jodd;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jodd.proxetta.ProxyAdvice;
import jodd.proxetta.ProxyTarget;
import jodd.util.StringUtil;

/**
 * Advice that logs all actions that are requirements.
 * 
 * @author daniel
 *
 */
public class AppLogAdvice implements ProxyAdvice
{

	/**
	 * Cache for loggers.
	 */
	private Map<Class<?>, Logger> loggers = new HashMap<>();

	private static final ThreadLocal<CallStackContext> callStackCtx = new ThreadLocal<>();

	@Override
	public Object execute() throws Exception
	{
		final Logger log = nullSafeGetLogger(ProxyTarget.targetClass());
		final String methodName = ProxyTarget.targetMethodName();

		if(callStackCtx.get() == null) {
			callStackCtx.set(new CallStackContext());
		}
		
		CallStackContext callStack = callStackCtx.get();
		
		try {
			callStack.incrementCallCnt();
			Object result = ProxyTarget.invoke();
			final int argCnt = ProxyTarget.argumentsCount();
			final Object[] args = ProxyTarget.createArgumentsArray();
			log.info(callStackName() + methodName + " executed with params: " + paramLog(argCnt, args));
			
			// TODO maybe create some annotation that controls the logging of the method return value. 
			log.info(callStackName() + "result: " + result);
			return result;
		} catch (Exception e) {
			log.error(callStackName() + "execution of requirement method " + methodName + " failed", e);
			throw e;
		} finally {
			callStack.decrementCallCnt();
			if(callStack.getCallStackCnt() == 0) {
				log.info(callStackName() + " execution duration (ms): " + callStack.getDuration());
				// reset the callstack context because we reached the start of the logged requirement stack
				callStackCtx.set(new CallStackContext());
			}
		}
	}

	private String callStackName()
	{
		char[] copyOf = Arrays.copyOf(new char[]{' '}, callStackCtx.get().getCallStackCnt() + 1);
		return callStackCtx.get().getCallStackName() + ":" + new String(copyOf);
	}

	private String paramLog(int argCnt, Object[] args)
	{
		if (argCnt == 0) {
			return "no params";
		}

		final String result = StringUtil.join(args, "\n");
		return result;
	}

	private Logger nullSafeGetLogger(Class<?> classToLog)
	{
		if (loggers.containsKey(classToLog) == false) {
			loggers.put(classToLog, LoggerFactory.getLogger(classToLog));
		}
		return loggers.get(classToLog);
	}
}
