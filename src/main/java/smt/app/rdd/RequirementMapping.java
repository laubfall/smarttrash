package smt.app.rdd;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

/**
 * Mapping information about technical method names and requirement method names.
 * 
 * @author Daniel
 *
 */
@Retention(RUNTIME)
public @interface RequirementMapping {
	public String target();
	public String name();
}
