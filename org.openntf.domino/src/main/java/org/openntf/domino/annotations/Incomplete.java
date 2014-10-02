/**
 * 
 */
package org.openntf.domino.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author nfreeman
 * 
 *         Indicates that a class is not yet done, and therefore should not be used
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface Incomplete {

}
