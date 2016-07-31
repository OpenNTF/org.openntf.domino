/*
 * Copyright 2013
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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.GregorianCalendar;

// TODO: Auto-generated Javadoc
/**
 * The Class DateTime.
 */
public class DateTime extends BaseNonThreadSafe<org.openntf.domino.DateTime, lotus.domino.DateTime, Session>
		implements org.openntf.domino.DateTime {
	private static final Logger log_ = Logger.getLogger(DateTime.class.getName());
	private static final long serialVersionUID = 1L;

	static {
		Factory.addTerminateHook(new Runnable() {
			@Override
			public void run() {
				Base.s_recycle(lotusWorker.get());
				lotusWorker.set(null);
				calendar.set(null);
			}
		}, true);
	}

	/** The calendar */
	private static ThreadLocal<Calendar> calendar = new ThreadLocal<Calendar>() {
		@Override
		public Calendar get() {
			if (super.get() == null) {
				set(GregorianCalendar.getInstance());
			}
			return super.get();
		};
	};

	private Date date_;

	/** The dst_. */
	private boolean dst_;

	/** The is date only_. */
	private boolean isDateOnly_;

	/** The is time only_. */
	private boolean isTimeOnly_;

	/** The notes zone_. */
	private int notesZone_ = Integer.MIN_VALUE;

	private static final ThreadLocal<lotus.domino.DateTime> lotusWorker = new ThreadLocal<lotus.domino.DateTime>();

	private static lotus.domino.DateTime generateWorker() {
		try {
			lotus.domino.Session rawsession = toLotus(Factory.getSession(SessionType.CURRENT));
			if (rawsession == null) {
				// Then fall back to getting a native session
				// This may occur in OSGi servlet contexts
				rawsession = toLotus(Factory.getSession(SessionType.NATIVE));
			}
			return rawsession.createDateTime(new Date());
		} catch (Exception e) {
			log_.log(Level.SEVERE, "Could not create the DateTime worker object. DateTime functions may not work as expected", e);
			return null;
		}

	};

	private lotus.domino.DateTime getWorker() throws NotesException {
		lotus.domino.DateTime ret = lotusWorker.get();

		if (ret == null || Base.isDead(ret)) {
			ret = generateWorker();
			lotusWorker.set(ret);
		}
		if (date_ != null) {
			initWorker(ret);
		}
		return ret;
	}

	private void initWorker(final lotus.domino.DateTime worker) throws NotesException {
		if (date_ == null) {
			return;
		}
		worker.setLocalTime(date_);
		if (!isTimeOnly_ && !isDateOnly_) {
			worker.convertToZone(notesZone_, dst_);
		}
		if (isTimeOnly_) {
			worker.setAnyDate();
		}
		if (isDateOnly_) {
			worker.setAnyTime();
		}
	}

	private void workDone(final lotus.domino.DateTime worker, final boolean reInit) {
		if (reInit) {
			this.initialize(worker);
		}
	}

	/**
	 * Instantiates a new date time.
	 *
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 * @param wf
	 *            the wrapperfactory
	 * @param cppId
	 *            the cpp-id
	 */
	protected DateTime(final lotus.domino.DateTime delegate, final Session parent) {
		super(delegate, parent, NOTES_TIME);
		initialize(delegate);
		// TODO: Wrapping recycles the caller's object. This may cause issues.
		Base.s_recycle(delegate);
	}

	/**
	 * Needed for clone
	 *
	 * @param dateTime
	 */
	protected DateTime(final DateTime orig, final Session sess) {
		super(null, sess, NOTES_TIME);
		dst_ = orig.dst_;
		isDateOnly_ = orig.isDateOnly_;
		isTimeOnly_ = orig.isTimeOnly_;
		notesZone_ = orig.notesZone_;
		if (notesZone_ == -18000000) {
			Throwable t = new Throwable();
			t.printStackTrace();
		}
		if (orig.date_ != null) {
			date_ = new Date(orig.date_.getTime());
		}
	}

	/**
	 * Clones the DateTime object.
	 */
	@Override
	public org.openntf.domino.DateTime clone() {
		return new DateTime(this, getAncestorSession());
	}

	/*
	 * The returned object MUST get recycled
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.impl.Base#getDelegate()
	 */
	@Override
	protected lotus.domino.DateTime getDelegate() {
		try {
			lotus.domino.Session rawsession = toLotus(parent);
			lotus.domino.DateTime delegate = rawsession.createDateTime(date_);
			if (notesZone_ != Integer.MIN_VALUE) {
				try {
					delegate.convertToZone(notesZone_, dst_);
				} catch (Throwable t) {
					log_.log(Level.WARNING,
							"Failed to convert a DateTime to zone " + notesZone_ + " with a dst of " + String.valueOf(dst_));
				}
			}
			if (isAnyTime()) {
				delegate.setAnyTime();
			}
			if (isAnyDate()) {
				delegate.setAnyDate();
			}
			return delegate;
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return null;
		}
	}

	/**
	 * Initialize.
	 *
	 * @param delegate
	 *            the delegate
	 */
	private void initialize(final lotus.domino.DateTime delegate) {
		try {
			dst_ = delegate.isDST();
			notesZone_ = delegate.getTimeZone();
			if (notesZone_ == -18000000) {
				Throwable t = new Throwable();
				t.printStackTrace();
			}
			String s = delegate.getDateOnly();
			isTimeOnly_ = (s == null || s.length() == 0);
			s = delegate.getTimeOnly();
			isDateOnly_ = (s == null || s.length() == 0);
			try {
				if (isTimeOnly_ && isDateOnly_) {
					date_ = null;
				} else {
					date_ = delegate.toJavaDate();
				}
			} catch (NotesException e1) {
				// System.out.println("Error attempting to initialize a DateTime: " + delegate.getGMTTime());
				throw new RuntimeException(e1);
			}

		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DateTime#adjustDay(int, boolean)
	 */
	@Override
	public void adjustDay(final int n, final boolean preserveLocalTime) {
		try {
			lotus.domino.DateTime worker = getWorker();
			worker.adjustDay(n, preserveLocalTime);
			workDone(worker, true);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DateTime#adjustDay(int)
	 */
	@Override
	public void adjustDay(final int n) {
		adjustDay(n, false);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DateTime#adjustHour(int, boolean)
	 */
	@Override
	public void adjustHour(final int n, final boolean preserveLocalTime) {
		try {
			lotus.domino.DateTime worker = getWorker();
			worker.adjustHour(n, preserveLocalTime);
			workDone(worker, true);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DateTime#adjustHour(int)
	 */
	@Override
	public void adjustHour(final int n) {
		adjustHour(n, false);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DateTime#adjustMinute(int, boolean)
	 */
	@Override
	public void adjustMinute(final int n, final boolean preserveLocalTime) {
		try {
			lotus.domino.DateTime worker = getWorker();
			worker.adjustMinute(n, preserveLocalTime);
			workDone(worker, true);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DateTime#adjustMinute(int)
	 */
	@Override
	public void adjustMinute(final int n) {
		adjustMinute(n, false);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DateTime#adjustMonth(int, boolean)
	 */
	@Override
	public void adjustMonth(final int n, final boolean preserveLocalTime) {
		try {
			lotus.domino.DateTime worker = getWorker();
			worker.adjustMonth(n, preserveLocalTime);
			workDone(worker, true);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DateTime#adjustMonth(int)
	 */
	@Override
	public void adjustMonth(final int n) {
		adjustMonth(n, false);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DateTime#adjustSecond(int, boolean)
	 */
	@Override
	public void adjustSecond(final int n, final boolean preserveLocalTime) {
		try {
			lotus.domino.DateTime worker = getWorker();
			worker.adjustSecond(n, preserveLocalTime);
			workDone(worker, true);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DateTime#adjustSecond(int)
	 */
	@Override
	public void adjustSecond(final int n) {
		adjustSecond(n, false);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DateTime#adjustYear(int, boolean)
	 */
	@Override
	public void adjustYear(final int n, final boolean preserveLocalTime) {
		try {
			lotus.domino.DateTime worker = getWorker();
			worker.adjustYear(n, preserveLocalTime);
			workDone(worker, true);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DateTime#adjustYear(int)
	 */
	@Override
	public void adjustYear(final int n) {
		adjustYear(n, false);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DateTime#convertToZone(int, boolean)
	 */
	@Override
	public void convertToZone(final int zone, final boolean isDST) {
		try {
			lotus.domino.DateTime worker = getWorker();
			worker.convertToZone(zone, isDST);
			workDone(worker, true);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
		// TODO NTF - find out what this actually does. The documentation is... vague
		//throw new UnimplementedException("convertToZone is not yet implemented.");
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.DateTime#equals(org.openntf.domino.DateTime)
	 */
	@Override
	public boolean equals(final org.openntf.domino.DateTime compareDate) {
		return date_.equals(compareDate.toJavaDate());
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.DateTime#equalsIgnoreDate(org.openntf.domino.DateTime)
	 */
	@Override
	public boolean equalsIgnoreDate(final org.openntf.domino.DateTime compareDate) {
		Calendar cal = calendar.get();
		cal.setTime(date_);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.YEAR, 2000);
		Date d1 = cal.getTime();
		cal.setTime(compareDate.toJavaDate());
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.YEAR, 2000);
		Date d2 = cal.getTime();
		return d1.equals(d2);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.DateTime#equalsIgnoreTime(org.openntf.domino.DateTime)
	 */
	@Override
	public boolean equalsIgnoreTime(final org.openntf.domino.DateTime compareDate) {
		Calendar cal = calendar.get();
		cal.setTime(date_);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date d1 = cal.getTime();
		cal.setTime(compareDate.toJavaDate());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date d2 = cal.getTime();
		return d1.equals(d2);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DateTime#getDateOnly()
	 */
	@Override
	public String getDateOnly() {
		String ret = null;
		try {
			lotus.domino.DateTime worker = getWorker();
			ret = worker.getDateOnly();
			workDone(worker, false);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DateTime#getGMTTime()
	 */
	@Override
	public String getGMTTime() {
		String ret = null;
		try {
			lotus.domino.DateTime worker = getWorker();
			ret = worker.getGMTTime();
			workDone(worker, false);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DateTime#getLocalTime()
	 */
	@Override
	public String getLocalTime() {
		String ret = null;
		try {
			lotus.domino.DateTime worker = getWorker();
			ret = worker.getLocalTime();
			workDone(worker, false);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public final Session getParent() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DateTime#getTimeOnly()
	 */
	@Override
	public String getTimeOnly() {
		String ret = null;
		try {
			lotus.domino.DateTime worker = getWorker();
			ret = worker.getTimeOnly();
			workDone(worker, false);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DateTime#getTimeZone()
	 */
	@Override
	public int getTimeZone() {
		return notesZone_;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DateTime#getZoneTime()
	 */
	@Override
	public String getZoneTime() {
		// TODO NTF - find out what this really does
		String ret = null;
		try {
			lotus.domino.DateTime worker = getWorker();
			ret = worker.getZoneTime();
			workDone(worker, false);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
		return ret;
		// throw new UnimplementedException("getZoneTime is not yet implemented.");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DateTime#isAfter(org.openntf.domino.DateTime)
	 */
	@Override
	public boolean isAfter(final org.openntf.domino.DateTime compareDate) {
		return date_.after(compareDate.toJavaDate());
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.DateTime#isAfterIgnoreDate(org.openntf.domino.DateTime)
	 */
	@Override
	public boolean isAfterIgnoreDate(final org.openntf.domino.DateTime compareDate) {
		Calendar cal = calendar.get();
		cal.setTime(date_);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.YEAR, 2000);
		Date d1 = cal.getTime();
		cal.setTime(compareDate.toJavaDate());
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.YEAR, 2000);
		Date d2 = cal.getTime();
		return d1.after(d2);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.DateTime#isAfterIgnoreTime(org.openntf.domino.DateTime)
	 */
	@Override
	public boolean isAfterIgnoreTime(final org.openntf.domino.DateTime compareDate) {
		Calendar cal = calendar.get();
		cal.setTime(date_);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date d1 = cal.getTime();
		cal.setTime(compareDate.toJavaDate());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date d2 = cal.getTime();
		return d1.after(d2);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DateTime#isBefore(org.openntf.domino.DateTime)
	 */
	@Override
	public boolean isBefore(final org.openntf.domino.DateTime compareDate) {
		return date_.before(compareDate.toJavaDate());
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.DateTime#isBeforeIgnoreDate(org.openntf.domino.DateTime)
	 */
	@Override
	public boolean isBeforeIgnoreDate(final org.openntf.domino.DateTime compareDate) {
		Calendar cal = calendar.get();
		cal.setTime(date_);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.YEAR, 2000);
		Date d1 = cal.getTime();
		cal.setTime(compareDate.toJavaDate());
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.YEAR, 2000);
		Date d2 = cal.getTime();
		return d1.before(d2);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.DateTime#isBeforeIgnoreTime(org.openntf.domino.DateTime)
	 */
	@Override
	public boolean isBeforeIgnoreTime(final org.openntf.domino.DateTime compareDate) {
		Calendar cal = calendar.get();
		cal.setTime(date_);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date d1 = cal.getTime();
		cal.setTime(compareDate.toJavaDate());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date d2 = cal.getTime();
		return d1.before(d2);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DateTime#isDST()
	 */
	@Override
	public boolean isDST() {
		return dst_;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DateTime#setAnyDate()
	 */
	@Override
	public void setAnyDate() {
		isTimeOnly_ = true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DateTime#setAnyTime()
	 */
	@Override
	public void setAnyTime() {
		isDateOnly_ = true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DateTime#setLocalDate(int, int, int, boolean)
	 */
	@Override
	public void setLocalDate(final int year, final int month, final int day, final boolean preserveLocalTime) {
		try {
			lotus.domino.DateTime worker = getWorker();
			worker.setLocalDate(year, month, day, preserveLocalTime);
			workDone(worker, true);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DateTime#setLocalDate(int, int, int)
	 */
	@Override
	public void setLocalDate(final int year, final int month, final int day) {
		setLocalDate(year, month, day, false);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DateTime#setLocalTime(java.util.Calendar)
	 */
	@Override
	public void setLocalTime(final java.util.Calendar calendar) {
		date_ = calendar.getTime();
		//FIXME NTF this is incorrect. The notesZone_ is the timezone according to the legacy Notes API. The Java calendar API cannot replace it.
		//		java.util.TimeZone localTimeZone = calendar.getTimeZone();
		//		notesZone_ = calendar.get(Calendar.ZONE_OFFSET);
		//		if (localTimeZone.useDaylightTime() == true) {
		//			dst_ = localTimeZone.inDaylightTime(date_);
		//		} else {
		//			dst_ = false;
		//		}
		isDateOnly_ = false;
		isTimeOnly_ = false;
		//setLocalTime(calendar.getTime());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.ext.DateTime#setLocalTime(com.ibm.icu.util.Calendar)
	 */
	@Override
	public void setLocalTime(final Calendar calendar) {
		date_ = calendar.getTime();
		//FIXME NTF this is incorrect. The notesZone_ is the timezone according to the legacy Notes API. The Java calendar API cannot replace it.
		//		TimeZone localTimeZone = calendar.getTimeZone();
		//		notesZone_ = calendar.get(Calendar.ZONE_OFFSET);
		//		if (localTimeZone.useDaylightTime() == true) {
		//			dst_ = localTimeZone.inDaylightTime(date_);
		//		} else {
		//			dst_ = false;
		//		}
		isDateOnly_ = false;
		isTimeOnly_ = false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DateTime#setLocalTime(java.util.Date)
	 */
	@Override
	public void setLocalTime(final Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		setLocalTime(cal);
		//		try {
		//			lotus.domino.DateTime worker = getWorker();
		//			worker.setLocalTime(date);
		//			workDone(worker, true);
		//		} catch (NotesException ne) {
		//			DominoUtils.handleException(ne);
		//		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DateTime#setLocalTime(int, int, int, int)
	 */
	@Override
	public void setLocalTime(final int hour, final int minute, final int second, final int hundredth) {
		try {
			lotus.domino.DateTime worker = getWorker();
			worker.setLocalTime(hour, minute, second, hundredth);
			workDone(worker, true);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DateTime#setLocalTime(java.lang.String)
	 */
	@Override
	public void setLocalTime(final String time) {
		try {
			lotus.domino.DateTime worker = getWorker();
			worker.setLocalTime(time);
			workDone(worker, true);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DateTime#setNow()
	 */
	@Override
	public void setNow() {
		setLocalTime(new Date());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DateTime#timeDifference(lotus.domino.DateTime)
	 */
	@Override
	public int timeDifference(final lotus.domino.DateTime dt) {
		Integer[] res = new Integer[1];
		res[0] = new Integer(0);
		timeDifferenceCommon(dt, res);
		return (res[0]);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DateTime#timeDifferenceDouble(lotus.domino.DateTime)
	 */
	@Override
	public double timeDifferenceDouble(final lotus.domino.DateTime dt) {
		Double[] res = new Double[1];
		res[0] = new Double(0);
		timeDifferenceCommon(dt, res);
		return (res[0]);
	}

	private void timeDifferenceCommon(final lotus.domino.DateTime dt, final Object[] res) {
		lotus.domino.DateTime dtLocal = dt;
		lotus.domino.DateTime lotusDTTmp = null;
		try {
			if (dtLocal instanceof org.openntf.domino.impl.DateTime) {
				lotusDTTmp = ((org.openntf.domino.impl.DateTime) dtLocal).getDelegate();
				dtLocal = lotusDTTmp;
			}
			lotus.domino.DateTime worker = getWorker();
			if (res[0] instanceof Integer) {
				res[0] = worker.timeDifference(dtLocal);
			} else if (res[0] instanceof Double) {
				res[0] = worker.timeDifferenceDouble(dtLocal);
			}
			workDone(worker, false);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		} finally {
			if (lotusDTTmp != null) {
				s_recycle(lotusDTTmp);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DateTime#toJavaDate()
	 */
	@Override
	public Date toJavaDate() {
		return new Date(date_.getTime());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String ret = null;
		try {
			lotus.domino.DateTime worker = getWorker();
			ret = worker.toString();
			workDone(worker, false);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public final Session getAncestorSession() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.ext.DateTime#isAnyDate()
	 */
	@Override
	public boolean isAnyDate() {
		return isTimeOnly_;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.ext.DateTime#isAnyTime()
	 */
	@Override
	public boolean isAnyTime() {
		return isDateOnly_;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.DateTime#toJavaCal()
	 */
	@Override
	public Calendar toJavaCal() {
		Calendar result = GregorianCalendar.getInstance();
		result.setTime(date_);
		return result;
	}

	@Override
	public int compareTo(final org.openntf.domino.DateTime arg0) {
		return date_.compareTo(arg0.toJavaDate());
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		dst_ = in.readBoolean();
		isDateOnly_ = in.readBoolean();
		isTimeOnly_ = in.readBoolean();
		notesZone_ = in.readInt();
		date_ = new Date(in.readLong());
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeBoolean(dst_);
		out.writeBoolean(isDateOnly_);
		out.writeBoolean(isTimeOnly_);
		out.writeInt(notesZone_);
		out.writeLong(date_.getTime());
	}

	/**
	 * @deprecated needed for {@link Externalizable} - do not use!
	 */
	@Deprecated
	public DateTime() {
		// it does not matter which session we use here, so we use the current one!
		super(null, Factory.getSession(SessionType.CURRENT), NOTES_TIME);
	}

	/**
	 * Constructs a new DateTime from the given String
	 *
	 * @param time
	 *            the time string in a notes readable format
	 * @throws java.text.ParseException
	 *             if the time string does not match
	 */
	protected DateTime(final Session parent) {
		super(null, parent, NOTES_TIME);
	}

	/*
	 * A few tiny methods needed for the org.openntf.domino.formula.DateTime interface
	 */
	@Override
	public int timeDifference(final org.openntf.formula.DateTime dt) {
		if (dt instanceof lotus.domino.DateTime) {
			return timeDifference((lotus.domino.DateTime) dt);
		}
		return (int) timeDifferenceDouble(dt);
	}

	@Override
	public double timeDifferenceDouble(final org.openntf.formula.DateTime dt) {
		if (dt instanceof lotus.domino.DateTime) {
			return timeDifferenceDouble((lotus.domino.DateTime) dt);
		}
		Calendar thisCal = this.toJavaCal();
		Calendar thatCal = dt.toJavaCal();
		return (thisCal.getTimeInMillis() - thatCal.getTimeInMillis()) * 1000;
	}

	@Override
	public int compare(final org.openntf.formula.DateTime sdt1, final org.openntf.formula.DateTime sdt2) {
		if (sdt1 instanceof DateTime && sdt2 instanceof DateTime) {
			return ((DateTime) sdt1).compareTo((DateTime) sdt2);
		}
		if (sdt1 instanceof DateTime) {
			return sdt2.compare(sdt2, sdt1);
		}
		return sdt1.compare(sdt1, sdt2);
	}

	@Override
	public void setLocalTime(final String time, final boolean parseLenient) {
		setLocalTime(time);
	}

	@Override
	protected WrapperFactory getFactory() {
		return parent.getFactory();
	}

}
