package org.openntf.domino;

import java.util.Calendar;
import java.util.Date;

public interface DateTime extends Base<lotus.domino.DateTime>, lotus.domino.DateTime {
	@Override
	public void adjustDay(int n);

	@Override
	public void adjustDay(int n, boolean preserveLocalTime);

	@Override
	public void adjustHour(int n);

	@Override
	public void adjustHour(int n, boolean preserveLocalTime);

	@Override
	public void adjustMinute(int n);

	@Override
	public void adjustMinute(int n, boolean preserveLocalTime);

	@Override
	public void adjustMonth(int n);

	@Override
	public void adjustMonth(int n, boolean preserveLocalTime);

	@Override
	public void adjustSecond(int n);

	@Override
	public void adjustSecond(int n, boolean preserveLocalTime);

	@Override
	public void adjustYear(int n);

	@Override
	public void adjustYear(int n, boolean preserveLocalTime);

	@Override
	public void convertToZone(int zone, boolean isDST);

	@Override
	public String getDateOnly();

	@Override
	public String getGMTTime();

	@Override
	public String getLocalTime();

	@Override
	public Session getParent();

	@Override
	public String getTimeOnly();

	@Override
	public int getTimeZone();

	@Override
	public String getZoneTime();

	@Override
	public boolean isDST();

	@Override
	public void setAnyDate();

	@Override
	public void setAnyTime();

	@Override
	public void setLocalDate(int year, int month, int day);

	@Override
	public void setLocalDate(int year, int month, int day, boolean preserveLocalTime);

	@Override
	public void setLocalTime(Calendar calendar);

	@Override
	public void setLocalTime(Date date);

	@Override
	public void setLocalTime(int hour, int minute, int second, int hundredth);

	@Override
	public void setLocalTime(String time);

	@Override
	public void setNow();

	@Override
	public int timeDifference(lotus.domino.DateTime dt);

	@Override
	public double timeDifferenceDouble(lotus.domino.DateTime dt);

	@Override
	public Date toJavaDate();
}