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

import java.util.Calendar;
import java.util.Date;

import org.openntf.domino.types.Encapsulated;
import org.openntf.domino.types.SessionDescendant;

/**
 * The Interface DateTime.
 */
public interface DateTime extends Base<lotus.domino.DateTime>, lotus.domino.DateTime, org.openntf.domino.ext.DateTime, Encapsulated,
		SessionDescendant {

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateTime#adjustDay(int)
	 */
	@Override
	public void adjustDay(final int n);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateTime#adjustDay(int, boolean)
	 */
	@Override
	public void adjustDay(final int n, final boolean preserveLocalTime);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateTime#adjustHour(int)
	 */
	@Override
	public void adjustHour(final int n);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateTime#adjustHour(int, boolean)
	 */
	@Override
	public void adjustHour(final int n, final boolean preserveLocalTime);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateTime#adjustMinute(int)
	 */
	@Override
	public void adjustMinute(final int n);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateTime#adjustMinute(int, boolean)
	 */
	@Override
	public void adjustMinute(final int n, final boolean preserveLocalTime);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateTime#adjustMonth(int)
	 */
	@Override
	public void adjustMonth(final int n);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateTime#adjustMonth(int, boolean)
	 */
	@Override
	public void adjustMonth(final int n, final boolean preserveLocalTime);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateTime#adjustSecond(int)
	 */
	@Override
	public void adjustSecond(final int n);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateTime#adjustSecond(int, boolean)
	 */
	@Override
	public void adjustSecond(final int n, final boolean preserveLocalTime);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateTime#adjustYear(int)
	 */
	@Override
	public void adjustYear(final int n);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateTime#adjustYear(int, boolean)
	 */
	@Override
	public void adjustYear(final int n, final boolean preserveLocalTime);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateTime#convertToZone(int, boolean)
	 */
	@Override
	public void convertToZone(final int zone, final boolean isDST);

	/**
	 * Compares current date with another and returns boolean of whether they are the same.
	 * 
	 * @param comparDate
	 *            DateTime to compare to current date
	 * @return boolean, whether or not the two dates are the same
	 */
	public boolean equals(final DateTime comparDate);

	/**
	 * Equals ignore date.
	 * 
	 * @param comparDate
	 *            DateTime to compare to the current DateTime
	 * @return boolean is time is the same, including millisecond
	 */
	public boolean equalsIgnoreDate(final DateTime comparDate);

	/**
	 * Equals ignore time.
	 * 
	 * @param comparDate
	 *            DateTime to compare to the current DateTime
	 * @return boolean is date is the same
	 */
	public boolean equalsIgnoreTime(final DateTime comparDate);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateTime#getDateOnly()
	 */
	@Override
	public String getDateOnly();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateTime#getGMTTime()
	 */
	@Override
	public String getGMTTime();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateTime#getLocalTime()
	 */
	@Override
	public String getLocalTime();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateTime#getParent()
	 */
	@Override
	public org.openntf.domino.Session getParent();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateTime#getTimeOnly()
	 */
	@Override
	public String getTimeOnly();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateTime#getTimeZone()
	 */
	@Override
	public int getTimeZone();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateTime#getZoneTime()
	 */
	@Override
	public String getZoneTime();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateTime#isDST()
	 */
	@Override
	public boolean isDST();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateTime#setAnyDate()
	 */
	@Override
	public void setAnyDate();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateTime#setAnyTime()
	 */
	@Override
	public void setAnyTime();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateTime#setLocalDate(int, int, int)
	 */
	@Override
	public void setLocalDate(final int year, final int month, final int day);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateTime#setLocalDate(int, int, int, boolean)
	 */
	@Override
	public void setLocalDate(final int year, final int month, final int day, final boolean preserveLocalTime);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateTime#setLocalTime(java.util.Calendar)
	 */
	@Override
	public void setLocalTime(final Calendar calendar);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateTime#setLocalTime(java.util.Date)
	 */
	@Override
	public void setLocalTime(final Date date);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateTime#setLocalTime(int, int, int, int)
	 */
	@Override
	public void setLocalTime(final int hour, final int minute, final int second, final int hundredth);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateTime#setLocalTime(java.lang.String)
	 */
	@Override
	public void setLocalTime(final String time);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateTime#setNow()
	 */
	@Override
	public void setNow();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateTime#timeDifference(lotus.domino.DateTime)
	 */
	@Override
	public int timeDifference(final lotus.domino.DateTime dt);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateTime#timeDifferenceDouble(lotus.domino.DateTime)
	 */
	@Override
	public double timeDifferenceDouble(final lotus.domino.DateTime dt);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateTime#toJavaDate()
	 */
	@Override
	public Date toJavaDate();

	/**
	 * To java cal.
	 * 
	 * @return Java Calendar object, same as used internally by org.openntf.domino.DateTime class
	 */
	public Calendar toJavaCal();
}
