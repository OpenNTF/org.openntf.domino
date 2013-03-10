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

	public void adjustDay(int arg0, boolean arg1) {
		try {
			getDelegate().adjustDay(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void adjustDay(int arg0) {
		adjustDay(arg0, false);
	}

	public void adjustHour(int arg0, boolean arg1) {
		try {
			getDelegate().adjustHour(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void adjustHour(int arg0) {
		adjustHour(arg0, false);
	}

	public void adjustMinute(int arg0, boolean arg1) {
		try {
			getDelegate().adjustMinute(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void adjustMinute(int arg0) {
		adjustMinute(arg0, false);
	}

	public void adjustMonth(int arg0, boolean arg1) {
		try {
			getDelegate().adjustMonth(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void adjustMonth(int arg0) {
		adjustMonth(arg0, false);
	}

	public void adjustSecond(int arg0, boolean arg1) {
		try {
			getDelegate().adjustSecond(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void adjustSecond(int arg0) {
		adjustSecond(arg0, false);
	}

	public void adjustYear(int arg0, boolean arg1) {
		try {
			getDelegate().adjustYear(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void adjustYear(int arg0) {
		adjustYear(arg0, false);
	}

	public void convertToZone(int arg0, boolean arg1) {
		try {
			getDelegate().convertToZone(arg0, arg1);
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

	public void setLocalDate(int arg0, int arg1, int arg2, boolean arg3) {
		try {
			getDelegate().setLocalDate(arg0, arg1, arg2, arg3);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setLocalDate(int arg0, int arg1, int arg2) {
		try {
			getDelegate().setLocalDate(arg0, arg1, arg2);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setLocalTime(Calendar arg0) {
		try {
			getDelegate().setLocalTime(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setLocalTime(Date arg0) {
		try {
			getDelegate().setLocalTime(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setLocalTime(int arg0, int arg1, int arg2, int arg3) {
		try {
			getDelegate().setLocalTime(arg0, arg1, arg2, arg3);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setLocalTime(String arg0) {
		try {
			getDelegate().setLocalTime(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setNow() {
		try {
			getDelegate().setNow();
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public int timeDifference(lotus.domino.DateTime arg0) {
		try {
			return getDelegate().timeDifference(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	public double timeDifferenceDouble(lotus.domino.DateTime arg0) {
		try {
			return getDelegate().timeDifferenceDouble(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0d;

		}
	}

	public Date toJavaDate() {
		try {
			return getDelegate().toJavaDate();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

}
