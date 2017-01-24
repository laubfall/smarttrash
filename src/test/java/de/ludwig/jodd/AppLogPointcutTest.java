package de.ludwig.jodd;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import jodd.petite.config.AutomagicPetiteConfigurator;
import jodd.proxetta.MethodInfo;
import jodd.proxetta.pointcuts.ProxyPointcutSupport;

public class AppLogPointcutTest
{
	@Test
	public void aop()
	{
		final AppLogPointcut spy = Mockito.spy(new AppLogPointcut());

		final PointcutResultList expectedResults = new PointcutResultList();
		expectedResults.addResult("AppLogBean1", false, "test01", "hashCode", "equals", "toString");
		expectedResults.addResult("AppLogBean2", "test01", true).addResult("AppLogBean2", false,  "hashCode", "equals", "toString");

		Mockito.doAnswer(invocation -> {
			final MethodInfo methodInfo = (MethodInfo) invocation.getArguments()[0];
			Boolean isPointcut = (Boolean) invocation.callRealMethod();

			List<PointcutResult> expectedResult = expectedResults
					.expectedResult(methodInfo.getClassInfo().getClassname(), methodInfo.getMethodName(), isPointcut);

			System.out.println(methodInfo.getClassInfo().getClassname() + "." + methodInfo.getMethodName() + ":"
					+ isPointcut + "\n topLevelMethod: " + methodInfo.isTopLevelMethod() + "\n rootMethod: "
					+ spy.isRootMethod(methodInfo) + "\n specialMethod: " + spy.isSpecialMethod(methodInfo) + "\n");

			Assert.assertEquals("expected " + methodInfo.getClassInfo().getClassname() + "."
					+ methodInfo.getMethodName() + " to be logged", 1, expectedResult.size());

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
		petiteConfigurator.setIncludedEntries("de.ludwig.jodd*");
		petiteConfigurator.configure(container);

		Mockito.verify(spy, Mockito.times(expectedResults.size())).apply(Mockito.any(MethodInfo.class));
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
