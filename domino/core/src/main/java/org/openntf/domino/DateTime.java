/*
 * Copyright 2013
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package org.openntf.domino;

import java.util.Date;

import org.openntf.domino.types.Encapsulated;
import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.SessionDescendant;

/**
 * Represents a date and time.
 * <h3>Notable enhancements and changes</h3>
 * <ul>
 * <li>Use {@link org.openntf.domino.ext.DateTime#isAfter(DateTime)} and {@link org.openntf.domino.ext.DateTime#isBefore(DateTime)} to
 * determine whether this <code>DateTime</code> is before or after another <code>DateTime</code></li>
 * </ul>
 * <h3>Creation</h3>
 * <p>
 * To create a new <code>DateTime</code> object, use {@link Session#createDateTime(Date)}.
 * <h3>Usage</h3>
 * <p>
 * After creating a <code>DateTime</code> object with {@link Session#createDateTime(Date)}, you can reset the date and time with
 * {@link #setLocalTime(java.util.Calendar)} and {@link #setLocalDate(int, int, int)}. The <code>createDateTime</code> method takes a
 * <code>String</code>, <code>java.util.Date</code>, or <code>java.util.Calendar</code> parameter. One form of {@link #setLocalTime(String)}
 * takes a <code>String</code> parameter, another form takes a <code>java.util.Date parameter</code>, and a third form takes a
 * <code>java.util.Calendar</code> parameter. Other forms of {@link #setLocalTime(int, int, int, int) setLocalTime} and
 * {@link #setLocalDate(int, int, int) setLocalDate} take multiple int parameters.
 * </p>
 *
 * <p>
 * The String form of the date parameter is a date, followed by a space, followed by a time. You can specify a date without a time, and a
 * time without a date, but the time zone will not be set (see "Time zones" below).
 * </p>
 *
 * <p>
 * The following applies when setting a date and time from a <code>String</code> value:
 * </p>
 * <ul>
 * <li>The date and time components are interpreted according to the regional settings of the operating system if possible. For example, if
 * the regional setting for dates is M/d/yy, then "3/4/05" means 4 March 2005.</li>
 * <li>If the date or time cannot be interpreted using the regional setting, alternate settings are tried until one works. For example, if
 * the regional setting for dates is M/d/yy, then "13/4/05" means 13 April 2005 (using d/M/yy as the alternate setting) and "13/33/05" means
 * 13 May 2033 (using d/yy/M as the alternate setting).</li>
 * <li>If the date or time cannot be interpreted using any setting, the following exception is thrown: NOTES_ERR_INVALID_DATE (4045).</li>
 * </ul>
 * <p>
 * The <code>java.util.Calendar</code> class can be used to avoid dependencies on regional settings. For example, you can use
 * <code>getInstance</code> and one of the set methods to create the parameter for {@link Session#createDateTime(java.util.Calendar)}.
 * </p>
 * <h3>Time zones</h3>
 * <p>
 * When you create a new <code>DateTime</code> object, the time zone setting in Domino determines the {@link #getTimeZone() TimeZone}
 * property. For example, if the code runs on a computer where the Domino time zone is set to Eastern Standard Time, the
 * {@link #getTimeZone() TimeZone} property of any new <code>DateTime</code> object is automatically set to 5. The time zone setting also
 * affects the {@link #getGMTTime() GMTTime} property.
 * </p>
 * <p>
 * If you create a date without a time component or without a date component, the time zone is invalid and the {@link #getTimeZone()
 * TimeZone} property returns 0. In this case, both {@link #getLocalTime()} and {@link #getGMTTime()} return the same time value without a
 * time zone appended. If you apply {@link #convertToZone(int, boolean)}, the time zone remains invalid and {@link #getTimeZone() TimeZone}
 * remains 0; no error occurs.
 * </p>
 * <h3>Today, Tomorrow, and Yesterday</h3>
 * <p>
 * When you create a new <code>DateTime</code> object using an expression such as "Today," "Tommorow," or "Yesterday," the value of the date
 * is determined using the current date setting in Domino. These expressions have no effect on the time component of a <code>DateTime</code>
 * object. To set a <code>DateTime</code> to the current date and time, specify the time after "Today" or "Yesterday" (for example, "Today
 * 12:00:00 AM") or use {@link #setNow()}.
 * </p>
 *
 * <h3>Access</h3>
 * <p>
 * To access a date-time value stored in an item in a Domino document, use {@link Item#getDateTimeValue()}.
 * </p>
 *
 * <p>
 * You can get the operating system date and time separators, and time zone settings through the {@link International} class.
 * </p>
 */
