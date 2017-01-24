package de.ludwig.jodd;

import java.util.ArrayList;

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
		expectedResults.addResult("AppLogBean1", "test01", false);
		expectedResults.addResult("AppLogBean2", "test01", true);

		
		
		Mockito.doAnswer(invocation -> {
			final MethodInfo object = (MethodInfo) invocation.getArguments()[0];
			Boolean callRealMethod = (Boolean) invocation.callRealMethod();
			
			boolean expectedResult = expectedResults.expectedResult(object.getClassInfo().getClassname(), object.getMethodName(), callRealMethod);
			Assert.assertTrue(expectedResult);
			
			return callRealMethod;
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
		// TODO only include the test petite beans.
		 petiteConfigurator.setIncludedEntries("de.ludwig.jodd*");
		petiteConfigurator.configure(container);

		Mockito.verify(spy, Mockito.times(expectedResults.size())).apply(Mockito.any(MethodInfo.class));
	}

	class PointcutResult
	{
		String classname;
		String methodname;
		boolean pointcutApplyResult;
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

		public boolean expectedResult(String classname, String methodname, boolean pointcutApplyResult)
		{
			long count = stream().parallel().filter(predicate -> predicate.classname.equals(classname))
					.filter(predicate -> predicate.methodname.equals(methodname))
					.filter(predicate -> predicate.pointcutApplyResult == pointcutApplyResult).count();
			return count == 1;
		}
	}
}
