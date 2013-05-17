/**
 * 
 */
package org.openntf.domino.ext;

import java.util.Date;

/**
 * @author withersp
 * 
 */
public interface Item {

	public <T> T getValues(Class<?> T);

	public Date getLastModifiedDate();
}
