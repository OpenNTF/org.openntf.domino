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

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;

import lotus.domino.NotesException;

import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.GregorianCalendar;

// TODO: Auto-generated Javadoc
/**
 * The Class DateTime.
 */
public class DateTime extends Base<org.openntf.domino.DateTime, lotus.domino.DateTime, Session> implements org.openntf.domino.DateTime {
	//private static final Logger log_ = Logger.getLogger(DateTime.class.getName());
	private static final long serialVersionUID = 1L;

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

	private static final ThreadLocal<lotus.domino.DateTime> lotusWorker = new ThreadLocal<lotus.domino.DateTime>() {
		@Override
		protected lotus.domino.DateTime initialValue() {
			return (generateWorker());
		}
	};

	private static lotus.domino.DateTime generateWorker() {
		lotus.domino.DateTime ret;
		try {
			lotus.domino.Session rawsession = toLotus(Factory.getSession());
			ret = rawsession.createDateTime(new Date());
		} catch (Exception e) {
			ret = null;
		}
		return ret;
	};

	private lotus.domino.DateTime getWorker() throws NotesException {
		lotus.domino.DateTime ret = lotusWorker.get();
		if (Base.isDead(ret)) {
			lotusWorker.set(generateWorker());
			ret = lotusWorker.get();
		}
		initWorker(ret);
		return ret;
	}

	private void initWorker(final lotus.domino.DateTime worker) throws NotesException {
		worker.setLocalTime(cal_.getTime());
		worker.convertToZone(notesZone_, dst_);
		if (isTimeOnly_)
			worker.setAnyDate();
		if (isDateOnly_)
			worker.setAnyTime();
	}

	private void workDone(final lotus.domino.DateTime worker, final boolean reInit) {
		if (reInit)
			this.initialize(worker);
	}

