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
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

import com.ibm.icu.util.Calendar;

// TODO: Auto-generated Javadoc
/**
 * The Class DateRange.
 */

/*
 * Regarding DateRange-s, the behaviour of Notes' Java API is very odd (at least with Notes 9.0.1):
 * 
 * 1) replaceItemValue works correctly for a value of type DateRange (as it should)
 * 2) replaceItemValue throws a NotesException (Unknown or unsupported object type in Vector)
 *    if value is a Vector containing DateRange-s (even if the Vector has size 1)
 * 3) getItemValueDateTimeArray, applied to an item with a single DateRange value, yields a
 *    Vector of size 1 (as it should), but elementAt(0) is null
 * 4) Analogously, getItemValueDateTimeArray, applied to a multiple DateRange item (generated
 *    e.g. via LotusScript), yields a Vector of size n with all elements null
 * 5) Finally, getItemValue, applied to a (single or multiple) DateRange item, returns a Vector of size 2*n
 *    containing start and end dates (as DateTime-s) of the DateRange(s).
 *    
 * On the other hand, in LotusScript everything works well: ReplaceItemValue lets you add an array
 * of NotesDateRange-s, GetItemValueDateTimeArray returns a correct array of NotesDateRange-s and so on.
 *  
 * Hence, for dealing with DateRange-s, openNTF Domino has 3 possibilities:
 *  
 * 1) Every DateRange is wrapped, regardless of whether it's a single value or a Vector of DateRange-s.
 *    Then everything works perfectly, but obviously, there's a considerable overhead.
 * 2) Or we let openNTF Domino behave like Notes (especially accept only single DateRange-s), with a workaround
 *    to make getItemValueDateTimeArray work correctly for DateRange-s. - Of course, wrapping of Vector-s
 *    containing "many" DateRange-s must then be deactivated.
 * 3) A mix of 1 and 2: Native Notes Java API is used, whenever it's a single DateRange (comprising the case of
 *    a Vector containing exactly one DateRange), whereas multiple DateRange-s are always wrapped.
 *    
 * At the moment, the second variant is implemented (without deactivation of wrapping).
 */

public class DateRange extends Base<org.openntf.domino.DateRange, lotus.domino.DateRange, Session> implements org.openntf.domino.DateRange,
		lotus.domino.DateRange {

	//	private java.util.Date startDate_;
	//	private java.util.Date endDate_;
	private DateTime startDateTime_ = null;
	private DateTime endDateTime_ = null;

	//	 * Instantiates a new date range.
	//	 * 
	//	 * @param delegate
	//	 *            the delegate
	//	 * @param parent
	//	 *            the parent
	//	 */
	//	@Deprecated
	//	public DateRange(final lotus.domino.DateRange delegate, final Session parent, WrapperFactory wf, long cppId) {
	//		super(delegate, Factory.getSession(parent), wf);
	//		if (delegate instanceof lotus.domino.local.DateRange) {
	//			initialize(delegate);
	//			Base.s_recycle(delegate);
	//		}
	//	}

	/**
	 * Creates a Daterange based on java dates
	 * 
	 * @param start
	 *            the start time
	 * @param end
	 *            the end time
	 * @param parent
	 *            the session
	 */
	public DateRange(final java.util.Date start, final java.util.Date end, final Session parent, final WrapperFactory wf) {
		super(null, parent, null, 0L, NOTES_DATERNG);
		startDateTime_ = getFactory().createDateTime(start, parent);
		endDateTime_ = getFactory().createDateTime(end, parent);
	}

	/**
	 * Instantiates a new outline.
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
	public DateRange(final lotus.domino.DateRange delegate, final Session parent, final WrapperFactory wf, final long cppId) {
		super(delegate, parent, wf, cppId, NOTES_DATERNG);
		initialize(delegate);
		//Base.s_recycle(delegate);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.impl.Base#findParent(lotus.domino.Base)
	 */
	@Override
	protected Session findParent(final lotus.domino.DateRange delegate) {
		if (delegate == null) {
			return Factory.getSession(); // the current Session
		}
		return fromLotus(Base.getSession(delegate), Session.SCHEMA, null);
	}

	/**
	 * The cpp-id is not used here, so we do not read it
	 * 
	 * @param delegate
	 * @param cppId
	 */
	@Override
	void setDelegate(final lotus.domino.DateRange delegate, final long cppId) {
		delegate_ = delegate;
	}

	private void initialize(final lotus.domino.DateRange delegate) {
		try {
			lotus.domino.DateTime sdt = delegate.getStartDateTime();
			lotus.domino.DateTime edt = delegate.getEndDateTime();
			Base.s_recycle(delegate);
			if (sdt != null)
				startDateTime_ = fromLotus(sdt, DateTime.SCHEMA, getParent());
			if (edt != null)
				endDateTime_ = fromLotus(edt, DateTime.SCHEMA, getParent());
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
		return getAncestor();
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
		if (startDateTime_ == null || endDateTime_ == null)
			return "";
		return startDateTime_.getLocalTime() + " - " + endDateTime_.getLocalTime();
	}

	public void setEndDate(final java.util.Date date) {
		endDateTime_ = getFactory().createDateTime(date, getAncestor());
	}

	public void setStartDate(final java.util.Date date) {
		startDateTime_ = getFactory().createDateTime(date, getAncestor());
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
		lotus.domino.DateRange dr = null;
		lotus.domino.Session rawsession = toLotus(Factory.getSession(getParent()));
		try {
			dr = rawsession.createDateRange();
			dr.setText(text);
			setStartDateTime(dr.getStartDateTime());
			setEndDateTime(dr.getEndDateTime());
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		} finally {
			Base.s_recycle(dr);
		}
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
			lotus.domino.DateRange ret;
			if (startDateTime_ != null && endDateTime_ != null)
				ret = rawsession.createDateRange(startDateTime_.toJavaDate(), endDateTime_.toJavaDate());
			else {
				ret = rawsession.createDateRange();
				if (startDateTime_ != null)
					ret.setStartDateTime(startDateTime_);
				if (endDateTime_ != null)
					ret.setEndDateTime(endDateTime_);
			}
			return ret;
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

	/*
	 * Deprecated, but needed for Externalization
	 */
	@Deprecated
	public DateRange() {
		super(null, Factory.getSession(), null, 0, NOTES_DATERNG);
	}
}
