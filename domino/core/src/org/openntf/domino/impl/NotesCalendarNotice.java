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

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.Document;
import org.openntf.domino.NotesCalendar;
import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.utils.DominoUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class NotesCalendarNotice.
 */
public class NotesCalendarNotice extends Base<org.openntf.domino.NotesCalendarNotice, lotus.domino.NotesCalendarNotice, NotesCalendar>
		implements org.openntf.domino.NotesCalendarNotice {

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
	public NotesCalendarNotice(final lotus.domino.NotesCalendarNotice delegate, final NotesCalendar parent, final WrapperFactory wf,
			final long cppId) {
		super(delegate, parent, wf, cppId, NOTES_CALENDARNOTICE);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.impl.Base#findParent(lotus.domino.Base)
	 */
	@Override
	protected NotesCalendar findParent(final lotus.domino.NotesCalendarNotice delegate) throws NotesException {
		// TODO Auto-generated method stub
		throw new IllegalArgumentException();
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarNotice#accept(java.lang.String)
	 */
	@Override
	public void accept(final String comments) {
		try {
			getDelegate().accept(comments);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarNotice#acceptCounter(java.lang.String)
	 */
	@Override
	public void acceptCounter(final String comments) {
		try {
			getDelegate().acceptCounter(comments);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarNotice#counter(java.lang.String, lotus.domino.DateTime, lotus.domino.DateTime)
	 */
	@Override
	public void counter(final String comments, final lotus.domino.DateTime start, final lotus.domino.DateTime end) {
		@SuppressWarnings("rawtypes")
		List recycleThis = new ArrayList();
		try {
			lotus.domino.DateTime dt1 = toLotus(start, recycleThis);
			lotus.domino.DateTime dt2 = toLotus(end, recycleThis);
			getDelegate().counter(comments, dt1, dt2);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		} finally {
			s_recycle(recycleThis);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarNotice#counter(java.lang.String, lotus.domino.DateTime, lotus.domino.DateTime, boolean)
	 */
	@Override
	public void counter(final String comments, final lotus.domino.DateTime start, final lotus.domino.DateTime end,
			final boolean keepPlaceholder) {
		@SuppressWarnings("rawtypes")
		List recycleThis = new ArrayList();
		try {
			lotus.domino.DateTime dt1 = toLotus(start, recycleThis);
			lotus.domino.DateTime dt2 = toLotus(end, recycleThis);
			getDelegate().counter(comments, dt1, dt2, keepPlaceholder);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		} finally {
			s_recycle(recycleThis);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarNotice#decline(java.lang.String)
	 */
	@Override
	public void decline(final String comments) {
		try {
			getDelegate().decline(comments);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarNotice#decline(java.lang.String, boolean)
	 */
	@Override
	public void decline(final String comments, final boolean keepInformed) {
		try {
			getDelegate().decline(comments, keepInformed);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarNotice#declineCounter(java.lang.String)
	 */
	@Override
	public void declineCounter(final String comments) {
		try {
			getDelegate().declineCounter(comments);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarNotice#delegate(java.lang.String, java.lang.String)
	 */
	@Override
	public void delegate(final String commentsToOrganizer, final String delegateTo) {
		try {
			getDelegate().delegate(commentsToOrganizer, delegateTo);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarNotice#delegate(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public void delegate(final String commentsToOrganizer, final String delegateTo, final boolean keepInformed) {
		try {
			getDelegate().delegate(commentsToOrganizer, delegateTo, keepInformed);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarNotice#getAsDocument()
	 */
	@Override
	public Document getAsDocument() {
		try {
			return fromLotus(getDelegate().getAsDocument(), Document.SCHEMA, null);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarNotice#getNoteID()
	 */
	@Override
	public String getNoteID() {
		try {
			return getDelegate().getNoteID();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarNotice#getOutstandingInvitations()
	 */
	@Override
	public Vector<org.openntf.domino.NotesCalendarNotice> getOutstandingInvitations() {
		try {
			return fromLotusAsVector(getDelegate().getOutstandingInvitations(), org.openntf.domino.NotesCalendarNotice.SCHEMA,
					this.getParent());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public NotesCalendar getParent() {
		return getAncestor();
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarNotice#getUNID()
	 */
	@Override
	public String getUNID() {
		try {
			return getDelegate().getUNID();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarNotice#isOverwriteCheckEnabled()
	 */
	@Override
	public boolean isOverwriteCheckEnabled() {
		try {
			return getDelegate().isOverwriteCheckEnabled();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarNotice#read()
	 */
	@Override
	public String read() {
		try {
			return getDelegate().read();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarNotice#removeCancelled()
	 */
	@Override
	public void removeCancelled() {
		try {
			getDelegate().removeCancelled();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarNotice#requestInfo(java.lang.String)
	 */
	@Override
	public void requestInfo(final String comments) {
		try {
			getDelegate().requestInfo(comments);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarNotice#sendUpdatedInfo(java.lang.String)
	 */
	@Override
	public void sendUpdatedInfo(final String comments) {
		try {
			getDelegate().sendUpdatedInfo(comments);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarNotice#setOverwriteCheckEnabled(boolean)
	 */
	@Override
	public void setOverwriteCheckEnabled(final boolean flag) {
		try {
			getDelegate().setOverwriteCheckEnabled(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarNotice#tentativelyAccept(java.lang.String)
	 */
	@Override
	public void tentativelyAccept(final String comments) {
		try {
			getDelegate().tentativelyAccept(comments);
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
		return this.getParent().getAncestorSession();
	}
}
