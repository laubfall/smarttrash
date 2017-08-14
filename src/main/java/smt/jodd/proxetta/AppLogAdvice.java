package smt.jodd.proxetta;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jodd.proxetta.ProxyAdvice;
import jodd.proxetta.ProxyTarget;
import jodd.util.StringUtil;
import smt.rdd.Requirement;
import smt.rdd.RequirementMapping;

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

	@Override
	public Object execute() throws Exception
	{
		final Logger log = nullSafeGetLogger(ProxyTarget.targetClass());
		String methodName = ProxyTarget.targetMethodName();
		Class<?> targetClass = ProxyTarget.targetClass();
		methodName = mappReqMethod(targetClass.getAnnotation(Requirement.class), methodName);

		ThreadLocal<CallStackContext> callStackCtx = CallStackContext.callStackCtx;
		if (callStackCtx.get() == null) {
			callStackCtx.set(new CallStackContext());
		}

		CallStackContext callStack = callStackCtx.get();

		try {
			callStack.incrementCallCnt();
			final String targetClassName = ProxyTarget.targetClass().getSimpleName();
			final int argCnt = ProxyTarget.argumentsCount();
			final Object[] args = ProxyTarget.createArgumentsArray();
			log.info(callStackName() + "execute " + targetClassName + "." + methodName + " with params: "
					+ paramLog(argCnt, args));
			Object result = ProxyTarget.invoke();

			// TODO maybe create some annotation that controls the logging of the method return value.
			log.info(callStackName() + targetClassName + "." + methodName + " result: " + result);
			return result;
		} catch (Exception e) {
			log.error(callStackName() + "execution of requirement method " + methodName + " failed", e);
			throw e;
		} finally {
			callStack.decrementCallCnt();
			if (callStack.getCallStackCnt() == 0) {
				log.info(callStackName() + " execution duration (ms): " + callStack.getDuration());
				// TODO why is the following code not active
				// reset the callstack context because we reached the start of the logged requirement stack
				// callStackCtx.set(new CallStackContext());
			}
		}
	}

	private String mappReqMethod(Requirement req, String targetMethodName)
	{
		if(req == null || req.mappings().length == 0) {
			return targetMethodName;
		}
		
		for(RequirementMapping rm : req.mappings()) {
			if(rm.target().equals(targetMethodName)) {
				return rm.name();
			}
		}
		
		return targetMethodName;
	}

	private String callStackName()
	{
		final ThreadLocal<CallStackContext> callStackCtx = CallStackContext.callStackCtx;
		String format = String.format("%1$" + (callStackCtx.get().getCallStackCnt() + 1) + "s", " ");
		return callStackCtx.get().getCallStackName() + ":" + format;
	}

	private String paramLog(int argCnt, Object[] args)
	{
		if (argCnt == 0) {
			return "no params";
		}

		final String result = "\n" + StringUtil.join(args, "\n");
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
