package de.ludwig.jodd;

/**
 * Jodd Config Properties for the flow config.
 * 
 * @author Daniel
 *
 */
public enum PropsFlowConfig
{

	PATH("flowconfig.path"), NAME("flowconfig.name");

	private String propName;

	private PropsFlowConfig(String propName) {
		this.propName = propName;
	}

	public String getPropName()
	{
		return propName;
	}

}
