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

import org.openntf.domino.DateTime;
import org.openntf.domino.Session;
import org.openntf.domino.exceptions.BlockedCrashException;
import org.openntf.domino.exceptions.UnimplementedException;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

import com.ibm.icu.util.Calendar;

// TODO: Auto-generated Javadoc
/**
 * The Class DateRange.
 */
public class DateRange extends Base<org.openntf.domino.DateRange, lotus.domino.DateRange> implements org.openntf.domino.DateRange,
		lotus.domino.DateRange {

	//	private java.util.Date startDate_;
	//	private java.util.Date endDate_;
	private DateTime startDateTime_;
	private DateTime endDateTime_;

	/**
	 * Instantiates a new date range.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	@Deprecated
	public DateRange(final lotus.domino.DateRange delegate, final org.openntf.domino.Base<?> parent) {
		super(delegate, Factory.getSession(parent));
		if (delegate instanceof lotus.domino.local.DateRange) {
			initialize(delegate);
			Base.s_recycle(delegate);
		}
	}

	@Deprecated
	public DateRange(final java.util.Date start, final java.util.Date end, final org.openntf.domino.Base<?> parent) {
		super(null, Factory.getSession(parent));
		//		Session session = Factory.getSession(parent);
		startDateTime_ = new org.openntf.domino.impl.DateTime(start, Factory.getSession(parent));
		endDateTime_ = new org.openntf.domino.impl.DateTime(end, Factory.getSession(parent));
		//		startDate_ = start;
		//		endDate_ = end;
	}

	private void initialize(final lotus.domino.DateRange delegate) {
		try {
			lotus.domino.DateTime dt = delegate.getStartDateTime();
			if (dt != null) {
				startDateTime_ = fromLotus(dt, DateTime.SCHEMA, getParent());
			}
		} catch (NotesException ne) {
			throw new RuntimeException(ne);
		}
		try {
			lotus.domino.DateTime dt = delegate.getEndDateTime();
			if (dt != null) {
				endDateTime_ = fromLotus(dt, DateTime.SCHEMA, getParent());
			}
		} catch (NotesException ne) {
			throw new RuntimeException(ne);
		}
	}

	public Date getEndDate() {
		if (endDateTime_ == null)
			return null;
		return endDateTime_.toJavaDate();
	}

	public Date getStartDate() {
		if (startDateTime_ == null)
			return null;
		return startDateTime_.toJavaDate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateRange#getEndDateTime()
	 */
	@Override
	public DateTime getEndDateTime() {
		return endDateTime_;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public Session getParent() {
		return (org.openntf.domino.Session) super.getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateRange#getStartDateTime()
	 */
	@Override
	public DateTime getStartDateTime() {
		return startDateTime_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateRange#getText()
	 */
	@Override
	public String getText() {
		StringBuilder sb = new StringBuilder();
		if (startDateTime_ != null) {
			sb.append(startDateTime_.getLocalTime());
			sb.append(" - ");
		}
		if (endDateTime_ != null) {
			sb.append(endDateTime_.getLocalTime());
		}
		return sb.toString();
		//		try {
		//			return getDelegate().getText();
		//		} catch (NotesException e) {
		//			DominoUtils.handleException(e);
		//			return null;
		//
		//		}
	}

	public void setEndDate(final java.util.Date date) {
		endDateTime_ = new org.openntf.domino.impl.DateTime(date, getParent());
	}

	public void setStartDate(final java.util.Date date) {
		startDateTime_ = new org.openntf.domino.impl.DateTime(date, getParent());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateRange#setEndDateTime(lotus.domino.DateTime)
	 */
	@Override
	public void setEndDateTime(final lotus.domino.DateTime end) {
		endDateTime_ = fromLotus(end, DateTime.SCHEMA, getParent());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateRange#setStartDateTime(lotus.domino.DateTime)
	 */
	@Override
	public void setStartDateTime(final lotus.domino.DateTime start) {
		startDateTime_ = fromLotus(start, DateTime.SCHEMA, getParent());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateRange#setText(java.lang.String)
	 */
	@Override
	public void setText(final String text) {
		throw new UnimplementedException("DateRange.setText(String) has not yet been implemented.");
		//		try {
		//			getDelegate().setText(text);
		//		} catch (NotesException e) {
		//			DominoUtils.handleException(e);
		//
		//		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.impl.Base#getDelegate()
	 */
	@Override
	protected lotus.domino.DateRange getDelegate() {
		try {
			lotus.domino.Session rawsession = toLotus(Factory.getSession(getParent()));
			if (startDateTime_ != null && endDateTime_ != null) {
				return rawsession.createDateRange(startDateTime_.toJavaDate(), endDateTime_.toJavaDate());
			}
			throw new BlockedCrashException(
					"Not attempting a return of a valid DateRange because either start date or end date is null and crashes may result.");
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return null;
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
	 * @see org.openntf.domino.ext.DateRange#contains(org.openntf.domino.DateTime)
	 */
	@Override
	public boolean contains(final org.openntf.domino.DateTime dt) {
		Calendar dtCal = dt.toJavaCal();
		Calendar startCal = dt.toJavaCal();
		Calendar endCal = dt.toJavaCal();
		if (dt.isAnyDate()) {
			// Compare times only -normalize dates
			dtCal.set(Calendar.YEAR, 2013);
			dtCal.set(Calendar.MONTH, 0);
			dtCal.set(Calendar.DAY_OF_MONTH, 2);

			startCal.set(Calendar.YEAR, 2013);
			startCal.set(Calendar.MONTH, 0);
			startCal.set(Calendar.DAY_OF_MONTH, 2);

			endCal.set(Calendar.YEAR, 2013);
			endCal.set(Calendar.MONTH, 0);
			endCal.set(Calendar.DAY_OF_MONTH, 2);
		} else if (dt.isAnyTime()) {
			// Compare dates only - normalize times
			dtCal.set(Calendar.HOUR_OF_DAY, 12);
			dtCal.set(Calendar.MINUTE, 0);
			dtCal.set(Calendar.SECOND, 0);
			dtCal.set(Calendar.MILLISECOND, 0);

			startCal.set(Calendar.HOUR_OF_DAY, 12);
			startCal.set(Calendar.MINUTE, 0);
			startCal.set(Calendar.SECOND, 0);
			startCal.set(Calendar.MILLISECOND, 0);

			endCal.set(Calendar.HOUR_OF_DAY, 12);
			endCal.set(Calendar.MINUTE, 0);
			endCal.set(Calendar.SECOND, 0);
			endCal.set(Calendar.MILLISECOND, 0);
		}
		return (dtCal.equals(startCal) || dtCal.after(startCal)) && (dtCal.equals(endCal) || dtCal.before(endCal));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ext.DateRange#contains(java.util.Date)
	 */
	@Override
	public boolean contains(final Date date) {
		Date start = this.getStartDateTime().toJavaDate();
		Date end = this.getEndDateTime().toJavaDate();
		return (date.equals(start) || date.after(start)) && (date.equals(end) || date.before(end));
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		startDateTime_ = (DateTime) in.readObject();
		endDateTime_ = (DateTime) in.readObject();
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeObject(startDateTime_);
		out.writeObject(endDateTime_);
	}

}
