package smt.jodd;

/**
 * Jodd Props Properties made accessible.
 * 
 * @author daniel
 *
 */
public enum PropsElasticsearchProps
{
	ELASTICSEARCH("elasticsearch", true), //
	INDEX(ELASTICSEARCH, "index"), //
	NODE(ELASTICSEARCH, "node", true),
	NODE_PATH(NODE, "path", true),
	NODE_DATA(NODE_PATH,"data");
	;
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
	
	private PropsElasticsearchProps(PropsElasticsearchProps prop, String propertyName, boolean section) {
		this.propertyName = prop.propertyName + "." + propertyName;
		this.section = section;
	}

	public String getPropertyName()
	{
		return propertyName;
	}

	public boolean isSection()
	{
		return section;
	}
}
