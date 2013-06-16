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
	public boolean contains(final org.openntf.domino.DateTime dt);

	public boolean contains(final Date date);
}
