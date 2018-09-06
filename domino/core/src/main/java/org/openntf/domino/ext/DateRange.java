/**
 *
 */
package org.openntf.domino.ext;

import java.util.Date;
import java.util.List;

/**
 * OpenNTF extensions to DateRange class
 *
 * @author jgallagher
 *
 *
 */
public interface DateRange {

	/**
	 * Checks whether a DateRange contains a specific date/time
	 *
	 * @param dt
	 *            DateTime to check within the range
	 * @return true when the DateTime is within the range
	 * @since org.openntf.domino 2.5.0
	 */
	public boolean contains(final org.openntf.domino.DateTime dt);

	/**
	 * Checks whether a DateRange contains a specific Java date/time
	 *
	 * @param dt
	 *            Date to check within the range
	 * @return true when the Java Date is within the range
	 * @since org.openntf.domino 2.5.0
	 */
	public boolean contains(final Date date);

	/**
	 * Creates a copy of this object.
	 *
	 * @return DateRange with the same StartDateTime and EndDateTime.
	 */
	public org.openntf.domino.DateRange clone();

	/**
	 * Returns a list of <code>java.util.Date</code> instances from {@link org.openntf.domino.DateRange#getStartDateTime() StartDateTime} to
	 * {@link org.openntf.domino.DateRange#getEndDateTime() EndDateTime}. The <code>StartDateTime</code> and <code>EndDateTime</code> are
	 * included in the list.
	 *
	 * @return List of all days between <code>StartDateTime</code> and <code>EndDateTime</code> (including)
	 */
	public List<Date> getDays();
}
