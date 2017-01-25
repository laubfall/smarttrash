package de.ludwig.jodd.proxetta;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import de.ludwig.jodd.proxetta.AppLogPointcut;
import de.ludwig.jodd.proxetta.ProxettaPetiteContainer;
import de.ludwig.jodd.proxetta.samplebeans.AppLogBean1_1;
import de.ludwig.jodd.proxetta.samplebeans.AppLogBean1_2;
import de.ludwig.jodd.proxetta.samplebeans.AppLogBean2_1;
import de.ludwig.jodd.proxetta.samplebeans.AppLogBean2_2;
import de.ludwig.jodd.proxetta.samplebeans.AppLogBean3_1;
import jodd.petite.config.AutomagicPetiteConfigurator;
import jodd.proxetta.MethodInfo;
import jodd.proxetta.pointcuts.ProxyPointcutSupport;

/**
 * Tests if the {@link AppLogPointcut} hits the correct methods.
 * 
 * @author Daniel
 *
 */
public class AppLogPointcutTest
{
	@Test
	public void samplebeansOne()
	{
		final PointcutResultList expectedResults = new PointcutResultList();
		expectedResults.addResult(AppLogBean1_1.class.getSimpleName(), false, "noRequirementMethod", "test01", "test02", "test03", "hashCode",
				"equals", "toString");
		expectedResults.addResult(AppLogBean1_2.class.getSimpleName(), true, "test03", "test02", "test01", "noRequirementMethod")
				.addResult(AppLogBean1_2.class.getSimpleName(), false, "hashCode", "equals", "toString");
		doAssertions(expectedResults, "de.ludwig.jodd.proxetta.samplebeans.AppLogBean1*");
	}

	@Test
	public void samplebeansTwo()
	{
		final PointcutResultList expectedResults = new PointcutResultList();
		expectedResults.addResult(AppLogBean2_1.class.getSimpleName(), false, "test", "hashCode", "equals", "toString");
		expectedResults.addResult(AppLogBean2_2.class.getSimpleName(), "test", true)
				.addResult(AppLogBean2_2.class.getSimpleName(), false, "hashCode", "equals", "toString");
		doAssertions(expectedResults, "de.ludwig.jodd.proxetta.samplebeans.AppLogBean2*");
	}

	@Test
	public void samplebeansThree()
	{
		final PointcutResultList expectedResults = new PointcutResultList();
		expectedResults.addResult(AppLogBean3_1.class.getSimpleName(), true, "test")
				.addResult(AppLogBean3_1.class.getSimpleName(), false, "hashCode", "equals", "toString");
		doAssertions(expectedResults, "de.ludwig.jodd.proxetta.samplebeans.AppLogBean3*");
	}

	private void doAssertions(PointcutResultList expectedResults, String includedEntriesMatcher)
	{
		final AppLogPointcut spy = Mockito.spy(new AppLogPointcut());

		Mockito.doAnswer(invocation -> {
			final MethodInfo methodInfo = (MethodInfo) invocation.getArguments()[0];

			Boolean isPointcut = (Boolean) invocation.callRealMethod();
			if (spy.ignoredMethod(methodInfo) == false) {
				List<PointcutResult> expectedResult = expectedResults.expectedResult(
						methodInfo.getClassInfo().getClassname(), methodInfo.getMethodName(), isPointcut);

				System.out.println(methodInfo.getClassInfo().getClassname() //
						+ "." + methodInfo.getMethodName() + ":\n" //
						+ " is Pointcut: " + isPointcut //
						+ "\n topLevelMethod: " + methodInfo.isTopLevelMethod() //
						+ "\n rootMethod: " + spy.isRootMethod(methodInfo) //
						+ "\n specialMethod: " + spy.isSpecialMethod(methodInfo) //
						+ "\n declaration: " + methodInfo.getDeclaration() //
						+ "\n declaredClassName: " + methodInfo.getDeclaredClassName() //
						+ "\n accessFlags: " + Modifier.toString(methodInfo.getAccessFlags()) + "\n");

				Assert.assertEquals("expected " + methodInfo.getClassInfo().getClassname() + "."
						+ methodInfo.getMethodName() + " to be logged", 1, expectedResult.size());
			}

			return isPointcut;
		}).when(spy).apply(Mockito.any(MethodInfo.class));

		// provide the spy as a pointcut
		final ProxettaPetiteContainer container = new ProxettaPetiteContainer() {
			@Override
			public ProxyPointcutSupport appLogAdvicePointcut()
			{
				return spy;
			}
		};

		final AutomagicPetiteConfigurator petiteConfigurator = new AutomagicPetiteConfigurator();
		petiteConfigurator.setIncludedEntries(includedEntriesMatcher);
		petiteConfigurator.configure(container);
	}

	class PointcutResult
	{
		String classname;
		String methodname;
		boolean pointcutApplyResult;

		@Override
		public String toString()
		{
			return "PointcutResult [classname=" + classname + ", methodname=" + methodname + ", pointcutApplyResult="
					+ pointcutApplyResult + "]";
		}
	}

	class PointcutResultList extends ArrayList<PointcutResult>
	{
		public PointcutResultList addResult(String classname, String methodname, boolean pointcutApplyResult)
		{
			PointcutResult pointcutResult = new PointcutResult();
			pointcutResult.classname = classname;
			pointcutResult.methodname = methodname;
			pointcutResult.pointcutApplyResult = pointcutApplyResult;
			add(pointcutResult);
			return this;
		}

		public PointcutResultList addResult(String classname, boolean pointcutApplyResult, String... methodnames)
		{
			for (String mn : methodnames) {
				addResult(classname, mn, pointcutApplyResult);
			}
			return this;
		}

		public List<PointcutResult> expectedResult(String classname, String methodname, boolean pointcutApplyResult)
		{
			List<PointcutResult> collect = stream().parallel()
					.filter(predicate -> predicate.classname.equals(classname))
					.filter(predicate -> predicate.methodname.equals(methodname))
					.filter(predicate -> predicate.pointcutApplyResult == pointcutApplyResult)
					.collect(Collectors.toList());
			return collect;
		}
	}
}
