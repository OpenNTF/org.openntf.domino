/**
 * 
 */
package org.openntf.domino.ext;

import com.ibm.icu.util.Calendar;

/**
 * @author nfreeman
 * 
 *         OpenNTF extensions to DateTime class
 * 
 */
public interface DateTime extends Comparable<org.openntf.domino.DateTime> {

	/**
	 * Compares current date with another and returns boolean of whether they are the same.
	 * 
	 * @param comparDate
	 *            DateTime to compare to current date
	 * @return boolean, whether or not the two dates are the same
	 * @since org.openntf.domino 1.0.0
	 */
	public boolean equals(final org.openntf.domino.DateTime compareDate);

	/**
	 * Compares two DateTimes to see if they are the same time (including millisecond), ignoring date element
	 * 
	 * @param comparDate
	 *            DateTime to compare to the current DateTime
	 * @return boolean true if time is the same
	 * @since org.openntf.domino 1.0.0
	 */
	public boolean equalsIgnoreDate(final org.openntf.domino.DateTime compareDate);

	/**
	 * Compares two DateTimes to see if they are the same date, ignoring the time element
	 * 
	 * @param comparDate
	 *            DateTime to compare to the current DateTime
	 * @return boolean true if date is the same
	 * @since org.openntf.domino 1.0.0
	 */
	public boolean equalsIgnoreTime(final org.openntf.domino.DateTime compareDate);

	/**
	 * Compares current date with another and returns boolean of whether current date is after parameter.
	 * 
	 * @param comparDate
	 *            DateTime to compare to current date
	 * @return boolean, whether or not current date is after the parameter
	 * @since org.openntf.domino 1.0.0
	 */
	public boolean isAfter(final org.openntf.domino.DateTime compareDate);

	/**
	 * Checks whether the DateTime is defined as any time, so just a specific Date
	 * 
	 * @return boolean, whether the DateTime is a date-only value (e.g. [1/1/2013])
	 * @since org.openntf.domino 1.0.0
	 */
	public boolean isAnyTime();

	/**
	 * Checks whether the DateTime is defined as any date, so just a specific Time
	 * 
	 * @return boolean, whether the DateTime is a time-only value (e.g. [1:00 PM])
	 * @since org.openntf.domino 1.0.0
	 */
	public boolean isAnyDate();

	/**
	 * Compares current date with another and returns boolean of whether current date is before parameter.
	 * 
	 * @param comparDate
	 *            DateTime to compare to current date
	 * @return boolean, whether or not current date is before the parameter
	 * @since org.openntf.domino 1.0.0
	 */
	public boolean isBefore(final org.openntf.domino.DateTime compareDate);

	/**
	 * Returns a Java Calendar object for the DateTime object, same as used internally by org.openntf.domino.DateTime class
	 * 
	 * @return Java Calendar object representing the DateTime object
	 * @since org.openntf.domino 1.0.0
	 */
	public Calendar toJavaCal();

	/**
	 * Sets the date and time to the value of a specific Java Calendar instance
	 * 
	 * @param calendar
	 *            Java calendar instance with relevant date and time
	 * @since org.openntf.domino 1.0.0
	 */
	public void setLocalTime(Calendar calendar);

}
