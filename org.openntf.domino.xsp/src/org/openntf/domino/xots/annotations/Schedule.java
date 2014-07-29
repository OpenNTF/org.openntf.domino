package org.openntf.domino.xots.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Schedule {
	public static final int WEEKDAYS = 10;
	public static final int WEEKENDS = 11;
	public static final int ALL = -1;

	TimeUnit timeunit();

	int frequency();

	int[] weekdays() default { ALL };

	int starthour() default 0;

	int endhour() default 23;

}
