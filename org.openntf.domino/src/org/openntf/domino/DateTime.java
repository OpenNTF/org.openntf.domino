package org.openntf.domino;

import java.util.Calendar;
import java.util.Date;

import lotus.domino.Session;

public interface DateTime extends Base<lotus.domino.DateTime>, lotus.domino.DateTime {
	@Override
	public void adjustDay(int arg0);

	@Override
	public void adjustDay(int arg0, boolean arg1);

	@Override
	public void adjustHour(int arg0);

	@Override
	public void adjustHour(int arg0, boolean arg1);

	@Override
	public void adjustMinute(int arg0);

	@Override
	public void adjustMinute(int arg0, boolean arg1);

	@Override
	public void adjustMonth(int arg0);

	@Override
	public void adjustMonth(int arg0, boolean arg1);

	@Override
	public void adjustSecond(int arg0);

	@Override
	public void adjustSecond(int arg0, boolean arg1);

	@Override
	public void adjustYear(int arg0);

	@Override
	public void adjustYear(int arg0, boolean arg1);

	@Override
	public void convertToZone(int arg0, boolean arg1);

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
	public void setLocalDate(int arg0, int arg1, int arg2);

	@Override
	public void setLocalDate(int arg0, int arg1, int arg2, boolean arg3);

	@Override
	public void setLocalTime(Calendar arg0);

	@Override
	public void setLocalTime(Date arg0);

	@Override
	public void setLocalTime(int arg0, int arg1, int arg2, int arg3);

	@Override
	public void setLocalTime(String arg0);

	@Override
	public void setNow();

	@Override
	public int timeDifference(lotus.domino.DateTime arg0);

	@Override
	public double timeDifferenceDouble(lotus.domino.DateTime arg0);

	@Override
	public Date toJavaDate();
}
