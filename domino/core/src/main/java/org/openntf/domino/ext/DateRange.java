/**
 * 
 */
package org.openntf.domino.ext;

import java.util.Date;

/**
 * @author jgallagher
 * 
 *         OpenNTF extensions to DateRange class
 * 
 */
public interface DateRange {

	/**
	 * Checks whether a DateRange contains a specific date/time
	 * 
	 * @param dt
	 *            DateTime to check within the range
	 * @return boolean whether the DateTime is within the range
	 * @since org.openntf.domino 2.5.0
	 */
	public boolean contains(final org.openntf.domino.DateTime dt);

	/**
	 * Checks whether a DateRange contains a specific Java date/time
	 * 
	 * @param dt
	 *            Date to check within the range
	 * @return boolean whether the Java Date is within the range
	 * @since org.openntf.domino 2.5.0
	 */
	public boolean contains(final Date date);

	public org.openntf.domino.DateRange clone();
}
