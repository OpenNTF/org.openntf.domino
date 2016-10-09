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
 * The Class NotesCalendarEntry.
 */
public class NotesCalendarEntry extends
		BaseThreadSafe<org.openntf.domino.NotesCalendarEntry, lotus.domino.NotesCalendarEntry, NotesCalendar> implements
org.openntf.domino.NotesCalendarEntry {

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
	protected NotesCalendarEntry(final lotus.domino.NotesCalendarEntry delegate, final NotesCalendar parent, final WrapperFactory wf,
			final long cppId) {
		super(delegate, parent, NOTES_CALENDARENTRY);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarEntry#accept(java.lang.String)
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
	 * @see org.openntf.domino.NotesCalendarEntry#accept(java.lang.String, int, java.lang.String)
	 */
	@Override
	public void accept(final String comments, final int scope, final String recurrenceId) {
		try {
			getDelegate().accept(comments, scope, recurrenceId);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarEntry#cancel(java.lang.String)
	 */
	@Override
	public void cancel(final String comments) {
		try {
			getDelegate().cancel(comments);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarEntry#cancel(java.lang.String, int, java.lang.String)
	 */
	@Override
	public void cancel(final String comments, final int scope, final String recurrenceId) {
		try {
			getDelegate().cancel(comments, scope, recurrenceId);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarEntry#counter(java.lang.String, lotus.domino.DateTime, lotus.domino.DateTime)
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
	 * @see org.openntf.domino.NotesCalendarEntry#counter(java.lang.String, lotus.domino.DateTime, lotus.domino.DateTime, boolean)
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
	 * @see org.openntf.domino.NotesCalendarEntry#counter(java.lang.String, lotus.domino.DateTime, lotus.domino.DateTime, boolean, int, java.lang.String)
	 */
	@Override
	public void counter(final String comments, final lotus.domino.DateTime start, final lotus.domino.DateTime end,
			final boolean keepPlaceholder, final int scope, final String recurrenceId) {
		@SuppressWarnings("rawtypes")
		List recycleThis = new ArrayList();
		try {
			lotus.domino.DateTime dt1 = toLotus(start, recycleThis);
			lotus.domino.DateTime dt2 = toLotus(end, recycleThis);
			getDelegate().counter(comments, dt1, dt2, keepPlaceholder, scope, recurrenceId);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		} finally {
			s_recycle(recycleThis);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarEntry#counter(java.lang.String, lotus.domino.DateTime, lotus.domino.DateTime, int, java.lang.String)
	 */
	@Override
	public void counter(final String comments, final lotus.domino.DateTime start, final lotus.domino.DateTime end, final int scope,
			final String recurrenceId) {
		@SuppressWarnings("rawtypes")
		List recycleThis = new ArrayList();
		try {
			lotus.domino.DateTime dt1 = toLotus(start, recycleThis);
			lotus.domino.DateTime dt2 = toLotus(end, recycleThis);
			getDelegate().counter(comments, dt1, dt2, scope, recurrenceId);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		} finally {
			s_recycle(recycleThis);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarEntry#decline(java.lang.String)
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
	 * @see org.openntf.domino.NotesCalendarEntry#decline(java.lang.String, boolean)
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
	 * @see org.openntf.domino.NotesCalendarEntry#decline(java.lang.String, boolean, int, java.lang.String)
	 */
	@Override
	public void decline(final String comments, final boolean keepInformed, final int scope, final String recurrenceId) {
		try {
			getDelegate().decline(comments, keepInformed, scope, recurrenceId);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarEntry#delegate(java.lang.String, java.lang.String)
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
	 * @see org.openntf.domino.NotesCalendarEntry#delegate(java.lang.String, java.lang.String, boolean)
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
	 * @see org.openntf.domino.NotesCalendarEntry#delegate(java.lang.String, java.lang.String, boolean, int, java.lang.String)
	 */
	@Override
	public void delegate(final String commentsToOrganizer, final String delegateTo, final boolean keepInformed, final int scope,
			final String recurrenceId) {
		try {
			getDelegate().delegate(commentsToOrganizer, delegateTo, keepInformed, scope, recurrenceId);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarEntry#delegate(java.lang.String, java.lang.String, int, java.lang.String)
	 */
	@Override
	public void delegate(final String commentsToOrganizer, final String delegateTo, final int scope, final String recurrenceId) {
		try {
			getDelegate().delegate(commentsToOrganizer, delegateTo, scope, recurrenceId);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarEntry#getAsDocument()
	 */
	@Override
	public Document getAsDocument() {
		try {
			// TODO This should really come from the doc's DB
			return fromLotus(getDelegate().getAsDocument(), Document.SCHEMA, null);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarEntry#getAsDocument(int)
	 */
	@Override
	public Document getAsDocument(final int flags) {
		try {
			// TODO This should really come from the doc's DB
			return fromLotus(getDelegate().getAsDocument(flags), Document.SCHEMA, null);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarEntry#getAsDocument(int, java.lang.String)
	 */
	@Override
	public Document getAsDocument(final int flags, final String recurrenceId) {
		try {
			return fromLotus(getDelegate().getAsDocument(flags, recurrenceId), Document.SCHEMA, null);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarEntry#getNotices()
	 */
	@Override
	public Vector<org.openntf.domino.NotesCalendarNotice> getNotices() {
		try {
			return fromLotusAsVector(getDelegate().getNotices(), org.openntf.domino.NotesCalendarNotice.SCHEMA, parent);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public final NotesCalendar getParent() {
		return parent;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarEntry#getUID()
	 */
	@Override
	public String getUID() {
		try {
			return getDelegate().getUID();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarEntry#read()
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
	 * @see org.openntf.domino.NotesCalendarEntry#read(java.lang.String)
	 */
	@Override
	public String read(final String recurrenceId) {
		try {
			return getDelegate().read(recurrenceId);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarEntry#remove()
	 */
	@Override
	public void remove() {
		try {
			getDelegate().remove();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarEntry#remove(int, java.lang.String)
	 */
	@Override
	public void remove(final int scope, final String recurrenceId) {
		try {
			getDelegate().remove(scope, recurrenceId);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarEntry#requestInfo(java.lang.String)
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
	 * @see org.openntf.domino.NotesCalendarEntry#tentativelyAccept(java.lang.String)
	 */
	@Override
	public void tentativelyAccept(final String comments) {
		try {
			getDelegate().tentativelyAccept(comments);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarEntry#tentativelyAccept(java.lang.String, int, java.lang.String)
	 */
	@Override
	public void tentativelyAccept(final String comments, final int scope, final String recurrenceId) {
		try {
			getDelegate().tentativelyAccept(comments, scope, recurrenceId);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarEntry#update(java.lang.String)
	 */
	@Override
	public void update(final String iCalEntry) {
		try {
			getDelegate().update(iCalEntry);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarEntry#update(java.lang.String, java.lang.String)
	 */
	@Override
	public void update(final String iCalEntry, final String comments) {
		try {
			getDelegate().update(iCalEntry, comments);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarEntry#update(java.lang.String, java.lang.String, long)
	 */
	@Override
	public void update(final String iCalEntry, final String comments, final long flags) {
		try {
			getDelegate().update(iCalEntry, comments, flags);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarEntry#update(java.lang.String, java.lang.String, long, java.lang.String)
	 */
	@Override
	public void update(final String iCalEntry, final String comments, final long flags, final String recurrenceId) {
		try {
			getDelegate().update(iCalEntry, comments, flags, recurrenceId);
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
	public final Session getAncestorSession() {
		return parent.getAncestorSession();
	}

	/* (non-Javadoc)
	 * @see lotus.domino.NotesCalendarEntry#addInvitees(java.util.Vector, java.util.Vector, java.util.Vector)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public void addInvitees(final Vector arg0, final Vector arg1, final Vector arg2) {
		try {
			getDelegate().addInvitees(arg0, arg1, arg2);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see lotus.domino.NotesCalendarEntry#addInvitees(java.util.Vector, java.util.Vector, java.util.Vector, java.lang.String)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public void addInvitees(final Vector arg0, final Vector arg1, final Vector arg2, final String arg3) {
		try {
			getDelegate().addInvitees(arg0, arg1, arg2, arg3);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see lotus.domino.NotesCalendarEntry#addInvitees(java.util.Vector, java.util.Vector, java.util.Vector, java.lang.String, int)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public void addInvitees(final Vector arg0, final Vector arg1, final Vector arg2, final String arg3, final int arg4) {
		try {
			getDelegate().addInvitees(arg0, arg1, arg2, arg3, arg4);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see lotus.domino.NotesCalendarEntry#addInvitees(java.util.Vector, java.util.Vector, java.util.Vector, java.lang.String, int, int, java.lang.String)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public void addInvitees(final Vector arg0, final Vector arg1, final Vector arg2, final String arg3, final int arg4, final int arg5,
			final String arg6) {
		try {
			getDelegate().addInvitees(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see lotus.domino.NotesCalendarEntry#modifyInvitees(java.util.Vector, java.util.Vector, java.util.Vector, java.util.Vector)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public void modifyInvitees(final Vector arg0, final Vector arg1, final Vector arg2, final Vector arg3) {
		try {
			getDelegate().modifyInvitees(arg0, arg1, arg2, arg3);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see lotus.domino.NotesCalendarEntry#modifyInvitees(java.util.Vector, java.util.Vector, java.util.Vector, java.util.Vector, java.lang.String)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public void modifyInvitees(final Vector arg0, final Vector arg1, final Vector arg2, final Vector arg3, final String arg4) {
		try {
			getDelegate().modifyInvitees(arg0, arg1, arg2, arg3, arg4);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see lotus.domino.NotesCalendarEntry#modifyInvitees(java.util.Vector, java.util.Vector, java.util.Vector, java.util.Vector, java.lang.String, int)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public void modifyInvitees(final Vector arg0, final Vector arg1, final Vector arg2, final Vector arg3, final String arg4, final int arg5) {
		try {
			getDelegate().modifyInvitees(arg0, arg1, arg2, arg3, arg4, arg5);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see lotus.domino.NotesCalendarEntry#modifyInvitees(java.util.Vector, java.util.Vector, java.util.Vector, java.util.Vector, java.lang.String, int, int, java.lang.String)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public void modifyInvitees(final Vector arg0, final Vector arg1, final Vector arg2, final Vector arg3, final String arg4,
			final int arg5, final int arg6, final String arg7) {
		try {
			getDelegate().modifyInvitees(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}

	}

	/* (non-Javadoc)
	 * @see lotus.domino.NotesCalendarEntry#removeInvitees(java.util.Vector)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public void removeInvitees(final Vector arg0) {
		try {
			getDelegate().removeInvitees(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see lotus.domino.NotesCalendarEntry#removeInvitees(java.util.Vector, java.lang.String)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public void removeInvitees(final Vector arg0, final String arg1) {
		try {
			getDelegate().removeInvitees(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see lotus.domino.NotesCalendarEntry#removeInvitees(java.util.Vector, java.lang.String, int)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public void removeInvitees(final Vector arg0, final String arg1, final int arg2) {
		try {
			getDelegate().removeInvitees(arg0, arg1, arg2);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see lotus.domino.NotesCalendarEntry#removeInvitees(java.util.Vector, java.lang.String, int, int, java.lang.String)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public void removeInvitees(final Vector arg0, final String arg1, final int arg2, final int arg3, final String arg4) {
		try {
			getDelegate().removeInvitees(arg0, arg1, arg2, arg3, arg4);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see lotus.domino.NotesCalendarEntry#requestInfo(java.lang.String, java.lang.String)
	 */
	@Override
	public void requestInfo(final String arg0, final String arg1) {
		try {
			getDelegate().requestInfo(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	protected WrapperFactory getFactory() {
		return parent.getAncestorSession().getFactory();
	}

}