public interface DateTime extends Base<lotus.domino.DateTime>, lotus.domino.DateTime, org.openntf.formula.DateTime,
		org.openntf.domino.ext.DateTime, Encapsulated, SessionDescendant, Cloneable {

	public static class Schema extends FactorySchema<DateTime, lotus.domino.DateTime, Session> {
		@Override
		public Class<DateTime> typeClass() {
			return DateTime.class;
		}

		@Override
		public Class<lotus.domino.DateTime> delegateClass() {
			return lotus.domino.DateTime.class;
		}

		@Override
		public Class<Session> parentClass() {
			return Session.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/**
	 * Increments a date-time by the number of days you specify.
	 * <p>
	 * If the DateTime object consists of only a time component, this method has no effect.
	 * </p>
	 * <p>
	 * The number of days by which you increment a DateTime object can cause the month to be incremented.
	 * </p>
	 *
	 * @param n
	 *            The number of days by which to increment the date-time. Can be positive or negative.
	 */
	@Override
	public void adjustDay(final int n);

	/**
	 * Increments a date-time by the number of days you specify.
	 * <p>
	 * If the DateTime object consists of only a time component, this method has no effect.
	 * </p>
	 * <p>
	 * The number of days by which you increment a DateTime object can cause the month to be incremented.
	 * </p>
	 *
	 * @param n
	 *            The number of days by which to increment the date-time. Can be positive or negative.
	 * @param preserveLocalTime
	 *            Affects adjustments that cross a daylight-saving time boundary. Specify true to increment or decrement the GMT time by one
	 *            hour such that a 24-hour adjustment yields the same local time in the new day. If this parameter is false or omitted, GMT
	 *            time remains as adjusted and local time gains or loses an hour.
	 */
	@Override
	public void adjustDay(final int n, final boolean preserveLocalTime);

	/**
	 * Increments a date-time by the number of hours you specify.
	 * <p>
	 * If the DateTime object consists of only a date component, this method has no effect.
	 * </p>
	 * <p>
	 * The number of hours by which you increment a DateTime object can cause the date component to be adjusted, too. For example, if the
	 * adjustment is +24, the date component changes to the next calendar day.
	 * </p>
	 *
	 * @param n
	 *            The number of hours by which to increment the date-time. Can be positive or negative.
	 */
	@Override
	public void adjustHour(final int n);

	/**
	 * Increments a date-time by the number of hours you specify.
	 * <p>
	 * If the DateTime object consists of only a date component, this method has no effect.
	 * </p>
	 * <p>
	 * The number of hours by which you increment a DateTime object can cause the date component to be adjusted, too. For example, if the
	 * adjustment is +24, the date component changes to the next calendar day.
	 * </p>
	 *
	 * @param n
	 *            The number of hours by which to increment the date-time. Can be positive or negative.
	 * @param preserveLocalTime
	 *            Affects adjustments that cross a daylight-saving time boundary. Specify true to increment or decrement the GMT time by one
	 *            hour such that a 24-hour adjustment yields the same local time in the new day. If this parameter is false or omitted, GMT
	 *            time remains as adjusted and local time gains or loses an hour.
	 */
	@Override
	public void adjustHour(final int n, final boolean preserveLocalTime);

	/**
	 * Increments a date-time by the number of minutes you specify.
	 * <p>
	 * If the DateTime object consists of only a date component, this method has no effect.
	 * </p>
	 * <p>
	 * The number of minutes by which you increment a DateTime object can cause the date component to be adjusted, too. For example, if the
	 * adjustment is +1440, the date component changes to the next calendar day.
	 * </p>
	 *
	 * @param n
	 *            The number of minutes by which to increment the date-time. Can be positive or negative.
	 */
	@Override
	public void adjustMinute(final int n);

	/**
	 * Increments a date-time by the number of minutes you specify.
	 * <p>
	 * If the DateTime object consists of only a date component, this method has no effect.
	 * </p>
	 * <p>
	 * The number of minutes by which you increment a DateTime object can cause the date component to be adjusted, too. For example, if the
	 * adjustment is +1440, the date component changes to the next calendar day.
	 * </p>
	 *
	 * @param n
	 *            The number of minutes by which to increment the date-time. Can be positive or negative.
	 * @param preserveLocalTime
	 *            Affects adjustments that cross a daylight-saving time boundary. Specify true to increment or decrement the GMT time by one
	 *            hour such that a 24-hour adjustment yields the same local time in the new day. If this parameter is false or omitted, GMT
	 *            time remains as adjusted and local time gains or loses an hour.
	 */
	@Override
	public void adjustMinute(final int n, final boolean preserveLocalTime);

	/**
	 * Increments a date-time by the number of months you specify.
	 * <p>
	 * If the DateTime object consists of only a time component, this method has no effect.
	 * </p>
	 * <p>
	 * The number of months by which you increment a DateTime object can cause the year to be incremented. For example, if the adjustment is
	 * +12, the date component changes to the next calendar year.
	 * </p>
	 *
	 * @param n
	 *            The number of months by which to increment the date-time. Can be positive or negative.
	 */
	@Override
	public void adjustMonth(final int n);

	/**
	 * Increments a date-time by the number of months you specify.
	 * <p>
	 * If the DateTime object consists of only a time component, this method has no effect.
	 * </p>
	 * <p>
	 * The number of months by which you increment a DateTime object can cause the year to be incremented. For example, if the adjustment is
	 * +12, the date component changes to the next calendar year.
	 * </p>
	 *
	 * @param n
	 *            The number of months by which to increment the date-time. Can be positive or negative.
	 * @param preserveLocalTime
	 *            Affects adjustments that cross a daylight-saving time boundary. Specify true to increment or decrement the GMT time by one
	 *            hour such that a 24-hour adjustment yields the same local time in the new day. If this parameter is false or omitted, GMT
	 *            time remains as adjusted and local time gains or loses an hour.
	 */
	@Override
	public void adjustMonth(final int n, final boolean preserveLocalTime);

	/**
	 * Increments a date-time by the number of seconds you specify.
	 * <p>
	 * If the DateTime object consists of only a date component, this method has no effect.
	 * </p>
	 *
	 * @param n
	 *            The number of seconds by which to increment the date-time. Can be positive or negative.
	 */
	@Override
	public void adjustSecond(final int n);

	/**
	 * Increments a date-time by the number of seconds you specify.
	 * <p>
	 * If the DateTime object consists of only a date component, this method has no effect.
	 * </p>
	 *
	 * @param n
	 *            The number of seconds by which to increment the date-time. Can be positive or negative.
	 * @param preserveLocalTime
	 *            Affects adjustments that cross a daylight-saving time boundary. Specify true to increment or decrement the GMT time by one
	 *            hour such that a 24-hour adjustment yields the same local time in the new day. If this parameter is false or omitted, GMT
	 *            time remains as adjusted and local time gains or loses an hour.
	 */
	@Override
	public void adjustSecond(final int n, final boolean preserveLocalTime);

	/**
	 * Increments a date-time by the number of years you specify.
	 * <p>
	 * If the DateTime object consists of only a time component, this method has no effect.
	 * </p>
	 * <p>
	 * The number of years by which you increment a DateTime object can cause the century to change. If this happens, the year is
	 * represented as a string with four digits instead of two, such as 08/18/2001.
	 * </p>
	 *
	 * @param n
	 *            The number of years by which to increment the date-time. Can be positive or negative.
	 */
	@Override
	public void adjustYear(final int n);

	/**
	 * Increments a date-time by the number of years you specify.
	 * <p>
	 * If the DateTime object consists of only a time component, this method has no effect.
	 * </p>
	 * <p>
	 * The number of years by which you increment a DateTime object can cause the century to change. If this happens, the year is
	 * represented as a string with four digits instead of two, such as 08/18/2001.
	 * </p>
	 *
	 * @param n
	 *            The number of years by which to increment the date-time. Can be positive or negative.
	 * @param preserveLocalTime
	 *            Affects adjustments that cross a daylight-saving time boundary. Specify true to increment or decrement the GMT time by one
	 *            hour such that a 24-hour adjustment yields the same local time in the new day. If this parameter is false or omitted, GMT
	 *            time remains as adjusted and local time gains or loses an hour.
	 */
	@Override
	public void adjustYear(final int n, final boolean preserveLocalTime);

	/**
	 * Changes the {@link #getTimeZone() TimeZone} and {@link #isDST() IsDST} properties as specified.
	 * <p>
	 * These changes also affect the {@link #getZoneTime() ZoneTime} property.
	 * </p>
	 * <p>
	 * These changes do not affect the {@link #getGMTTime() GMTTime}</a> and the {@link #getLocalTime() LocalTime} properties.
	 * </p>
	 * <p>
	 * This method has no effect on an invalid {@link #getTimeZone() TimeZone} (because the DateTime object lacks the date or the time).
	 * {@link #getTimeZone() TimeZone} continues to return 0.
	 * </p>
	 * <p>
	 * Daylight saving time is a world-wide practice of decreasing energy consumption by adjusting clocks forward an hour, changing the
	 * offset from GMT. The {@link #getTimeZone() TimeZone} property indicates the base offset from GMT. For example, standard time in New
	 * York City is GMT -5, but when daylight savings is in effect, New York City is GMT -4.
	 * </p>
	 *
	 * @param zone
	 *            A time zone -12 through 12.
	 * @param isDST
	 *            Indicates whether daylight-saving time is in effect (true) or not (false).
	 */
	@Override
	public void convertToZone(final int zone, final boolean isDST);

	/**
	 * A string representation of the date part of the time-date.
	 */
	@Override
	public String getDateOnly();

	/**
	 * A string representing a date-time, converted to Greenwich Mean Time (timezone 0).
	 * <p>
	 * The GMT time for a date created from a string is determined using the operating system time zone setting, unless the date string
	 * specifies a time zone.
	 * </p>
	 *
	 */
	@Override
	public String getGMTTime();

	/**
	 * A string representing a date-time in the local time zone. Additional methods let you set the local time with Date, integer, and
	 * Boolean values.
	 * <p>
	 * When you set this property, it changes the value of the date-time that the object represents, and therefore affects the
	 * {@link #getGMTTime() GMTTime} property.
	 * </p>
	 *
	 * @return A string representing a date-time in the local time zone.
	 *
	 */
	@Override
	public String getLocalTime();

	/**
	 * The Notes session that contains this DateTime object.
	 */
	@Override
	public org.openntf.domino.Session getParent();

	/**
	 * A string representation of the time part of the date-time.
	 *
	 */
	@Override
	public String getTimeOnly();

	/**
	 * An integer representing the time zone of a date-time. This integer indicates the number of hours that must be added to the time to
	 * get Greenwich Mean Time when daylight-saving time is not in effect. May be positive or negative.
	 * <p>
	 * When you create a new DateTime object, this property is set according to the time zone setting of the computer on which the program
	 * runs or, for remote (IIOP) operations, the Domino server.
	 * </p>
	 * <p>
	 * This property is not set and returns 0 if the DateTime object does not include both a time and a date.
	 * </p>
	 * <p>
	 * When you access a date-time value from an item in a document using the {@link Item#getDateTimeValue()}, this property is set
	 * according to the time zone stored in the item.
	 * </p>
	 * <p>
	 * The {@link #convertToZone(int, boolean)} method and {@link #getLocalTime() LocalTime} property may modify the value of this property.
	 * </p>
	 *
	 * @return An integer representing the time zone of a date-time. This integer indicates the number of hours that must be added to the
	 *         time to get Greenwich Mean Time when daylight-saving time is not in effect. May be positive or negative.
	 *
	 */
	@Override
	public int getTimeZone();

	/**
	 * A string representation of the time adjusted for the {@link #getTimeZone() TimeZone} and {@link #isDST() IsDST} properties.
	 * <p>
	 * Initially {@link #getZoneTime() ZoneTime} is the same as {@link #getLocalTime() LocalTime}. If you use the
	 * {@link #convertToZone(int, boolean)} method or {@link #getLocalTime()}, changes to {@link #getTimeZone() TimeZone} and
	 * {@link #isDST() IsDST} are reflected in {@link #getZoneTime() ZoneTime}, but {@link #getLocalTime() LocalTime} stays the same.
	 * </p>
	 *
	 * @return A string representation of the time adjusted for the {@link #getTimeZone() TimeZone} and {@link #isDST() IsDST} properties.
	 *
	 */
	@Override
	public String getZoneTime();

	/**
	 * Indicates whether the time reflects daylight-saving time.
	 * <p>
	 * For remote (IIOP) operations, the current computer is the Domino server. For local operations, the current computer is the computer
	 * on which the program is running.
	 * </p>
	 * <p>
	 * The {@link #convertToZone(int, boolean)} method and {@link #getLocalTime() LocalTime} property may modify the value of this property.
	 * </p>
	 *
	 * @return true when the time reflects daylight-saving time
	 */
	@Override
	public boolean isDST();

	/**
	 * Sets the date component to a wildcard value, which means that it matches any date. The time component is unaffected.
	 * <p>
	 * This method is useful when passing the object as an argument to other methods.
	 * </p>
	 * <p>
	 * A DateTime object for which you have invoked this method may not be convertible to string format.
	 * </p>
	 *
	 */
	@Override
	public void setAnyDate();

	/**
	 * Sets the time component to a wildcard value, which means that it matches any time. The date component is unaffected.
	 * <p>
	 * This method is useful when passing the object as an argument to other methods.
	 * </p>
	 * <p>
	 * A date-time object for which you have invoked this method may not be convertible to string format.
	 * </p>
	 *
	 */
	@Override
	public void setAnyTime();

	/**
	 * Set the year, month and day components of this DateTime object
	 *
	 * @param year
	 * @param month
	 * @param day
	 */
	@Override
	public void setLocalDate(final int year, final int month, final int day);

	/**
	 * Set the year, month and day components of this DateTime object
	 *
	 * @param year
	 * @param month
	 * @param day
	 * @param preserveLocalTime
	 *            affects adjustments from the existing date that cross a daylight-saving time boundary. Specify true to increment or
	 *            decrement the GMT time by one hour so that a 24-hour adjustment yields the same local time in the new day. If this
	 *            parameter is false or omitted, GMT time remains as adjusted and local time gains or loses an hour.
	 */
	@Override
	public void setLocalDate(final int year, final int month, final int day, final boolean preserveLocalTime);

	/**
	 * Sets the date-time using a Calendar instance
	 *
	 * @param calendar
	 */
	@Override
	public void setLocalTime(final java.util.Calendar calendar);

	/**
	 * Sets the date-time using a Date instance
	 *
	 * @param date
	 */
	@Override
	public void setLocalTime(final Date date);

	/**
	 * Sets the time parts using hour, minute, second and hundreth of a second
	 *
	 * @param hour
	 * @param minute
	 * @param second
	 * @param hundredth
	 */
	@Override
	public void setLocalTime(final int hour, final int minute, final int second, final int hundredth);

	/**
	 * Sets the date and time using a String
	 *
	 * @param time
	 *            String representation of a date-time interpreted using regional settings.
	 */
	@Override
	public void setLocalTime(final String time);

	/**
	 * Sets the value of a date-time to now (today's date and current time).
	 * <p>
	 * For remote (IIOP) operations, the date-time value is based on the host computer. For local operations, the date-time value is based
	 * on the computer on which the program is running.
	 * </p>
	 * <p>
	 * This method works by taking a snapshot of the current date-time at the moment that the method is executed. After the method executes,
	 * the value of the DateTime object does <em>not </em>update itself to always reflect the current date-time. Its value only changes if
	 * you change it explicitly.
	 * </p>
	 *
	 */
	@Override
	public void setNow();

	/**
	 * Finds the difference in seconds between this date-time and another.
	 *
	 * @param dt
	 *            Any date-time value.
	 * @return The date-time of this object minus the date-time of the parameter, in seconds.
	 */
	@Override
	public int timeDifference(final lotus.domino.DateTime dt);

	/**
	 * Finds the difference in seconds between this date-time and another.
	 *
	 * @param dt
	 *            Any date-time value.
	 * @return The date-time of this object minus the date-time of the parameter, in seconds.
	 */
	@Override
	public double timeDifferenceDouble(final lotus.domino.DateTime dt);

	/**
	 * Converts a Notes date and time into a java.util.Date object.
	 *
	 * @return The date-time of the object in Java format.
	 */
	@Override
	public Date toJavaDate();

	/**
	 * Creates a new dateTime object with the same date and time values.
	 */
	@Override
	public DateTime clone();

	// /**
	// * To java cal.
	// *
	// * @return Java Calendar object, same as used internally by org.openntf.domino.DateTime class
	// */
	// public Calendar toJavaCal();
}
