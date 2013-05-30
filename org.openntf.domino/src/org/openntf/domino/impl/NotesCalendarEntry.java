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

import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.Session;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class NotesCalendarEntry.
 */
public class NotesCalendarEntry extends Base<org.openntf.domino.NotesCalendarEntry, lotus.domino.NotesCalendarEntry> implements
		org.openntf.domino.NotesCalendarEntry {

	/**
	 * Instantiates a new notes calendar entry.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	public NotesCalendarEntry(lotus.domino.NotesCalendarEntry delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarEntry#accept(java.lang.String)
	 */
	@Override
	public void accept(String comments) {
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
	public void accept(String comments, int scope, String recurrenceId) {
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
	public void cancel(String comments) {
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
	public void cancel(String comments, int scope, String recurrenceId) {
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
	public void counter(String comments, lotus.domino.DateTime start, lotus.domino.DateTime end) {
		try {
			lotus.domino.DateTime dt1 = (lotus.domino.DateTime) toLotus(start);
			lotus.domino.DateTime dt2 = (lotus.domino.DateTime) toLotus(end);
			getDelegate().counter(comments, dt1, dt2);
			enc_recycle(dt1);
			enc_recycle(dt2);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarEntry#counter(java.lang.String, lotus.domino.DateTime, lotus.domino.DateTime, boolean)
	 */
	@Override
	public void counter(String comments, lotus.domino.DateTime start, lotus.domino.DateTime end, boolean keepPlaceholder) {
		try {
			lotus.domino.DateTime dt1 = (lotus.domino.DateTime) toLotus(start);
			lotus.domino.DateTime dt2 = (lotus.domino.DateTime) toLotus(end);
			getDelegate().counter(comments, dt1, dt2, keepPlaceholder);
			enc_recycle(dt1);
			enc_recycle(dt2);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarEntry#counter(java.lang.String, lotus.domino.DateTime, lotus.domino.DateTime, boolean, int, java.lang.String)
	 */
	@Override
	public void counter(String comments, lotus.domino.DateTime start, lotus.domino.DateTime end, boolean keepPlaceholder, int scope,
			String recurrenceId) {
		try {
			lotus.domino.DateTime dt1 = (lotus.domino.DateTime) toLotus(start);
			lotus.domino.DateTime dt2 = (lotus.domino.DateTime) toLotus(end);
			getDelegate().counter(comments, dt1, dt2, keepPlaceholder, scope, recurrenceId);
			enc_recycle(dt1);
			enc_recycle(dt2);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarEntry#counter(java.lang.String, lotus.domino.DateTime, lotus.domino.DateTime, int, java.lang.String)
	 */
	@Override
	public void counter(String comments, lotus.domino.DateTime start, lotus.domino.DateTime end, int scope, String recurrenceId) {
		try {
			lotus.domino.DateTime dt1 = (lotus.domino.DateTime) toLotus(start);
			lotus.domino.DateTime dt2 = (lotus.domino.DateTime) toLotus(end);
			getDelegate().counter(comments, dt1, dt2, scope, recurrenceId);
			enc_recycle(dt1);
			enc_recycle(dt2);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarEntry#decline(java.lang.String)
	 */
	@Override
	public void decline(String comments) {
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
	public void decline(String comments, boolean keepInformed) {
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
	public void decline(String comments, boolean keepInformed, int scope, String recurrenceId) {
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
	public void delegate(String commentsToOrganizer, String delegateTo) {
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
	public void delegate(String commentsToOrganizer, String delegateTo, boolean keepInformed) {
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
	public void delegate(String commentsToOrganizer, String delegateTo, boolean keepInformed, int scope, String recurrenceId) {
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
	public void delegate(String commentsToOrganizer, String delegateTo, int scope, String recurrenceId) {
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
			return Factory.fromLotus(getDelegate().getAsDocument(), Document.class, this.getParent());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarEntry#getAsDocument(int)
	 */
	@Override
	public Document getAsDocument(int flags) {
		try {
			// TODO This should really come from the doc's DB
			return Factory.fromLotus(getDelegate().getAsDocument(flags), Document.class, this.getParent());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendarEntry#getAsDocument(int, java.lang.String)
	 */
	@Override
	public Document getAsDocument(int flags, String recurrenceId) {
		try {
			return Factory.fromLotus(getDelegate().getAsDocument(flags, recurrenceId), Document.class, this.getParent());
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
			return Factory.fromLotusAsVector(getDelegate().getNotices(), org.openntf.domino.NotesCalendarNotice.class, this.getParent());
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
		return (NotesCalendar) super.getParent();
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
	public String read(String recurrenceId) {
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
	public void remove(int scope, String recurrenceId) {
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
	public void requestInfo(String comments) {
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
	public void tentativelyAccept(String comments) {
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
	public void tentativelyAccept(String comments, int scope, String recurrenceId) {
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
	public void update(String iCalEntry) {
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
	public void update(String iCalEntry, String comments) {
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
	public void update(String iCalEntry, String comments, long flags) {
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
	public void update(String iCalEntry, String comments, long flags, String recurrenceId) {
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
	public Session getAncestorSession() {
		return this.getParent().getAncestorSession();
	}
}
