package smt.app.jodd.proxetta;

import java.lang.reflect.Modifier;

import jodd.proxetta.MethodInfo;
import jodd.proxetta.pointcuts.ProxyPointcutSupport;
import smt.app.rdd.Requirement;

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
		if (ignoredMethod(methodInfo)) {
			return false;
		}

		boolean apply = hasAnnotation(methodInfo.getClassInfo(), Requirement.class)
				|| hasAnnotation(methodInfo, Requirement.class);

		return apply;

	}

	/**
	 * Some methods are ignored by default. Thos methods are: abstract methods, volatile methods and methods provided by
	 * java.lang.Object.
	 * 
	 * @param methodInfo method to check.
	 * @return true if a method is ignored.
	 */
	public boolean ignoredMethod(final MethodInfo methodInfo)
	{
		return isRootMethod(methodInfo) || Modifier.isAbstract(methodInfo.getAccessFlags())
				|| Modifier.isVolatile(methodInfo.getAccessFlags());
	}
}
