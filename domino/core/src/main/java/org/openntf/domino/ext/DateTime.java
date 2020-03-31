/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 *
 */
package org.openntf.domino.ext;

import java.time.temporal.Temporal;

import com.ibm.icu.util.Calendar;

/**
 * OpenNTF extensions to DateTime class
 *
 * @author nfreeman
 *
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
	 * Compares current date with another and returns boolean of whether current date is after parameter, ignoring the date element
	 *
	 * @param comparDate
	 *            DateTime to compare to the current DateTime
	 * @return boolean, whether or not current date is after the parameter
	 * @since org.openntf.domino 2.0.1
	 */
	public boolean isAfterIgnoreDate(final org.openntf.domino.DateTime compareDate);

	/**
	 * Compares current date with another and returns boolean of whether current date is after parameter, ignoring the time element
	 *
	 * @param comparDate
	 *            DateTime to compare to the current DateTime
	 * @return boolean, whether or not current date is after the parameter
	 * @since org.openntf.domino 2.0.1
	 */
	public boolean isAfterIgnoreTime(final org.openntf.domino.DateTime compareDate);

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
	 * Compares current date with another and returns boolean of whether current date is before parameter, ignoring the date element
	 *
	 * @param comparDate
	 *            DateTime to compare to the current DateTime
	 * @return boolean, whether or not current date is before the parameter
	 * @since org.openntf.domino 2.0.1
	 */
	public boolean isBeforeIgnoreDate(final org.openntf.domino.DateTime compareDate);

	/**
	 * Compares current date with another and returns boolean of whether current date is before parameter, ignoring the time element
	 *
	 * @param comparDate
	 *            DateTime to compare to the current DateTime
	 * @return boolean, whether or not current date is before the parameter
	 * @since org.openntf.domino 2.0.1
	 */
	public boolean isBeforeIgnoreTime(final org.openntf.domino.DateTime compareDate);

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

	public org.openntf.domino.DateTime clone();

	public String toGMTISO();

	public Temporal toGMTDateTime();

	public void convertToGMT();

}
