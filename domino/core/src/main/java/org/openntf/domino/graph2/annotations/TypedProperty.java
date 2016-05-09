package org.openntf.domino.graph2.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.openntf.domino.utils.TypeUtils;

/**
 * Property annotations are for getter and setters to manipulate the property value of an Element. TypedProperty annotations are used to
 * enforce type coercion on the results, provide for derived dynamic properties and allow for properties that must be specifically requested
 * to return in services.
 *
 * @author Nathan T. Freeman
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TypedProperty {
	public String value();

	/**
	 * The converter used for the type casting.
	 *
	 * @return the converter used to enforce the getter and setter types
	 */
	public Class<?> converter() default TypeUtils.class;

	public boolean derived() default false;

	public boolean specified() default false;
}
