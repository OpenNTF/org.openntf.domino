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

}
