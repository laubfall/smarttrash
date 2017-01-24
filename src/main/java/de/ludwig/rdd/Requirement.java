package de.ludwig.rdd;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marker Annotation to show that the type or method is an application requirement.
 * @author daniel
 *
 */
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface Requirement {
	public RequirementMapping[] mappings() default {};
}
