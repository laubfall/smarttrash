package de.ludwig.jodd;

import de.ludwig.rdd.Requirement;
import jodd.proxetta.MethodInfo;
import jodd.proxetta.pointcuts.ProxyPointcutSupport;

/**
 * Pointcut for logging of requirements. Match all types and methods with the {@link Requirement} annotation.
 * 
 * @author daniel
 *
 */
public class AppLogPointcut extends ProxyPointcutSupport
{

	@Override
	public boolean apply(MethodInfo methodInfo)
	{
		// do not log any methods of class Object
		if(isRootMethod(methodInfo)) {
			return false;
		}
		
		boolean apply = hasAnnotation(methodInfo.getClassInfo(), Requirement.class)
				|| hasAnnotation(methodInfo, Requirement.class);

		return apply;

	}

}
