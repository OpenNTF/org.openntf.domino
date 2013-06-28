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

import org.openntf.domino.types.SessionDescendant;

/**
 * The Interface NotesCalendarNotice.
 */
public interface NotesCalendarNotice extends Base<lotus.domino.NotesCalendarNotice>, lotus.domino.NotesCalendarNotice,
		org.openntf.domino.ext.NotesCalendarNotice, SessionDescendant {

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarNotice#accept(java.lang.String)
	 */
	@Override
	public void accept(final String comments);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarNotice#acceptCounter(java.lang.String)
	 */
	@Override
	public void acceptCounter(final String comments);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarNotice#counter(java.lang.String, lotus.domino.DateTime, lotus.domino.DateTime)
	 */
	@Override
	public void counter(final String comments, final lotus.domino.DateTime start, final lotus.domino.DateTime end);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarNotice#counter(java.lang.String, lotus.domino.DateTime, lotus.domino.DateTime, boolean)
	 */
	@Override
	public void counter(final String comments, final lotus.domino.DateTime start, final lotus.domino.DateTime end, final boolean keepPlaceholder);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarNotice#decline(java.lang.String)
	 */
	@Override
	public void decline(final String comments);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarNotice#decline(java.lang.String, boolean)
	 */
	@Override
	public void decline(final String comments, final boolean keepInformed);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarNotice#declineCounter(java.lang.String)
	 */
	@Override
	public void declineCounter(final String comments);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarNotice#delegate(java.lang.String, java.lang.String)
	 */
	@Override
	public void delegate(final String commentsToOrganizer, final String delegateTo);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarNotice#delegate(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public void delegate(final String commentsToOrganizer, final String delegateTo, final boolean keepInformed);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarNotice#getAsDocument()
	 */
	@Override
	public Document getAsDocument();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarNotice#getNoteID()
	 */
	@Override
	public String getNoteID();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarNotice#getOutstandingInvitations()
	 */
	@Override
	public Vector<NotesCalendarNotice> getOutstandingInvitations();

	/**
	 * Gets the parent.
	 * 
	 * @return the parent
	 */
	public NotesCalendar getParent();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarNotice#getUNID()
	 */
	@Override
	public String getUNID();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarNotice#read()
	 */
	@Override
	public boolean isOverwriteCheckEnabled();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarNotice#read()
	 */
	@Override
	public String read();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarNotice#removeCancelled()
	 */
	@Override
	public void removeCancelled();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarNotice#requestInfo(java.lang.String)
	 */
	@Override
	public void requestInfo(final String comments);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarNotice#sendUpdatedInfo(java.lang.String)
	 */
	@Override
	public void sendUpdatedInfo(final String comments);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarNotice#tentativelyAccept(java.lang.String)
	 */
	@Override
	public void setOverwriteCheckEnabled(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarNotice#tentativelyAccept(java.lang.String)
	 */
	@Override
	public void tentativelyAccept(final String comments);
}
