package smt.app.frontend;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.thymeleaf.messageresolver.StandardMessageResolver;
import org.thymeleaf.templateresource.ClassLoaderTemplateResource;

/**
 * A thymeleaf message resolver that works without beeing inside the thymeleaf template process. This not mentioned to
 * be used as a messageresolver for the thymeleaf template engine! Use this in your code.
 * 
 * Use this class if you have to resolve messages that are part of i18n property files that follows the thymeleaf
 * template name scheme.
 * 
 * @author Daniel
 *
 */
public class StandaloneStandardMessageResolver extends StandardMessageResolver
{
	/**
	 * Name of the thymeleafe template.
	 */
	private String template;

	/**
	 * I18n Cache for the chosen template.
	 */
	private Map<Locale, Map<String, String>> translations = new HashMap<>();

	public StandaloneStandardMessageResolver(String template) {
		super();
		this.template = template;
	}

	/**
	 * 
	 * @param key
	 * @param locale
	 * @return
	 */
	public String resolveMessage(String key, Locale locale)
	{
		final Map<String, String> resolveMessagesForTemplate = translations(locale);
		return resolveMessagesForTemplate.get(key);
	}

	private Map<String, String> translations(Locale locale)
	{
		if (translations.containsKey(locale) == false) {
			ClassLoaderTemplateResource cltr = new ClassLoaderTemplateResource(getClass().getClassLoader(),
					"templates/" + template, "UTF-8");
			Map<String, String> resolveMessagesForTemplate = resolveMessagesForTemplate(template, cltr, locale);
			translations.put(locale, resolveMessagesForTemplate);
		}

		return translations.get(locale);
	}
}
