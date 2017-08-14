package smt.req.frontend.tec;

import java.io.Serializable;

public class MenuEntry implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3787707114644482316L;

	private String url;
	
	private String i18nKey;

	public MenuEntry(String url, String i18nKey) {
		super();
		this.url = url;
		this.i18nKey = i18nKey;
	}

	public String getUrl()
	{
		return url;
	}

	public String getI18nKey()
	{
		return i18nKey;
	}
}
