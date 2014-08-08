package org.openntf.formula.impl;

import static com.ibm.icu.util.Calendar.DAY_OF_MONTH;
import static com.ibm.icu.util.Calendar.HOUR_OF_DAY;
import static com.ibm.icu.util.Calendar.MILLISECOND;
import static com.ibm.icu.util.Calendar.MINUTE;
import static com.ibm.icu.util.Calendar.MONTH;
import static com.ibm.icu.util.Calendar.SECOND;
import static com.ibm.icu.util.Calendar.YEAR;
import static com.ibm.icu.util.Calendar.getInstance;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

import org.openntf.formula.DateTime;
import org.openntf.formula.Formulas;

import com.ibm.icu.util.Calendar;

public class DateTimeImpl implements DateTime, Serializable {
	private static final long serialVersionUID = -4270572924947814291L;
	private Locale iLocale;
	private Calendar iCal;
	private boolean iNoDate = false;
	private boolean iNoTime = false;

	DateTimeImpl(final Locale loc) {
		iLocale = loc;
		iCal = getInstance(loc);
	}

	public void adjustDay(final int n) {
		if (!iNoDate)
			iCal.add(DAY_OF_MONTH, n);
	}

	public void adjustHour(final int n) {
		if (!iNoTime)
			iCal.add(HOUR_OF_DAY, n);
	}

	public void adjustMinute(final int n) {
		if (!iNoTime)
			iCal.add(MINUTE, n);
	}

	public void adjustMonth(final int n) {
		if (!iNoDate)
			iCal.add(MONTH, n);
	}

	public void adjustSecond(final int n) {
		if (!iNoTime)
			iCal.add(SECOND, n);
	}

	public void adjustYear(final int n) {
		if (!iNoDate)
			iCal.add(YEAR, n);
	}

	public void convertToZone(final int zone, final boolean isDST) {
		int unused;
		// TODO MSt: Timezone support (Really needed?)
	}

	public String getDateOnly() {
		if (iNoDate)
			return "";
		return Formulas.getFormatter(iLocale).formatCalDateOnly(iCal);
	}

	public String getLocalTime() {
		if (iNoDate)
			return getTimeOnly();
		if (iNoTime)
			return getDateOnly();
		return Formulas.getFormatter(iLocale).formatCalDateTime(iCal);
	}

	public String getTimeOnly() {
		if (iNoTime)
			return "";
		return Formulas.getFormatter(iLocale).formatCalTimeOnly(iCal);
	}

	public int getTimeZone() {
		int unused;
		// TODO MSt: Timezone support
		return 0;
	}

	public String getZoneTime() {
		int unused;
		// TODO MSt: Timezone support
		return null;
	}

	public boolean isAnyDate() {
		return iNoDate;
	}

	public boolean isAnyTime() {
		return iNoTime;
	}

	public boolean isDST() {
		int unused;
		// TODO MSt: Timezone support
		return false;
	}

	public void setAnyDate() {
		iNoDate = true;
	}

	public void setAnyTime() {
		iNoTime = true;
	}

	public void setLocalDate(final int year, final int month, final int day) {
		iCal.set(YEAR, year);
		iCal.set(MONTH, month - 1);
		iCal.set(DAY_OF_MONTH, day);
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
		iCal.set(HOUR_OF_DAY, hour);
		iCal.set(MINUTE, minute);
		iCal.set(SECOND, second);
		iCal.set(MILLISECOND, hundredth * 10);
		iNoTime = false;
	}

	public void setLocalTime(final String time) {
		setLocalTime(time, true);
	}

	public void setLocalTime(final String time, final boolean parseLenient) {
		boolean[] noDT = new boolean[2];
		iCal = Formulas.getFormatter(iLocale).parseDateToCal(time, noDT, parseLenient);
		iNoDate = noDT[0];
		iNoTime = noDT[1];
	}

	public void setNow() {
		setLocalTime(new Date());
	}

	public int timeDifference(final DateTime dt) {
		return (int) timeDifferenceDouble(dt);
	}

	public double timeDifferenceDouble(final DateTime dt) {
		// What if isAnyDate or isAnyTime for one of these?
		return (iCal.getTimeInMillis() - dt.toJavaCal().getTimeInMillis()) / 1000;
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

	public int compare(final DateTime sdt1, final DateTime sdt2) {
		boolean noDate1 = sdt1.isAnyDate();
		boolean noDate2 = sdt2.isAnyDate();
		if (noDate1 != noDate2)
			return (noDate2 ? 1 : -1);
		boolean noTime1 = sdt1.isAnyTime();
		boolean noTime2 = sdt2.isAnyTime();
		if (noDate1) {
			if (noTime1 && noTime2)
				return (0);
			if (noTime1 != noTime2)
				return (noTime2 ? 1 : -1);
		}
		Calendar cal1 = sdt1.toJavaCal();
		Calendar cal2 = sdt2.toJavaCal();
		int i1, i2;
		if (!noDate1) {
			i1 = cal1.get(YEAR);
			i2 = cal2.get(YEAR);
			if (i1 != i2)
				return (i1 > i2 ? 1 : -1);
			i1 = cal1.get(MONTH);
			i2 = cal2.get(MONTH);
			if (i1 != i2)
				return (i1 > i2 ? 1 : -1);
			i1 = cal1.get(DAY_OF_MONTH);
			i2 = cal2.get(DAY_OF_MONTH);
			if (i1 != i2)
				return (i1 > i2 ? 1 : -1);
			if (noTime1 && noTime2)
				return 0;
			if (noTime1 != noTime2)
				return (noTime2 ? 1 : -1);
		}
		i1 = cal1.get(HOUR_OF_DAY);
		i2 = cal2.get(HOUR_OF_DAY);
		if (i1 != i2)
			return (i1 > i2 ? 1 : -1);
		i1 = cal1.get(MINUTE);
		i2 = cal2.get(MINUTE);
		if (i1 != i2)
			return (i1 > i2 ? 1 : -1);
		i1 = cal1.get(SECOND);
		i2 = cal2.get(SECOND);
		if (i1 != i2)
			return (i1 > i2 ? 1 : -1);
		return 0;
	}

	@Override
	public boolean equals(final Object o) {
		if (o instanceof DateTime)
			return (compare(this, (DateTime) o) == 0);
		return super.equals(o);
	}
}
