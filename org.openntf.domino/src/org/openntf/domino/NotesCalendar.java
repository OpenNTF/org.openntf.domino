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

import org.openntf.domino.types.SessionDescendant;

// TODO: Auto-generated Javadoc
/**
 * The Interface NotesCalendar.
 */
public interface NotesCalendar extends Base<lotus.domino.NotesCalendar>, lotus.domino.NotesCalendar, SessionDescendant {

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendar#createEntry(java.lang.String)
	 */
	@Override
	public NotesCalendarEntry createEntry(String iCalEntry);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendar#createEntry(java.lang.String, int)
	 */
	@Override
	public NotesCalendarEntry createEntry(String iCalEntry, int flags);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendar#getAutoSendNotices()
	 */
	@Override
	public boolean getAutoSendNotices();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendar#getEntries(lotus.domino.DateTime, lotus.domino.DateTime)
	 */
	@Override
	public Vector<NotesCalendarEntry> getEntries(lotus.domino.DateTime start, lotus.domino.DateTime end);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendar#getEntries(lotus.domino.DateTime, lotus.domino.DateTime, int, int)
	 */
	@Override
	public Vector<NotesCalendarEntry> getEntries(lotus.domino.DateTime start, lotus.domino.DateTime end, int skipCount, int maxReturn);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendar#getEntriesProcessed()
	 */
	@Override
	public int getEntriesProcessed();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendar#getEntry(java.lang.String)
	 */
	@Override
	public NotesCalendarEntry getEntry(String uid);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendar#getEntryByNoteID(java.lang.String)
	 */
	@Override
	public NotesCalendarEntry getEntryByNoteID(String noteid);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendar#getEntryByUNID(java.lang.String)
	 */
	@Override
	public NotesCalendarEntry getEntryByUNID(String unid);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendar#getNewInvitations()
	 */
	@Override
	public Vector<NotesCalendarNotice> getNewInvitations();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendar#getNewInvitations(lotus.domino.DateTime, lotus.domino.DateTime)
	 */
	@Override
	public Vector<NotesCalendarNotice> getNewInvitations(lotus.domino.DateTime start, lotus.domino.DateTime since);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendar#getNoticeByUNID(java.lang.String)
	 */
	@Override
	public NotesCalendarNotice getNoticeByUNID(String unid);

	public Session getParent();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendar#getReadRangeMask1()
	 */
	@Override
	public int getReadRangeMask1();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendar#getReadRangeMask2()
	 */
	@Override
	public int getReadRangeMask2();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendar#getReadXLotusPropsOutputLevel()
	 */
	@Override
	public int getReadXLotusPropsOutputLevel();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendar#getUntilTime()
	 */
	@Override
	public DateTime getUntilTime();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendar#readRange(lotus.domino.DateTime, lotus.domino.DateTime)
	 */
	@Override
	public String readRange(lotus.domino.DateTime start, lotus.domino.DateTime end);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendar#readRange(lotus.domino.DateTime, lotus.domino.DateTime, int, int)
	 */
	@Override
	public String readRange(lotus.domino.DateTime start, lotus.domino.DateTime end, int skipCount, int maxRead);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendar#setAutoSendNotices(boolean)
	 */
	@Override
	public void setAutoSendNotices(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendar#setReadRangeMask1(int)
	 */
	@Override
	public void setReadRangeMask1(int mask);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendar#setReadRangeMask2(int)
	 */
	@Override
	public void setReadRangeMask2(int mask);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesCalendar#setReadXLotusPropsOutputLevel(int)
	 */
	@Override
	public void setReadXLotusPropsOutputLevel(int level);

}