package org.openntf.domino.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import lotus.domino.NotesException;

import org.openntf.domino.Session;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class DateTime extends Base<org.openntf.domino.DateTime, lotus.domino.DateTime> implements org.openntf.domino.DateTime {
	private final Calendar cal_ = GregorianCalendar.getInstance();
	private boolean dst_;
	private boolean isDateOnly_;
	private boolean isTimeOnly_;
	private int notesZone_;

	public DateTime(lotus.domino.DateTime delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, Factory.getSession(parent));
		initialize(delegate);
		org.openntf.domino.impl.Base.recycle(delegate);
	}

	public DateTime(Date date, org.openntf.domino.Base<?> parent) {
		super(null, Factory.getSession(parent));
		initialize(date);
	}

	public DateTime(Date date) {
		super(null, null);
		initialize(date);
	}

	private void initialize(java.util.Date date) {
		cal_.setTime(date);
		dst_ = false;
		notesZone_ = 0;
	}

	private void initialize(lotus.domino.DateTime delegate) {
		try {
			dst_ = delegate.isDST();
			notesZone_ = delegate.getTimeZone();
			String dateonly = delegate.getDateOnly();
			if (dateonly == null || dateonly.length() == 0)
				isTimeOnly_ = true;
			String timeonly = delegate.getTimeOnly();
			if (timeonly == null || timeonly.length() == 0)
				isDateOnly_ = true;
			Date date = delegate.toJavaDate();
			cal_.setTime(date);

		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	public void adjustDay(int n, boolean preserveLocalTime) {
		try {
			getDelegate().adjustDay(n, preserveLocalTime);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void adjustDay(int n) {
		adjustDay(n, false);
	}

	public void adjustHour(int n, boolean preserveLocalTime) {
		try {
			getDelegate().adjustHour(n, preserveLocalTime);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void adjustHour(int n) {
		adjustHour(n, false);
	}

	public void adjustMinute(int n, boolean preserveLocalTime) {
		try {
			getDelegate().adjustMinute(n, preserveLocalTime);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void adjustMinute(int n) {
		adjustMinute(n, false);
	}

	public void adjustMonth(int n, boolean preserveLocalTime) {
		try {
			getDelegate().adjustMonth(n, preserveLocalTime);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void adjustMonth(int n) {
		adjustMonth(n, false);
	}

	public void adjustSecond(int n, boolean preserveLocalTime) {
		try {
			getDelegate().adjustSecond(n, preserveLocalTime);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void adjustSecond(int n) {
		adjustSecond(n, false);
	}

	public void adjustYear(int n, boolean preserveLocalTime) {
		try {
			getDelegate().adjustYear(n, preserveLocalTime);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void adjustYear(int n) {
		adjustYear(n, false);
	}

	public void convertToZone(int zone, boolean isDST) {
		try {
			getDelegate().convertToZone(zone, isDST);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public String getDateOnly() {
		return org.openntf.domino.impl.Session.getFormatter().getDateOnly(cal_.getTime());
	}

	public String getGMTTime() {
		return org.openntf.domino.impl.Session.getFormatter().getDateTime(cal_.getTime());
	}

	public String getLocalTime() {
		try {
			return getDelegate().getLocalTime();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public Session getParent() {
		return Factory.getSession(super.getParent());
	}

	public String getTimeOnly() {
		return org.openntf.domino.impl.Session.getFormatter().getTimeOnly(cal_.getTime());
	}

	public int getTimeZone() {
		try {
			return getDelegate().getTimeZone();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	public String getZoneTime() {
		try {
			return getDelegate().getZoneTime();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public boolean isDST() {
		try {
			return getDelegate().isDST();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public void setAnyDate() {
		try {
			getDelegate().setAnyDate();
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setAnyTime() {
		try {
			getDelegate().setAnyTime();
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setLocalDate(int year, int month, int day, boolean preserveLocalTime) {
		try {
			getDelegate().setLocalDate(year, month, day, preserveLocalTime);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setLocalDate(int year, int month, int day) {
		try {
			getDelegate().setLocalDate(year, month, day);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setLocalTime(Calendar calendar) {
		try {
			getDelegate().setLocalTime(calendar);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setLocalTime(Date date) {
		try {
			getDelegate().setLocalTime(date);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setLocalTime(int hour, int minute, int second, int hundredth) {
		try {
			getDelegate().setLocalTime(hour, minute, second, hundredth);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setLocalTime(String time) {
		try {
			getDelegate().setLocalTime(time);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setNow() {
		cal_.setTime(new Date());
	}

	public int timeDifference(lotus.domino.DateTime dt) {
		int result = 0;
		if (dt instanceof org.openntf.domino.impl.DateTime) {
			DateTime otherDT = (org.openntf.domino.impl.DateTime) dt; // so we can access private members directly.
			long left = cal_.getTimeInMillis() / 1000; // math is in seconds
			long right = otherDT.toJavaDate().getTime() / 1000; // math is also in seconds
			result = Long.valueOf(left - right).intValue();
		} else {
			long left = cal_.getTimeInMillis() / 1000; // math is in seconds
			long right = 0;
			try {
				right = dt.toJavaDate().getTime() / 1000; // math is also in seconds
			} catch (NotesException ne) {
				DominoUtils.handleException(ne);
			}
			result = Long.valueOf(left - right).intValue();
		}
		return result;
	}

	public double timeDifferenceDouble(lotus.domino.DateTime dt) {
		int i = this.timeDifference(dt);
		return Double.valueOf(i).doubleValue();
	}

	public Date toJavaDate() {
		return cal_.getTime();
	}
}
