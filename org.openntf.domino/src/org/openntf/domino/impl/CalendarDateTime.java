package org.openntf.domino.impl;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.DateTime;
import org.openntf.domino.ISimpleDateTime;
import org.openntf.domino.Session;
import org.openntf.domino.events.EnumEvent;
import org.openntf.domino.events.IDominoEvent;
import org.openntf.domino.events.IDominoListener;

import com.ibm.icu.util.Calendar;

/**
 * This is a class that has no dependency to the lotus.domino API. All calculations are done on calendars. This class is used in at-Formula
 * engine.
 * 
 * Warning, this class is not yet completely implemented
 * 
 * @author Roland Praml, Foconis AG
 * 
 */
@Deprecated
public class CalendarDateTime implements DateTime, Cloneable {
	Calendar instance = Calendar.getInstance();
	boolean anyTime = false;
	boolean anyDate = false;

	@Override
	public DateTime clone() {
		return null;
	}

	public boolean isBefore(final DateTime compareDate) {
		return instance.before(compareDate.toJavaCal());
	}

	public boolean isAfter(final DateTime compareDate) {
		return instance.after(compareDate.toJavaCal());
	}

	public boolean isAnyTime() {
		return anyTime;
	}

	public boolean isAnyDate() {
		return anyDate;
	}

	public Calendar toJavaCal() {
		return (Calendar) instance.clone();
	}

	public void setLocalTime(final Calendar calendar) {
		instance = (Calendar) calendar.clone();
	}

	public int compareTo(final DateTime arg0) {
		return instance.compareTo(arg0.toJavaCal());
	}

	public void adjustSecond(final int n) {
		adjustSecond(n, true);
	}

	public void adjustMinute(final int n) {
		adjustMinute(n, true);
	}

	public void adjustHour(final int n) {
		adjustHour(n, true);
	}

	public void adjustDay(final int n) {
		adjustDay(n, true);
	}

	public void adjustMonth(final int n) {
		adjustMonth(n, true);
	}

	public void adjustYear(final int n) {
		adjustYear(n, true);

	}

	public void adjustSecond(final int n, final boolean preserveLocalTime) {
		instance.set(Calendar.SECOND, n);
	}

	public void adjustMinute(final int n, final boolean preserveLocalTime) {
		instance.set(Calendar.MINUTE, n);

	}

	public void adjustHour(final int n, final boolean preserveLocalTime) {
		instance.set(Calendar.HOUR_OF_DAY, n);
	}

	public void adjustDay(final int n, final boolean preserveLocalTime) {
		instance.set(Calendar.DAY_OF_MONTH, n);
	}

	public void adjustMonth(final int n, final boolean preserveLocalTime) {
		instance.set(Calendar.MONTH, n - 1);
	}

	public void adjustYear(final int n, final boolean preserveLocalTime) {
		instance.set(Calendar.YEAR, n);

	}

	public void convertToZone(final int zone, final boolean isDST) {
		// TODO Auto-generated method stub

	}

	public boolean equals(final DateTime comparDate) {
		return instance.equals(comparDate.toJavaCal());
	}

	public boolean equalsIgnoreDate(final DateTime comparDate) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean equalsIgnoreTime(final DateTime comparDate) {
		// TODO Auto-generated method stub
		return false;
	}

	public String getDateOnly() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getGMTTime() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getLocalTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Deprecated
	public Session getParent() {
		throw new UnsupportedOperationException();
	}

	public String getTimeOnly() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getTimeZone() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getZoneTime() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isDST() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setAnyDate() {
		anyDate = true;
		// TODO Auto-generated method stub

	}

	public void setAnyTime() {
		anyTime = true;
		// TODO Auto-generated method stub
	}

	public void setLocalDate(final int year, final int month, final int day) {
		setLocalDate(year, month, day, true);
	}

	public void setLocalDate(final int year, final int month, final int day, final boolean preserveLocalTime) {
		instance.set(year, month - 1, day);
	}

	public void setLocalTime(final java.util.Calendar calendar) {
		instance = (Calendar) calendar.clone();
	}

	public void setLocalTime(final Date date) {
		instance.setTime(date);
	}

	public void setLocalTime(final int hour, final int minute, final int second, final int hundredth) {
		instance.set(Calendar.HOUR_OF_DAY, hour);
		instance.set(Calendar.MINUTE, minute);
		instance.set(Calendar.SECOND, second);
		instance.set(Calendar.MILLISECOND, hundredth * 10);

	}

	public void setLocalTime(final String time) {
		// TODO Auto-generated method stub

	}

	public void setNow() {
		instance = Calendar.getInstance();
	}

	public int timeDifference(final lotus.domino.DateTime dt) {
		if (dt instanceof org.openntf.domino.DateTime) {
			Calendar other = ((org.openntf.domino.DateTime) dt).toJavaCal();
			return (int) ((instance.getTimeInMillis() - other.getTimeInMillis()) / 1000);
		}
		throw new UnsupportedOperationException("Cannot work on " + dt.getClass());
	}

	public double timeDifferenceDouble(final lotus.domino.DateTime dt) {
		if (dt instanceof org.openntf.domino.DateTime) {
			Calendar other = ((org.openntf.domino.DateTime) dt).toJavaCal();
			return (double) ((instance.getTimeInMillis() - other.getTimeInMillis()) / 1000);
		}
		throw new UnsupportedOperationException("Cannot work on " + dt.getClass());
	}

	public Date toJavaDate() {
		return instance.getTime();
	}

	public void recycle() throws NotesException {
		// TODO Auto-generated method stub

	}

	public void recycle(final Vector arg0) throws NotesException {
		// TODO Auto-generated method stub

	}

	public boolean isDead() {
		// TODO Auto-generated method stub
		return false;
	}

	public void addListener(final IDominoListener listener) {
		// TODO Auto-generated method stub

	}

	public void removeListener(final IDominoListener listener) {
		// TODO Auto-generated method stub

	}

	public List<IDominoListener> getListeners() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<IDominoListener> getListeners(final EnumEvent event) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean fireListener(final IDominoEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub

	}

	public void writeExternal(final ObjectOutput out) throws IOException {
		// TODO Auto-generated method stub

	}

	public Session getAncestorSession() {
		// TODO Auto-generated method stub
		return null;
	}

	public int timeDifference(final ISimpleDateTime dt) {
		// TODO Auto-generated method stub
		return 0;
	}

	public double timeDifferenceDouble(final ISimpleDateTime dt) {
		// TODO Auto-generated method stub
		return 0;
	}

}
