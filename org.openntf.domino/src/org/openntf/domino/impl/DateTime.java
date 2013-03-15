/*
 * Copyright OpenNTF 2013
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
package org.openntf.domino.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import lotus.domino.NotesException;

import org.openntf.domino.Session;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class DateTime.
 */
public class DateTime extends Base<org.openntf.domino.DateTime, lotus.domino.DateTime> implements org.openntf.domino.DateTime {

	/** The cal_. */
	private final Calendar cal_ = GregorianCalendar.getInstance();

	/** The dst_. */
	private boolean dst_;

	/** The is date only_. */
	private boolean isDateOnly_;

	/** The is time only_. */
	private boolean isTimeOnly_;

	/** The notes zone_. */
	private int notesZone_;

	/**
	 * Instantiates a new date time.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	public DateTime(lotus.domino.DateTime delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, Factory.getSession(parent));
		initialize(delegate);
		org.openntf.domino.impl.Base.recycle(delegate);
	}

	/**
	 * Instantiates a new date time.
	 * 
	 * @param date
	 *            the date
	 * @param parent
	 *            the parent
	 */
	public DateTime(Date date, org.openntf.domino.Base<?> parent) {
		super(null, Factory.getSession(parent));
		initialize(date);
	}

	/**
	 * Instantiates a new date time.
	 * 
	 * @param date
	 *            the date
	 */
	public DateTime(Date date) {
		super(null, null);
		initialize(date);
	}

	/**
	 * Initialize.
	 * 
	 * @param date
	 *            the date
	 */
	private void initialize(java.util.Date date) {
		cal_.setTime(date);
		dst_ = false;
		notesZone_ = 0;
	}

	/**
	 * Initialize.
	 * 
	 * @param delegate
	 *            the delegate
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#adjustDay(int, boolean)
	 */
	public void adjustDay(int n, boolean preserveLocalTime) {
		try {
			getDelegate().adjustDay(n, preserveLocalTime);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#adjustDay(int)
	 */
	public void adjustDay(int n) {
		adjustDay(n, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#adjustHour(int, boolean)
	 */
	public void adjustHour(int n, boolean preserveLocalTime) {
		try {
			getDelegate().adjustHour(n, preserveLocalTime);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#adjustHour(int)
	 */
	public void adjustHour(int n) {
		adjustHour(n, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#adjustMinute(int, boolean)
	 */
	public void adjustMinute(int n, boolean preserveLocalTime) {
		try {
			getDelegate().adjustMinute(n, preserveLocalTime);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#adjustMinute(int)
	 */
	public void adjustMinute(int n) {
		adjustMinute(n, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#adjustMonth(int, boolean)
	 */
	public void adjustMonth(int n, boolean preserveLocalTime) {
		try {
			getDelegate().adjustMonth(n, preserveLocalTime);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#adjustMonth(int)
	 */
	public void adjustMonth(int n) {
		adjustMonth(n, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#adjustSecond(int, boolean)
	 */
	public void adjustSecond(int n, boolean preserveLocalTime) {
		try {
			getDelegate().adjustSecond(n, preserveLocalTime);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#adjustSecond(int)
	 */
	public void adjustSecond(int n) {
		adjustSecond(n, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#adjustYear(int, boolean)
	 */
	public void adjustYear(int n, boolean preserveLocalTime) {
		try {
			getDelegate().adjustYear(n, preserveLocalTime);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#adjustYear(int)
	 */
	public void adjustYear(int n) {
		adjustYear(n, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#convertToZone(int, boolean)
	 */
	public void convertToZone(int zone, boolean isDST) {
		try {
			getDelegate().convertToZone(zone, isDST);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#getDateOnly()
	 */
	public String getDateOnly() {
		return org.openntf.domino.impl.Session.getFormatter().getDateOnly(cal_.getTime());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#getGMTTime()
	 */
	public String getGMTTime() {
		return org.openntf.domino.impl.Session.getFormatter().getDateTime(cal_.getTime());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#getLocalTime()
	 */
	public String getLocalTime() {
		try {
			return getDelegate().getLocalTime();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public Session getParent() {
		return Factory.getSession(super.getParent());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#getTimeOnly()
	 */
	public String getTimeOnly() {
		return org.openntf.domino.impl.Session.getFormatter().getTimeOnly(cal_.getTime());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#getTimeZone()
	 */
	public int getTimeZone() {
		try {
			return getDelegate().getTimeZone();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#getZoneTime()
	 */
	public String getZoneTime() {
		try {
			return getDelegate().getZoneTime();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#isDST()
	 */
	public boolean isDST() {
		try {
			return getDelegate().isDST();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#setAnyDate()
	 */
	public void setAnyDate() {
		try {
			getDelegate().setAnyDate();
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#setAnyTime()
	 */
	public void setAnyTime() {
		try {
			getDelegate().setAnyTime();
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#setLocalDate(int, int, int, boolean)
	 */
	public void setLocalDate(int year, int month, int day, boolean preserveLocalTime) {
		try {
			getDelegate().setLocalDate(year, month, day, preserveLocalTime);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#setLocalDate(int, int, int)
	 */
	public void setLocalDate(int year, int month, int day) {
		try {
			getDelegate().setLocalDate(year, month, day);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#setLocalTime(java.util.Calendar)
	 */
	public void setLocalTime(Calendar calendar) {
		try {
			getDelegate().setLocalTime(calendar);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#setLocalTime(java.util.Date)
	 */
	public void setLocalTime(Date date) {
		try {
			getDelegate().setLocalTime(date);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#setLocalTime(int, int, int, int)
	 */
	public void setLocalTime(int hour, int minute, int second, int hundredth) {
		try {
			getDelegate().setLocalTime(hour, minute, second, hundredth);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#setLocalTime(java.lang.String)
	 */
	public void setLocalTime(String time) {
		try {
			getDelegate().setLocalTime(time);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#setNow()
	 */
	public void setNow() {
		cal_.setTime(new Date());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#timeDifference(lotus.domino.DateTime)
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#timeDifferenceDouble(lotus.domino.DateTime)
	 */
	public double timeDifferenceDouble(lotus.domino.DateTime dt) {
		int i = this.timeDifference(dt);
		return Double.valueOf(i).doubleValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#toJavaDate()
	 */
	public Date toJavaDate() {
		return cal_.getTime();
	}

}
