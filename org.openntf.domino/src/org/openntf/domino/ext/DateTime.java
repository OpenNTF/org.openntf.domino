/**
 * 
 */
package org.openntf.domino.ext;

import com.ibm.icu.util.Calendar;

/**
 * @author nfreeman
 * 
 */
public interface DateTime extends Comparable<org.openntf.domino.DateTime> {

	/**
	 * Compares current date with another and returns boolean of whether they are the same.
	 * 
	 * @param comparDate
	 *            DateTime to compare to current date
	 * @return boolean, whether or not the two dates are the same
	 */
	public boolean equals(final org.openntf.domino.DateTime compareDate);

	/**
	 * @param comparDate
	 *            DateTime to compare to the current DateTime
	 * @return boolean true if time is the same, including millisecond
	 */
	public boolean equalsIgnoreDate(final org.openntf.domino.DateTime compareDate);

	/**
	 * @param comparDate
	 *            DateTime to compare to the current DateTime
	 * @return boolean true if date is the same
	 */
	public boolean equalsIgnoreTime(final org.openntf.domino.DateTime compareDate);

	/**
	 * Compares current date with another and returns boolean of whether current date is after parameter.
	 * 
	 * @param comparDate
	 *            DateTime to compare to current date
	 * @return boolean, whether or not current date is after the parameter
	 */
	public boolean isAfter(final org.openntf.domino.DateTime compareDate);

	/**
	 * 
	 * @return whether the DateTime is a date-only value (e.g. [1/1/2013])
	 */
	public boolean isAnyTime();

	/*
	 * 
	 * @return whether the DateTime is a time-only value (e.g. [1:00 PM])
	 */
	public boolean isAnyDate();

	/**
	 * Compares current date with another and returns boolean of whether current date is before parameter.
	 * 
	 * @param comparDate
	 *            DateTime to compare to current date
	 * @return boolean, whether or not current date is before the parameter
	 */
	public boolean isBefore(final org.openntf.domino.DateTime compareDate);

	/**
	 * @return Java Calendar object, same as used internally by org.openntf.domino.DateTime class
	 */
	public Calendar toJavaCal();

	public void setLocalTime(Calendar calendar);

}
