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
		boolean apply = hasAnnotation(methodInfo.getClassInfo(), Requirement.class)
				&& hasAnnotation(methodInfo, Requirement.class);

		System.out.println(methodInfo.getClassInfo().getClassname() + "." + methodInfo.getMethodName() + ":" + apply
				+ " topLevelMethod: " + methodInfo.isTopLevelMethod() + " " + methodInfo.getRawSignature());
		return apply;

	}

}
