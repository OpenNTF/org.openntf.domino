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
import org.openntf.domino.exceptions.UnimplementedException;
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

	private static void terminate() {
		calendar.set(null);
	}

	/** The cal_. */
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
	private int notesZone_;

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
			lotus.domino.DateTime delegate = rawsession.createDateTime(date_);
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
	 * @param date
	 *            the date
	 */
	private void initialize(final java.util.Date date) {
		date_ = new Date(date.getTime());	//NTF copy to keep immutable
		//		cal_.setTime(date);
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
			String dateonly = delegate.getDateOnly();
			if (dateonly == null || dateonly.length() == 0)
				isTimeOnly_ = true;
			String timeonly = delegate.getTimeOnly();
			if (timeonly == null || timeonly.length() == 0)
				isDateOnly_ = true;
			try {
				if (isTimeOnly_ && isDateOnly_) {
					date_ = null;
				} else {
					date_ = delegate.toJavaDate();
				}
				//				cal_.setTime(date);
			} catch (NotesException e1) {
				// System.out.println("Error attempting to initialize a DateTime: " + delegate.getGMTTime());
				throw new RuntimeException(e1);
			}

		} catch (NotesException e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#adjustDay(int, boolean)
	 */
	public void adjustDay(final int n, final boolean preserveLocalTime) {
		Calendar cal = calendar.get();
		cal.setTime(date_);
		cal.add(Calendar.DAY_OF_MONTH, n); // TODO NTF - figure out what preserveLocalTime *REALLY* does
		date_ = cal.getTime();
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
		Calendar cal = calendar.get();
		cal.setTime(date_);
		cal.add(Calendar.HOUR_OF_DAY, n); // TODO flag
		date_ = cal.getTime();
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
		Calendar cal = calendar.get();
		cal.setTime(date_);
		cal.add(Calendar.MINUTE, n); // TODO flag
		date_ = cal.getTime();
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
		Calendar cal = calendar.get();
		cal.setTime(date_);
		cal.add(Calendar.MONTH, n); // TODO flag
		date_.getTime();
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
		Calendar cal = calendar.get();
		cal.setTime(date_);
		cal.add(Calendar.SECOND, n); // TODO flag
		date_.getTime();
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
		Calendar cal = calendar.get();
		cal.setTime(date_);
		cal.add(Calendar.YEAR, n); // TODO flag
		date_.getTime();
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
		// TODO NTF - find out what this actually does. The documentation is... vague
		throw new UnimplementedException("convertToZone is not yet implemented.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#isEqual(org.openntf.domino.DateTime)
	 */
	@Override
	public boolean equals(final org.openntf.domino.DateTime compareDate) {
		return date_.equals(compareDate.toJavaDate());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#equalsIgnoreDate(org.openntf.domino.DateTime)
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#equalsIgnoreTime(org.openntf.domino.DateTime)
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
	public String getDateOnly() {
		return getAncestor().getFormatter().getDateOnly(date_);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#getGMTTime()
	 */
	public String getGMTTime() {
		return getAncestor().getFormatter().getDateTime(date_);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#getLocalTime()
	 */
	public String getLocalTime() {
		// TODO rationalize timezone stuff
		return getAncestor().getFormatter().getDateTime(date_);
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
		return getAncestor().getFormatter().getTimeOnly(date_);
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
		throw new UnimplementedException("getZoneTime is not yet implemented.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#isAfter(org.openntf.domino.DateTime)
	 */
	public boolean isAfter(final org.openntf.domino.DateTime compareDate) {
		return date_.after(compareDate.toJavaDate());
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
		Calendar cal = calendar.get();
		cal.setTime(date_);
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, day);
		date_ = cal.getTime();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#setLocalDate(int, int, int)
	 */
	public void setLocalDate(final int year, final int month, final int day) {
		Calendar cal = calendar.get();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, day);
		date_ = cal.getTime();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#setLocalTime(java.util.Calendar)
	 */
	public void setLocalTime(final java.util.Calendar calendar) {
		date_ = calendar.getTime();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ext.DateTime#setLocalTime(com.ibm.icu.util.Calendar)
	 */
	@Override
	public void setLocalTime(final Calendar calendar) {
		date_ = calendar.getTime();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#setLocalTime(java.util.Date)
	 */
	public void setLocalTime(final Date date) {
		date_ = new Date(date.getTime());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#setLocalTime(int, int, int, int)
	 */
	public void setLocalTime(final int hour, final int minute, final int second, final int hundredth) {
		// TODO NTF - figure out exactly what this means vis a vis timezones
		Calendar cal = calendar.get();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);
		cal.set(Calendar.MILLISECOND, hundredth * 10);
		date_ = cal.getTime();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#setLocalTime(java.lang.String)
	 */
	public void setLocalTime(final String time) {
		date_ = getAncestor().getFormatter().parseDateFromString(time);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#setNow()
	 */
	public void setNow() {
		date_ = new Date();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#timeDifference(lotus.domino.DateTime)
	 */
	public int timeDifference(final lotus.domino.DateTime dt) {
		int result = 0;
		if (dt instanceof org.openntf.domino.impl.DateTime) {
			DateTime otherDT = (org.openntf.domino.impl.DateTime) dt; // so we can access private members directly.
			long left = date_.getTime() / 1000; // math is in seconds
			long right = otherDT.toJavaDate().getTime() / 1000; // math is also in seconds
			result = Long.valueOf(left - right).intValue();
		} else {
			long left = date_.getTime() / 1000; // math is in seconds
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
	public double timeDifferenceDouble(final lotus.domino.DateTime dt) {
		// TODO NTF - this probably returns a higher-precision number in the legacy API
		int i = this.timeDifference(dt);
		return Double.valueOf(i).doubleValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateTime#toJavaDate()
	 */
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
		// return cal_.getTime().toString();
		// Just re-instantiate the delegate for now to ensure compatibility
		// TODO Switch to a date formatter that properly mimics the original
		lotus.domino.DateTime delegate = this.getDelegate();
		try {
			return delegate.toString();
		} finally {
			s_recycle(delegate);
		}
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
		Calendar result = GregorianCalendar.getInstance();
		result.setTime(date_);
		return result;
	}

	public int compareTo(final org.openntf.domino.DateTime arg0) {
		return date_.compareTo(arg0.toJavaDate());
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
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
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeBoolean(dst_);
		out.writeBoolean(isDateOnly_);
		out.writeBoolean(isTimeOnly_);
		out.writeInt(notesZone_);
		out.writeLong(date_.getTime());
	}

}
