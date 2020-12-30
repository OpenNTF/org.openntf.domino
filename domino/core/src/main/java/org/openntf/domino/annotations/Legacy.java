/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Legacy interface denotes methods that often represent older and less-efficient methods of programming.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE })
@SuppressWarnings("nls")
public @interface Legacy {

	/**
	 * Value.
	 *
	 * @return the string[]
	 */
	String[]value() default { "Generics are best practice" };

	/** The Constant INTERFACES_WARNING. */
	public static final String INTERFACES_WARNING = "Methods should return interfaces instead of classes";

	/** The Constant GENERICS_WARNING. */
	public static final String GENERICS_WARNING = "Use generics";

	/** The Constant DATETIME_WARNING. */
	public static final String DATETIME_WARNING = "Considering switching to Java Dates or Calendars whereever possible";

	/** The Constant ITERATION_WARNING. */
	public static final String ITERATION_WARNING = "Consider using for(member : collection) syntax for cleaner iteration";

	/** The Constant IBM_DEP_WARNING. */
	public static final String IBM_DEP_WARNING = "IBM help documentation indicates that this is depreciated and should not be used.";
}
