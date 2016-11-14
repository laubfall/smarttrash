package de.ludwig.jodd;

/**
 * Jodd Props Properties made accessible.
 * 
 * @author daniel
 *
 */
public enum PropsElasticsearchProps {
	ELASTICSEARCH("elasticsearch",true), CONFIG(ELASTICSEARCH, "config"), INDEX(ELASTICSEARCH, "index");
	private String propertyName;

	private boolean section = false;
	
	private PropsElasticsearchProps(String propertyName) {
		this.propertyName = propertyName;
	}
	
	private PropsElasticsearchProps(String propertyName, boolean section) {
		this.propertyName = propertyName;
		this.section = section;
	}

	private PropsElasticsearchProps(PropsElasticsearchProps prop, String propertyName) {
		this.propertyName = prop.propertyName + "." + propertyName;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public boolean isSection() {
		return section;
	}
}
