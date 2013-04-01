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
package org.openntf.domino;

import java.util.Vector;

// TODO: Auto-generated Javadoc
/**
 * The Interface NotesCalendarNotice.
 */
public interface NotesCalendarNotice extends Base<lotus.domino.NotesCalendarNotice>, lotus.domino.NotesCalendarNotice {

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarNotice#accept(java.lang.String)
	 */
	@Override
	public void accept(String comments);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarNotice#acceptCounter(java.lang.String)
	 */
	@Override
	public void acceptCounter(String comments);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarNotice#counter(java.lang.String, lotus.domino.DateTime, lotus.domino.DateTime)
	 */
	@Override
	public void counter(String comments, lotus.domino.DateTime start, lotus.domino.DateTime end);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarNotice#counter(java.lang.String, lotus.domino.DateTime, lotus.domino.DateTime, boolean)
	 */
	@Override
	public void counter(String comments, lotus.domino.DateTime start, lotus.domino.DateTime end, boolean keepPlaceholder);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarNotice#decline(java.lang.String)
	 */
	@Override
	public void decline(String comments);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarNotice#decline(java.lang.String, boolean)
	 */
	@Override
	public void decline(String comments, boolean keepInformed);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarNotice#declineCounter(java.lang.String)
	 */
	@Override
	public void declineCounter(String comments);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarNotice#delegate(java.lang.String, java.lang.String)
	 */
	@Override
	public void delegate(String commentsToOrganizer, String delegateTo);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarNotice#delegate(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public void delegate(String commentsToOrganizer, String delegateTo, boolean keepInformed);

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
	public void requestInfo(String comments);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarNotice#sendUpdatedInfo(java.lang.String)
	 */
	@Override
	public void sendUpdatedInfo(String comments);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendarNotice#tentativelyAccept(java.lang.String)
	 */
	@Override
	public void setOverwriteCheckEnabled(boolean flag);

	@Override
	public void tentativelyAccept(String comments);
}
