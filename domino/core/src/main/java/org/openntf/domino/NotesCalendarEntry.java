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
package org.openntf.domino;

import java.util.Vector;

import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.SessionDescendant;

/**
 * The Interface NotesCalendarEntry.
 */
public interface NotesCalendarEntry extends Base<lotus.domino.NotesCalendarEntry>, lotus.domino.NotesCalendarEntry,
		org.openntf.domino.ext.NotesCalendarEntry, SessionDescendant {

	public static class Schema extends FactorySchema<NotesCalendarEntry, lotus.domino.NotesCalendarEntry, NotesCalendar> {
		@Override
		public Class<NotesCalendarEntry> typeClass() {
			return NotesCalendarEntry.class;
		}

		@Override
		public Class<lotus.domino.NotesCalendarEntry> delegateClass() {
			return lotus.domino.NotesCalendarEntry.class;
		}

		@Override
		public Class<NotesCalendar> parentClass() {
			return NotesCalendar.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarEntry#accept(java.lang.String)
	 */
	@Override
	public void accept(final String comments);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarEntry#accept(java.lang.String, int, java.lang.String)
	 */
	@Override
	public void accept(final String comments, final int scope, final String recurrenceId);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarEntry#cancel(java.lang.String)
	 */
	@Override
	public void cancel(final String comments);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarEntry#cancel(java.lang.String, int, java.lang.String)
	 */
	@Override
	public void cancel(final String comments, final int scope, final String recurrenceId);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarEntry#counter(java.lang.String, lotus.domino.DateTime, lotus.domino.DateTime)
	 */
	@Override
	public void counter(final String comments, final lotus.domino.DateTime start, final lotus.domino.DateTime end);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarEntry#counter(java.lang.String, lotus.domino.DateTime, lotus.domino.DateTime, boolean)
	 */
	@Override
	public void counter(final String comments, final lotus.domino.DateTime start, final lotus.domino.DateTime end, final boolean keepPlaceholder);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarEntry#counter(java.lang.String, lotus.domino.DateTime, lotus.domino.DateTime, boolean, int,
	 * java.lang.String)
	 */
	@Override
	public void counter(final String comments, final lotus.domino.DateTime start, final lotus.domino.DateTime end, final boolean keepPlaceholder, final int scope,
			final String recurrenceId);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarEntry#counter(java.lang.String, lotus.domino.DateTime, lotus.domino.DateTime, int, java.lang.String)
	 */
	@Override
	public void counter(final String comments, final lotus.domino.DateTime start, final lotus.domino.DateTime end, final int scope, final String recurrenceId);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarEntry#decline(java.lang.String)
	 */
	@Override
	public void decline(final String comments);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarEntry#decline(java.lang.String, boolean)
	 */
	@Override
	public void decline(final String comments, final boolean keepInformed);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarEntry#decline(java.lang.String, boolean, int, java.lang.String)
	 */
	@Override
	public void decline(final String comments, final boolean keepInformed, final int scope, final String recurrenceId);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarEntry#delegate(java.lang.String, java.lang.String)
	 */
	@Override
	public void delegate(final String commentsToOrganizer, final String delegateTo);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarEntry#delegate(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public void delegate(final String commentsToOrganizer, final String delegateTo, final boolean keepInformed);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarEntry#delegate(java.lang.String, java.lang.String, boolean, int, java.lang.String)
	 */
	@Override
	public void delegate(final String commentsToOrganizer, final String delegateTo, final boolean keepInformed, final int scope, final String recurrenceId);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarEntry#delegate(java.lang.String, java.lang.String, int, java.lang.String)
	 */
	@Override
	public void delegate(final String commentsToOrganizer, final String delegateTo, final int scope, final String recurrenceId);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarEntry#getAsDocument()
	 */
	@Override
	public Document getAsDocument();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarEntry#getAsDocument(int)
	 */
	@Override
	public Document getAsDocument(final int flags);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarEntry#getAsDocument(int, java.lang.String)
	 */
	@Override
	public Document getAsDocument(final int flags, final String recurrenceId);

	/**
	 * Gets the parent.
	 * 
	 * @return the parent
	 */
	public NotesCalendar getParent();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarEntry#getNotices()
	 */
	@Override
	public Vector<NotesCalendarNotice> getNotices();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarEntry#getUID()
	 */
	@Override
	public String getUID();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarEntry#read()
	 */
	@Override
	public String read();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarEntry#read(java.lang.String)
	 */
	@Override
	public String read(final String recurrenceId);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarEntry#remove()
	 */
	@Override
	public void remove();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarEntry#remove(int, java.lang.String)
	 */
	@Override
	public void remove(final int scope, final String recurrenceId);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarEntry#requestInfo(java.lang.String)
	 */
	@Override
	public void requestInfo(final String comments);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarEntry#tentativelyAccept(java.lang.String)
	 */
	@Override
	public void tentativelyAccept(final String comments);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarEntry#tentativelyAccept(java.lang.String, int, java.lang.String)
	 */
	@Override
	public void tentativelyAccept(final String comments, final int scope, final String recurrenceId);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarEntry#update(java.lang.String)
	 */
	@Override
	public void update(final String iCalEntry);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarEntry#update(java.lang.String, java.lang.String)
	 */
	@Override
	public void update(final String iCalEntry, final String comments);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarEntry#update(java.lang.String, java.lang.String, long)
	 */
	@Override
	public void update(final String iCalEntry, final String comments, final long flags);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarEntry#update(java.lang.String, java.lang.String, long, java.lang.String)
	 */
	@Override
	public void update(final String iCalEntry, final String comments, final long flags, final String recurrenceId);

}
