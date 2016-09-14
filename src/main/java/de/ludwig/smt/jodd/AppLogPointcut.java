package de.ludwig.smt.jodd;

import de.ludwig.rdd.Requirement;
import jodd.proxetta.MethodInfo;
import jodd.proxetta.pointcuts.ProxyPointcutSupport;

/**
 * Pointcut for logging of requireqments. Match all types and methods with the {@link Requirement} annotation.
 * 
 * @author daniel
 *
 */
public class AppLogPointcut extends ProxyPointcutSupport {

	@Override
	public boolean apply(MethodInfo methodInfo) {
		return hasAnnotation(methodInfo.getClassInfo(), Requirement.class)||hasAnnotation(methodInfo, Requirement.class);
	}

}
