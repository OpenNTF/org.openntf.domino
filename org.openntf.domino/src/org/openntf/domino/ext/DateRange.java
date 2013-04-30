/**
 * 
 */
package org.openntf.domino.ext;

import java.util.Date;

/**
 * @author jgallagher
 * 
 */
public interface DateRange {
	public boolean contains(org.openntf.domino.DateTime dt);

	public boolean contains(Date date);
}
