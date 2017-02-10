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
 * The Interface NotesCalendar.
 */
/**
 * @author Paul Withers
 *
 */
public interface NotesCalendar
		extends Base<lotus.domino.NotesCalendar>, lotus.domino.NotesCalendar, org.openntf.domino.ext.NotesCalendar, SessionDescendant {

	public static class Schema extends FactorySchema<NotesCalendar, lotus.domino.NotesCalendar, Session> {
		@Override
		public Class<NotesCalendar> typeClass() {
			return NotesCalendar.class;
		}

		@Override
		public Class<lotus.domino.NotesCalendar> delegateClass() {
			return lotus.domino.NotesCalendar.class;
		}

		@Override
		public Class<Session> parentClass() {
			return Session.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.NotesCalendar#createEntry(java.lang.String)
	 */
	@Override
	public NotesCalendarEntry createEntry(final String iCalEntry);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.NotesCalendar#createEntry(java.lang.String, int)
	 */
	@Override
	public NotesCalendarEntry createEntry(final String iCalEntry, final int flags);

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
	public Vector<NotesCalendarEntry> getEntries(final lotus.domino.DateTime start, final lotus.domino.DateTime end);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.NotesCalendar#getEntries(lotus.domino.DateTime, lotus.domino.DateTime, int, int)
	 */
	@Override
	public Vector<NotesCalendarEntry> getEntries(final lotus.domino.DateTime start, final lotus.domino.DateTime end, final int skipCount,
			final int maxReturn);

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
	public NotesCalendarEntry getEntry(final String uid);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.NotesCalendar#getEntryByNoteID(java.lang.String)
	 */
	@Override
	public NotesCalendarEntry getEntryByNoteID(final String noteid);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.NotesCalendar#getEntryByUNID(java.lang.String)
	 */
	@Override
	public NotesCalendarEntry getEntryByUNID(final String unid);

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
	public Vector<NotesCalendarNotice> getNewInvitations(final lotus.domino.DateTime start, final lotus.domino.DateTime since);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.NotesCalendar#getNoticeByUNID(java.lang.String)
	 */
	@Override
	public NotesCalendarNotice getNoticeByUNID(final String unid);

	/**
	 * Gets the parent.
	 *
	 * @return the parent
	 */
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
	public String readRange(final lotus.domino.DateTime start, final lotus.domino.DateTime end);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.NotesCalendar#readRange(lotus.domino.DateTime, lotus.domino.DateTime, int, int)
	 */
	@Override
	public String readRange(final lotus.domino.DateTime start, final lotus.domino.DateTime end, final int skipCount, final int maxRead);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.NotesCalendar#setAutoSendNotices(boolean)
	 */
	@Override
	public void setAutoSendNotices(final boolean flag);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.NotesCalendar#setReadRangeMask1(int)
	 */
	@Override
	public void setReadRangeMask1(final int mask);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.NotesCalendar#setReadRangeMask2(int)
	 */
	@Override
	public void setReadRangeMask2(final int mask);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.NotesCalendar#setReadXLotusPropsOutputLevel(int)
	 */
	@Override
	public void setReadXLotusPropsOutputLevel(final int level);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.NotesCalendar#getApptunidFromUID(java.lang.String, boolean)
	 */
	@Override
	public String getApptunidFromUID(String arg0, boolean arg1);

	/* (non-Javadoc)
	 * @see lotus.domino.NotesCalendar#getActAsDbOwner()
	 * @since Domino 9.0.1 FP8
	 */
	@Override
	public boolean getActAsDbOwner();

	/* (non-Javadoc)
	 * @see lotus.domino.NotesCalendar#getAutoRemoveProcessedNotices()
	 * @since Domino 9.0.1 FP8
	 */
	@Override
	public boolean getAutoRemoveProcessedNotices();

	/* (non-Javadoc)
	 * @see lotus.domino.NotesCalendar#getRecurrenceID(lotus.domino.DateTime)
	 * @since Domino 9.0.1 FP8
	 */
	@Override
	public String getRecurrenceID(lotus.domino.DateTime arg0);

	/* (non-Javadoc)
	 * @see lotus.domino.NotesCalendar#setActAsDbOwner(boolean)
	 * @since Domino 9.0.1 FP8
	 */
	@Override
	public void setActAsDbOwner(boolean arg0);

	/* (non-Javadoc)
	 * @see lotus.domino.NotesCalendar#setAutoRemoveProcessedNotices(boolean)
	 * @since Domino 9.0.1 FP8
	 */
	@Override
	public void setAutoRemoveProcessedNotices(boolean arg0);

}
