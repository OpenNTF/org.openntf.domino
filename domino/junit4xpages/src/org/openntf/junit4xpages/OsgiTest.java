package org.openntf.junit4xpages;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ java.lang.annotation.ElementType.TYPE })
public @interface OsgiTest {
	String value() default "";
}
