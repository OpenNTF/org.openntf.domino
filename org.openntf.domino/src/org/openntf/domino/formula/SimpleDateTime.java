package org.openntf.domino.formula;

import java.text.ParsePosition;
import java.util.Date;
import java.util.Locale;

import org.openntf.domino.ISimpleDateTime;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.util.Calendar;

public class SimpleDateTime implements ISimpleDateTime {
	private Locale iLocale;
	private Calendar iCal;
	private boolean iNoDate = false;
	private boolean iNoTime = false;

	public SimpleDateTime() {
		this(Locale.getDefault());
	}

	public SimpleDateTime(final Locale loc) {
		iLocale = loc;
		iCal = Calendar.getInstance(loc);
	}

	public void adjustDay(final int n) {
		if (!iNoDate)
			iCal.add(Calendar.DAY_OF_MONTH, n);
	}

	public void adjustHour(final int n) {
		if (!iNoTime)
			iCal.add(Calendar.HOUR_OF_DAY, n);
	}

	public void adjustMinute(final int n) {
		if (!iNoTime)
			iCal.add(Calendar.MINUTE, n);
	}

	public void adjustMonth(final int n) {
		if (!iNoDate)
			iCal.add(Calendar.MONTH, n);
	}

	public void adjustSecond(final int n) {
		if (!iNoTime)
			iCal.add(Calendar.SECOND, n);
	}

	public void adjustYear(final int n) {
		if (!iNoDate)
			iCal.add(Calendar.YEAR, n);
	}

	public void convertToZone(final int zone, final boolean isDST) {
		int unused;
		// TODO Really needed?
	}

	public String getDateOnly() {
		if (iNoDate)
			return "";
		DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, iLocale);
		df.setCalendar(iCal);
		return df.format(iCal.getTime());
	}

	public String getLocalTime() {
		if (iNoDate)
			return getTimeOnly();
		if (iNoTime)
			return getDateOnly();
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.LONG, iLocale);
		df.setCalendar(iCal);
		return df.format(iCal.getTime());
	}

	public String getTimeOnly() {
		if (iNoTime)
			return "";
		DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM, iLocale);
		df.setCalendar(iCal);
		return df.format(iCal.getTime());
	}

	public int getTimeZone() {
		int unused;
		// TODO Auto-generated method stub
		return 0;
	}

	public String getZoneTime() {
		int unused;
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isDST() {
		int unused;
		// TODO Auto-generated method stub
		return false;
	}

	public void setAnyDate() {
		iNoDate = true;
	}

	public void setAnyTime() {
		iNoTime = true;
	}

	public void setLocalDate(final int year, final int month, final int day) {
		iCal.set(Calendar.YEAR, year);
		iCal.set(Calendar.MONTH, month - 1);
		iCal.set(Calendar.DAY_OF_MONTH, day);
		iNoDate = false;
	}

	public void setLocalTime(final java.util.Calendar otherCal) {
		setLocalTime(otherCal.getTime());
	}

	public void setLocalTime(final Date date) {
		iCal.setTime(date);
		iNoDate = false;
		iNoTime = false;
	}

	public void setLocalTime(final int hour, final int minute, final int second, final int hundredth) {
		iCal.set(Calendar.HOUR_OF_DAY, hour);
		iCal.set(Calendar.MINUTE, minute);
		iCal.set(Calendar.SECOND, second);
		iCal.set(Calendar.MILLISECOND, hundredth * 10);
		iNoTime = false;
	}

	public void setLocalTime(String time) {
		time = time.trim();
		// Should an empty string lead to a DateTime with noDate=noTime=true?
		// (Lotus doesn't accept empty strings here.)
		char spec = 0;
		if (time.equalsIgnoreCase("TODAY"))
			spec = 'H';
		else if (time.equalsIgnoreCase("TOMORROW"))
			spec = 'M';
		else if (time.equalsIgnoreCase("YESTERDAY"))
			spec = 'G';
		if (spec != 0) {
			setNow();
			if (spec == 'M')
				adjustDay(1);
			else if (spec == 'G')
				adjustDay(-1);
			iNoDate = false;
			iNoTime = true;
			return;
		}
		Calendar c2 = Calendar.getInstance(iLocale);
		c2.setLenient(false);
		for (;;) {
			c2.clear();
			/*
			 * First attempt: Take a full date-time format MEDIUM
			 */
			DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, iLocale);
			ParsePosition p = new ParsePosition(0);
			df.parse(time, c2, p);
			if (p.getErrorIndex() < 0)
				break;
			if (!c2.isSet(Calendar.DAY_OF_MONTH) || !c2.isSet(Calendar.MONTH)) {
				//Try with SHORT format			
				c2.clear();
				df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, iLocale);
				p.setIndex(0);
				p.setErrorIndex(-1);
				df.parse(time, c2, p);
				if (!c2.isSet(Calendar.DAY_OF_MONTH) || !c2.isSet(Calendar.MONTH)) {	// Give up with date
					c2.clear();
					p.setErrorIndex(0);
				}
			}
			if (c2.isSet(Calendar.MINUTE))
				break;
			/*
			 * If no time found yet (i.e. at least hour+minute like Lotus), try to fish it
			 */
			p.setIndex(p.getErrorIndex());
			p.setErrorIndex(-1);
			df = DateFormat.getTimeInstance(DateFormat.MEDIUM, iLocale);
			df.parse(time, c2, p);
			if (c2.isSet(Calendar.MINUTE))
				break;
			if (c2.isSet(Calendar.DAY_OF_MONTH)) { // Set back possible hour (in accordance with Lotus)
				c2.clear(Calendar.HOUR);
				c2.clear(Calendar.HOUR_OF_DAY);
				break;
			}
			/*
			 * Left: No date found, no time found; so:
			 */
			throw new IllegalArgumentException("Illegal date string '" + time + "'");
		}
		boolean contDate = c2.isSet(Calendar.DAY_OF_MONTH);
		boolean contTime = c2.isSet(Calendar.MINUTE);
		if (c2.isSet(Calendar.YEAR)) {
			if (!contTime)
				c2.set(Calendar.HOUR_OF_DAY, 0);
		} else {
			Calendar now = Calendar.getInstance(iLocale);
			now.setTime(new Date());
			c2.set(Calendar.YEAR, now.get(Calendar.YEAR));
			if (!contDate) {
				c2.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));
				c2.set(Calendar.MONTH, now.get(Calendar.MONTH));
			}
		}
		if (!c2.isSet(Calendar.MINUTE))
			c2.set(Calendar.MINUTE, 0);
		if (!c2.isSet(Calendar.SECOND))
			c2.set(Calendar.SECOND, 0);
		try {
			c2.getTime();
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Illegal date string '" + time + "': " + e.getMessage());
		}
		iCal = c2;
		iNoDate = !contDate;
		iNoTime = !contTime;
	}

	public void setNow() {
		setLocalTime(new Date());
	}

	public int timeDifference(final org.openntf.domino.ISimpleDateTime dt) {
		return (int) timeDifferenceDouble(dt);
	}

	public double timeDifferenceDouble(final org.openntf.domino.ISimpleDateTime dt) {
		return (iCal.getTimeInMillis() - dt.toJavaCal().getTimeInMillis()) * 1000;
	}

	public Date toJavaDate() {
		return iCal.getTime();
	}

	public Calendar toJavaCal() {
		return iCal;
	}

	@Override
	public String toString() {
		return getLocalTime();
	}
}
