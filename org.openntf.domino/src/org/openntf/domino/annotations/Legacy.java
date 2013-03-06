package org.openntf.domino.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE })
public @interface Legacy {

	String[] value() default { "Generics are best practice" };

	public static final String INTERFACES_WARNING = "Methods should return interfaces instead of classes";
	public static final String GENERICS_WARNING = "Use generics";
}
