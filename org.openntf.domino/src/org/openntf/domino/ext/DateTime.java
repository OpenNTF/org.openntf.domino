/**
 * 
 */
package org.openntf.domino.ext;

import java.util.Calendar;

/**
 * @author nfreeman
 * 
 */
public interface DateTime {
	/**
	 * Compares current date with another and returns boolean of whether current date is after parameter.
	 * 
	 * @param comparDate
	 *            DateTime to compare to current date
	 * @return boolean, whether or not current date is after the parameter
	 */
	public boolean isAfter(DateTime comparDate);

	/**
	 * Compares current date with another and returns boolean of whether current date is before parameter.
	 * 
	 * @param comparDate
	 *            DateTime to compare to current date
	 * @return boolean, whether or not current date is before the parameter
	 */
	public boolean isBefore(DateTime comparDate);

	/**
	 * @return Java Calendar object, same as used internally by org.openntf.domino.DateTime class
	 */
	public Calendar toJavaCal();
}
