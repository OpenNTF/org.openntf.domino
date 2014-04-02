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

import com.ibm.icu.util.Calendar;

/**
 * The Interface SimpleDateTime.
 */
public interface ISimpleDateTime {

	public abstract void adjustDay(final int n);

	public abstract void adjustHour(final int n);

	public abstract void adjustMinute(final int n);

	public abstract void adjustMonth(final int n);

	public abstract void adjustSecond(final int n);

	public abstract void adjustYear(final int n);

	public abstract void convertToZone(final int zone, final boolean isDST);

	public abstract String getDateOnly();

	public abstract String getLocalTime();

	public abstract String getTimeOnly();

	public abstract int getTimeZone();

	public abstract String getZoneTime();

	public abstract boolean isDST();

	public abstract void setAnyDate();

	public abstract void setAnyTime();

	public abstract void setLocalDate(final int year, final int month, final int day);

	public abstract void setLocalTime(final java.util.Calendar calendar);

	public abstract void setLocalTime(final Date date);

	public abstract void setLocalTime(final int hour, final int minute, final int second, final int hundredth);

	public abstract void setLocalTime(final String time);

	public abstract void setNow();

	public abstract int timeDifference(final ISimpleDateTime dt);

	public abstract double timeDifferenceDouble(final ISimpleDateTime dt);

	public abstract Date toJavaDate();

	public abstract Calendar toJavaCal();
}
