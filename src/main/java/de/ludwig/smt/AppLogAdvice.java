package de.ludwig.smt;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jodd.proxetta.ProxyAdvice;
import jodd.proxetta.ProxyTarget;

/**
 * Advice that logs all actions that are requirements.
 * @author daniel
 *
 */
public class AppLogAdvice implements ProxyAdvice {

	private Map<Class<?>, Logger> loggers = new HashMap<>();
	
	@Override
	public Object execute() throws Exception {
		final Logger log = nullSafeGetLogger(ProxyTarget.targetClass());
		final String methodName = ProxyTarget.targetMethodName();
		
		try {
			Object result = ProxyTarget.invoke();
			log.info(methodName + " executed"); // TODO log parameters and result.
			return result;
		} catch (Exception e){
			log.error("execution of requirement method " + methodName + " failed", e);
			throw e;
		}
	}

	private Logger nullSafeGetLogger(Class<?> classToLog){
		if(loggers.containsKey(classToLog) == false){
			loggers.put(classToLog, LoggerFactory.getLogger(classToLog));
		}
		return loggers.get(classToLog);
	}
}
