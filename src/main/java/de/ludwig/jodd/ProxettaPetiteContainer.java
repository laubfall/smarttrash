package de.ludwig.jodd;

import jodd.petite.BeanDefinition;
import jodd.petite.PetiteContainer;
import jodd.petite.PetiteUtil;
import jodd.petite.WiringMode;
import jodd.petite.scope.Scope;
import jodd.proxetta.ProxyAspect;
import jodd.proxetta.impl.ProxyProxetta;
import jodd.proxetta.impl.ProxyProxettaBuilder;
import jodd.proxetta.pointcuts.ProxyPointcutSupport;

/**
 * Petite Container with Proxetta Support.
 * 
 * @author daniel
 *
 */
public class ProxettaPetiteContainer extends PetiteContainer
{

	private ProxyProxetta proxetta;

	public ProxettaPetiteContainer() {
		initProxetta();
	}

	@Override
	public BeanDefinition registerPetiteBean(Class type, String name, Class<? extends Scope> arg2, WiringMode arg3,
			boolean arg4)
	{

		if (name == null) {
			name = PetiteUtil.resolveBeanName(type, true);
		}

		ProxyProxettaBuilder builder = proxetta.builder();
		builder.setTarget(type);
		type = builder.define();

		return super.registerPetiteBean(type, name, arg2, arg3, arg4);
	}

	@Override
	public <T> T getBean(Class<T> type)
	{
		// You cannot retrieve Beans by type that are proxied with Proxetta, so we forward the call to the method that
		// expects the Class-name
		return super.getBean(type.getCanonicalName());
	}

	private final void initProxetta()
	{
		ProxyAspect aspect = new ProxyAspect(AppLogAdvice.class, appLogAdvicePointcut());
		proxetta = ProxyProxetta.withAspects(aspect);

		// TODO make this configurable
		proxetta.setDebugFolder("./target");
	}

	public ProxyPointcutSupport appLogAdvicePointcut()
	{
		return new AppLogPointcut();
	}
}
