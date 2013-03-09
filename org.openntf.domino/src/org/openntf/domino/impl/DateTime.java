package org.openntf.domino.impl;

import java.util.Calendar;
import java.util.Date;

import lotus.domino.NotesException;

import org.openntf.domino.Session;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class DateTime extends Base<org.openntf.domino.DateTime, lotus.domino.DateTime> implements org.openntf.domino.DateTime {
	lotus.domino.DateTime temp_;

	public DateTime(lotus.domino.DateTime delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, (parent instanceof org.openntf.domino.Session) ? parent : Factory.getSession(parent));
	}

	public void adjustDay(int arg0, boolean arg1) {
		try {
			getDelegate().adjustDay(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void adjustDay(int arg0) {
		try {
			getDelegate().adjustDay(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void adjustHour(int arg0, boolean arg1) {
		try {
			getDelegate().adjustHour(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void adjustHour(int arg0) {
		try {
			getDelegate().adjustHour(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void adjustMinute(int arg0, boolean arg1) {
		try {
			getDelegate().adjustMinute(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void adjustMinute(int arg0) {
		try {
			getDelegate().adjustMinute(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void adjustMonth(int arg0, boolean arg1) {
		try {
			getDelegate().adjustMonth(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void adjustMonth(int arg0) {
		try {
			getDelegate().adjustMonth(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void adjustSecond(int arg0, boolean arg1) {
		try {
			getDelegate().adjustSecond(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void adjustSecond(int arg0) {
		try {
			getDelegate().adjustSecond(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void adjustYear(int arg0, boolean arg1) {
		try {
			getDelegate().adjustYear(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void adjustYear(int arg0) {
		try {
			getDelegate().adjustYear(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void convertToZone(int arg0, boolean arg1) {
		try {
			getDelegate().convertToZone(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public String getDateOnly() {
		try {
			return getDelegate().getDateOnly();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getGMTTime() {
		try {
			return getDelegate().getGMTTime();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
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
		return (org.openntf.domino.Session) super.getParent();
	}

	public String getTimeOnly() {
		try {
			return getDelegate().getTimeOnly();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
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
