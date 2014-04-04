package org.openntf.domino.formula;

import java.util.Date;
import java.util.Locale;

import org.openntf.domino.ISimpleDateTime;

import com.ibm.icu.util.Calendar;

public class SimpleDateTime implements ISimpleDateTime {
	private Locale iLocale;
	private Calendar iCal;
	private boolean iNoDate = false;
	private boolean iNoTime = false;

	SimpleDateTime(final Locale loc) {
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
		return DominoFormatter.getInstance(iLocale).formatCalDateOnly(iCal);
	}

	public String getLocalTime() {
		if (iNoDate)
			return getTimeOnly();
		if (iNoTime)
			return getDateOnly();
		return DominoFormatter.getInstance(iLocale).formatCalDateTime(iCal);
	}

	public String getTimeOnly() {
		if (iNoTime)
			return "";
		return DominoFormatter.getInstance(iLocale).formatCalTimeOnly(iCal);
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

	public boolean isAnyDate() {
		return iNoDate;
	}

	public boolean isAnyTime() {
		return iNoTime;
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

	public void setLocalTime(final String time) {
		setLocalTime(time, true);
	}

	public void setLocalTime(final String time, final boolean parseLenient) {
		boolean[] noDT = new boolean[2];
		iCal = DominoFormatter.getInstance(iLocale).parseDateToCal(time, noDT, parseLenient);
		iNoDate = noDT[0];
		iNoTime = noDT[1];
	}

	public void setNow() {
		setLocalTime(new Date());
	}

	public int timeDifference(final ISimpleDateTime dt) {
		return (int) timeDifferenceDouble(dt);
	}

	public double timeDifferenceDouble(final ISimpleDateTime dt) {
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

	public int compare(final ISimpleDateTime sdt1, final ISimpleDateTime sdt2) {
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
			i1 = cal1.get(Calendar.YEAR);
			i2 = cal2.get(Calendar.YEAR);
			if (i1 != i2)
				return (i1 > i2 ? 1 : -1);
			i1 = cal1.get(Calendar.MONTH);
			i2 = cal2.get(Calendar.MONTH);
			if (i1 != i2)
				return (i1 > i2 ? 1 : -1);
			i1 = cal1.get(Calendar.DAY_OF_MONTH);
			i2 = cal2.get(Calendar.DAY_OF_MONTH);
			if (i1 != i2)
				return (i1 > i2 ? 1 : -1);
			if (noTime1 && noTime2)
				return 0;
			if (noTime1 != noTime2)
				return (noTime2 ? 1 : -1);
		}
		i1 = cal1.get(Calendar.HOUR_OF_DAY);
		i2 = cal2.get(Calendar.HOUR_OF_DAY);
		if (i1 != i2)
			return (i1 > i2 ? 1 : -1);
		i1 = cal1.get(Calendar.MINUTE);
		i2 = cal2.get(Calendar.MINUTE);
		if (i1 != i2)
			return (i1 > i2 ? 1 : -1);
		i1 = cal1.get(Calendar.SECOND);
		i2 = cal2.get(Calendar.SECOND);
		if (i1 != i2)
			return (i1 > i2 ? 1 : -1);
		return 0;
	}

	@Override
	public boolean equals(final Object o) {
		if (o instanceof ISimpleDateTime)
			return (compare(this, (ISimpleDateTime) o) == 0);
		return super.equals(o);
	}
}
