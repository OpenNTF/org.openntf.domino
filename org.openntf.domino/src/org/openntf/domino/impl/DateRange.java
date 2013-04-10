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

import lotus.domino.NotesException;

import org.openntf.domino.Session;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class DateRange.
 */
public class DateRange extends Base<org.openntf.domino.DateRange, lotus.domino.DateRange> implements org.openntf.domino.DateRange,
		lotus.domino.DateRange {

	/**
	 * Instantiates a new date range.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	public DateRange(lotus.domino.DateRange delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, (parent instanceof org.openntf.domino.Session) ? parent : Factory.getSession(parent));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateRange#getEndDateTime()
	 */
	@Override
	public DateTime getEndDateTime() {
		try {
			return Factory.fromLotus(getDelegate().getEndDateTime(), DateTime.class, getParent());
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
		return (org.openntf.domino.Session) super.getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateRange#getStartDateTime()
	 */
	@Override
	public DateTime getStartDateTime() {
		try {
			return Factory.fromLotus(getDelegate().getStartDateTime(), DateTime.class, getParent());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateRange#getText()
	 */
	@Override
	public String getText() {
		try {
			return getDelegate().getText();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateRange#setEndDateTime(lotus.domino.DateTime)
	 */
	@Override
	public void setEndDateTime(lotus.domino.DateTime end) {
		try {
			getDelegate().setEndDateTime((lotus.domino.DateTime) toLotus(end));
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateRange#setStartDateTime(lotus.domino.DateTime)
	 */
	@Override
	public void setStartDateTime(lotus.domino.DateTime start) {
		try {
			getDelegate().setStartDateTime((lotus.domino.DateTime) toLotus(start));
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DateRange#setText(java.lang.String)
	 */
	@Override
	public void setText(String text) {
		try {
			getDelegate().setText(text);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

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
	public boolean contains(org.openntf.domino.DateTime dt) {
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
	public boolean contains(Date date) {
		Date start = this.getStartDateTime().toJavaDate();
		Date end = this.getEndDateTime().toJavaDate();
		return (date.equals(start) || date.after(start)) && (date.equals(end) || date.before(end));
	}

}
