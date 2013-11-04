/**
 * 
 */
package org.openntf.domino.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author rpraml
 * 
 *         Indicates that a class is available in notes 9 only, and therefore should not be used in Notes 8.5.x
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD, ElementType.TYPE })
public @interface Notes9only {

}