	//	/**
	//	 * Instantiates a new date time.
	//	 * 
	//	 * @param delegate
	//	 *            the delegate
	//	 * @param parent
	//	 *            the parent
	//	 */
	//	@Deprecated
	//	public DateTime(final lotus.domino.DateTime delegate, final org.openntf.domino.Base<?> parent) {
	//		super(null, Factory.getSession(parent));
	//		if (delegate instanceof lotus.domino.local.DateTime) {
	//			initialize(delegate);
	//			Base.s_recycle(delegate);
	//		}
	//	}

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
	public DateTime(final lotus.domino.DateTime delegate, final Session parent, final WrapperFactory wf, final long cppId) {
		super(delegate, parent, wf, cppId, NOTES_TIME);
		initialize(delegate);
		// TODO: Wrapping recycles the caller's object. This may cause issues.
		Base.s_recycle(delegate);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.impl.Base#findParent(lotus.domino.Base)
	 */
	@Override
	protected Session findParent(final lotus.domino.DateTime delegate) {
		if (delegate == null) {
			return Factory.getSession(); // the current Session
		}
		return fromLotus(Base.getSession(delegate), Session.SCHEMA, null);
	}

	/**
	 * Instantiates a new date time.
	 * 
	 * @param date
	 *            the date
	 * @param parent
	 *            the parent
	 * @param wf
	 *            the wrapperfactory
	 * @param cppId
	 *            the cpp-id
	 */
	public DateTime(final Date date, final Session parent, final WrapperFactory wf, final long cppId) {
		super(null, parent, wf, cppId, NOTES_TIME);
		initialize(date);
	}

	//	/**
	//	 * Instantiates a new date time.
	//	 * 
	//	 * @param date
	//	 *            the date
	//	 */
	//	@Deprecated
	//	public DateTime(final Date date) {
	//		super(null, null);
	//		initialize(date);
	//	}
	//
	//	@Deprecated
	//	public DateTime() {	// for deserialization?
	//		super(null, null);
	//	}

	/*
	 * The returned object MUST get recycled
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.impl.Base#getDelegate()
	 */
	@Override
	protected lotus.domino.DateTime getDelegate() {
		try {
			lotus.domino.Session rawsession = toLotus(Factory.getSession(getParent()));
			lotus.domino.DateTime ret = rawsession.createDateTime(cal_.getTime());
			ret.convertToZone(notesZone_, dst_);
			if (isTimeOnly_)
				ret.setAnyDate();
			if (isDateOnly_)
				ret.setAnyTime();
			return ret;
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return null;
		}
	}

	/**
	 * Initialize.
	 * 
	 * @param date
	 *            the date
	 */
	private void initialize(final java.util.Date date) {
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
	private void initialize(final lotus.domino.DateTime delegate) {
		try {
			dst_ = delegate.isDST();
			notesZone_ = delegate.getTimeZone();
			String s = delegate.getDateOnly();
			isTimeOnly_ = (s == null || s.length() == 0);
			s = delegate.getTimeOnly();
			isDateOnly_ = (s == null || s.length() == 0);
			try {
				Date date = delegate.toJavaDate();
				cal_.setTime(date);
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
	public void adjustDay(final int n, final boolean preserveLocalTime) {
		try {
			lotus.domino.DateTime worker = getWorker();
			worker.adjustDay(n, preserveLocalTime);
			workDone(worker, true);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
		// cal_.add(Calendar.DAY_OF_MONTH, n); // TODO NTF - figure out what preserveLocalTime *REALLY* does
		// getDelegate().adjustDay(n, preserveLocalTime);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#adjustDay(int)
	 */
	public void adjustDay(final int n) {
		adjustDay(n, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#adjustHour(int, boolean)
	 */
	public void adjustHour(final int n, final boolean preserveLocalTime) {
		try {
			lotus.domino.DateTime worker = getWorker();
			worker.adjustHour(n, preserveLocalTime);
			workDone(worker, true);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
		// cal_.add(Calendar.HOUR_OF_DAY, n); // TODO flag
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#adjustHour(int)
	 */
	public void adjustHour(final int n) {
		adjustHour(n, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#adjustMinute(int, boolean)
	 */
	public void adjustMinute(final int n, final boolean preserveLocalTime) {
		try {
			lotus.domino.DateTime worker = getWorker();
			worker.adjustMinute(n, preserveLocalTime);
			workDone(worker, true);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
		// cal_.add(Calendar.MINUTE, n); // TODO flag
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#adjustMinute(int)
	 */
	public void adjustMinute(final int n) {
		adjustMinute(n, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#adjustMonth(int, boolean)
	 */
	public void adjustMonth(final int n, final boolean preserveLocalTime) {
		try {
			lotus.domino.DateTime worker = getWorker();
			worker.adjustMonth(n, preserveLocalTime);
			workDone(worker, true);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
		// cal_.add(Calendar.MONTH, n); // TODO flag
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#adjustMonth(int)
	 */
	public void adjustMonth(final int n) {
		adjustMonth(n, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#adjustSecond(int, boolean)
	 */
	public void adjustSecond(final int n, final boolean preserveLocalTime) {
		try {
			lotus.domino.DateTime worker = getWorker();
			worker.adjustSecond(n, preserveLocalTime);
			workDone(worker, true);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
		// cal_.add(Calendar.SECOND, n); // TODO flag
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#adjustSecond(int)
	 */
	public void adjustSecond(final int n) {
		adjustSecond(n, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#adjustYear(int, boolean)
	 */
	public void adjustYear(final int n, final boolean preserveLocalTime) {
		try {
			lotus.domino.DateTime worker = getWorker();
			worker.adjustYear(n, preserveLocalTime);
			workDone(worker, true);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
		// cal_.add(Calendar.YEAR, n); // TODO flag
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#adjustYear(int)
	 */
	public void adjustYear(final int n) {
		adjustYear(n, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#convertToZone(int, boolean)
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#isEqual(org.openntf.domino.DateTime)
	 */
	@Override
	public boolean equals(final org.openntf.domino.DateTime comparDate) {
		return cal_.equals(comparDate.toJavaCal());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#equalsIgnoreDate(org.openntf.domino.DateTime)
	 */
	@Override
	public boolean equalsIgnoreDate(final org.openntf.domino.DateTime comparDate) {
		Calendar c1 = GregorianCalendar.getInstance();
		Calendar c2 = GregorianCalendar.getInstance();
		c1.setTime(toJavaDate());
		c1.set(Calendar.DAY_OF_MONTH, 1);
		c1.set(Calendar.MONTH, 0);
		c1.set(Calendar.YEAR, 2000);
		c2.setTime(comparDate.toJavaDate());
		c2.set(Calendar.DAY_OF_MONTH, 1);
		c2.set(Calendar.MONTH, 0);
		c2.set(Calendar.YEAR, 2000);
		return c1.equals(c2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#equalsIgnoreTime(org.openntf.domino.DateTime)
	 */
	@Override
	public boolean equalsIgnoreTime(final org.openntf.domino.DateTime comparDate) {
		Calendar c1 = GregorianCalendar.getInstance();
		Calendar c2 = GregorianCalendar.getInstance();
		c1.setTime(toJavaDate());
		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MILLISECOND, 0);
		c2.setTime(comparDate.toJavaDate());
		c2.set(Calendar.HOUR_OF_DAY, 0);
		c2.set(Calendar.MINUTE, 0);
		c2.set(Calendar.SECOND, 0);
		c2.set(Calendar.MILLISECOND, 0);
		return c1.equals(c2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#getDateOnly()
	 */
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
		// return getAncestor().getFormatter().getDateOnly(cal_.getTime());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#getGMTTime()
	 */
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
		// return getAncestor().getFormatter().getDateTime(cal_.getTime());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#getLocalTime()
	 */
	public String getLocalTime() {
		// TODO rationalize timezone stuff
		String ret = null;
		try {
			lotus.domino.DateTime worker = getWorker();
			ret = worker.getLocalTime();
			workDone(worker, false);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
		return ret;
		// 		return getAncestor().getFormatter().getDateTime(cal_.getTime());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public Session getParent() {
		return getAncestor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#getTimeOnly()
	 */
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
		// return getAncestor().getFormatter().getTimeOnly(cal_.getTime());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#getTimeZone()
	 */
	public int getTimeZone() {
		return notesZone_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#getZoneTime()
	 */
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
	public boolean isAfter(final org.openntf.domino.DateTime comparDate) {
		return cal_.after(comparDate.toJavaCal());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#isBefore(org.openntf.domino.DateTime)
	 */
	@Override
	public boolean isBefore(final org.openntf.domino.DateTime comparDate) {
		return cal_.before(comparDate.toJavaCal());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#isDST()
	 */
	public boolean isDST() {
		return dst_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#setAnyDate()
	 */
	public void setAnyDate() {
		isTimeOnly_ = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#setAnyTime()
	 */
	public void setAnyTime() {
		isDateOnly_ = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#setLocalDate(int, int, int, boolean)
	 */
	public void setLocalDate(final int year, final int month, final int day, final boolean preserveLocalTime) {
		// TODO NTF - deal with timezone and dst nonsense :-/
		try {
			lotus.domino.DateTime worker = getWorker();
			worker.setLocalDate(year, month, day, preserveLocalTime);
			workDone(worker, true);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
		// cal_.set(Calendar.YEAR, year);
		// cal_.set(Calendar.MONTH, month);
		// cal_.set(Calendar.DAY_OF_MONTH, day);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#setLocalDate(int, int, int)
	 */
	public void setLocalDate(final int year, final int month, final int day) {
		setLocalDate(year, month, day, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#setLocalTime(java.util.Calendar)
	 */
	public void setLocalTime(final java.util.Calendar calendar) {
		setLocalTime(calendar.getTime());
		// cal_.setTime(calendar.getTime());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ext.DateTime#setLocalTime(com.ibm.icu.util.Calendar)
	 */
	@Override
	public void setLocalTime(final Calendar calendar) {
		setLocalTime(calendar.getTime());
		// cal_.setTime(calendar.getTime());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#setLocalTime(java.util.Date)
	 */
	public void setLocalTime(final Date date) {
		try {
			lotus.domino.DateTime worker = getWorker();
			worker.setLocalTime(date);
			workDone(worker, true);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
		// cal_.setTime(date);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#setLocalTime(int, int, int, int)
	 */
	public void setLocalTime(final int hour, final int minute, final int second, final int hundredth) {
		// TODO NTF - figure out exactly what this means vis a vis timezones
		try {
			lotus.domino.DateTime worker = getWorker();
			worker.setLocalTime(hour, minute, second, hundredth);
			workDone(worker, true);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
		// cal_.set(Calendar.HOUR_OF_DAY, hour);
		// cal_.set(Calendar.MINUTE, minute);
		// cal_.set(Calendar.SECOND, second);
		// cal_.set(Calendar.MILLISECOND, hundredth * 10);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#setLocalTime(java.lang.String)
	 */
	public void setLocalTime(final String time) {
		try {
			lotus.domino.DateTime worker = getWorker();
			worker.setLocalTime(time);
			workDone(worker, true);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
		// Date date = getAncestor().getFormatter().parseDateFromString(time);
		// cal_.setTime(date);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#setNow()
	 */
	public void setNow() {
		setLocalTime(new Date());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#timeDifference(lotus.domino.DateTime)
	 */
	public int timeDifference(final lotus.domino.DateTime dt) {
		Integer[] res = new Integer[1];
		res[0] = new Integer(0);
		timeDifferenceCommon(dt, res);
		return (res[0]);
		/*******
		 * int result = 0; if (dt instanceof org.openntf.domino.impl.DateTime) { DateTime otherDT = (org.openntf.domino.impl.DateTime) dt;
		 * // so we can access private members directly. long left = cal_.getTimeInMillis() / 1000; // math is in seconds long right =
		 * otherDT.toJavaDate().getTime() / 1000; // math is also in seconds result = Long.valueOf(left - right).intValue(); } else { long
		 * left = cal_.getTimeInMillis() / 1000; // math is in seconds long right = 0; try { right = dt.toJavaDate().getTime() / 1000; //
		 * math is also in seconds } catch (NotesException ne) { DominoUtils.handleException(ne); } result = Long.valueOf(left -
		 * right).intValue(); } return result;
		 *******/
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#timeDifferenceDouble(lotus.domino.DateTime)
	 */
	public double timeDifferenceDouble(final lotus.domino.DateTime dt) {
		Double[] res = new Double[1];
		res[0] = new Double(0);
		timeDifferenceCommon(dt, res);
		return (res[0]);
		/******
		 * // TODO NTF - this probably returns a higher-precision number in the legacy API int i = this.timeDifference(dt); return
		 * Double.valueOf(i).doubleValue();
		 *****/
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
			if (res[0] instanceof Integer)
				res[0] = worker.timeDifference(dtLocal);
			else if (res[0] instanceof Double)
				res[0] = worker.timeDifferenceDouble(dtLocal);
			workDone(worker, false);
			if (lotusDTTmp != null)
				lotusDTTmp.recycle();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		} finally {
			if (lotusDTTmp != null)
				s_recycle(lotusDTTmp);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#toJavaDate()
	 */
	public Date toJavaDate() {
		//		Date ret = null;
		//		try {
		//			lotus.domino.DateTime worker = getWorker();
		//			ret = worker.toJavaDate();
		//			workDone(worker, false);
		//		} catch (NotesException ne) {
		//			DominoUtils.handleException(ne);
		//		}
		//		return ret;
		return cal_.getTime();
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
		// return cal_.getTime().toString();
		// Just re-instantiate the delegate for now to ensure compatibility
		// TODO Switch to a date formatter that properly mimics the original
		// lotus.domino.DateTime delegate = this.getDelegate();
		// try {
		//	return delegate.toString();
		// } finally {
		//	s_recycle(delegate);
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public Session getAncestorSession() {
		return this.getParent();
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
		Date dt = toJavaDate();
		Calendar ret = Calendar.getInstance();
		if (dt != null)
			ret.setTime(dt);
		return ret;
		// return cal_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final org.openntf.domino.ext.DateTime arg0) {
		return cal_.compareTo(arg0.toJavaCal());
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		dst_ = in.readBoolean();
		isDateOnly_ = in.readBoolean();
		isTimeOnly_ = in.readBoolean();
		notesZone_ = in.readInt();
		cal_.setTimeInMillis(in.readLong());
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeBoolean(dst_);
		out.writeBoolean(isDateOnly_);
		out.writeBoolean(isTimeOnly_);
		out.writeInt(notesZone_);
		out.writeLong(cal_.getTimeInMillis());
	}

	/*
	 * Deprecated, but needed for Externalization
	 */
	@Deprecated
	public DateTime() {
		super(null, Factory.getSession(), null, 0, NOTES_TIME);
	}

}
