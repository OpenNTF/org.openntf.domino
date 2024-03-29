/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.DateTime;
import org.openntf.domino.NotesCalendarEntry;
import org.openntf.domino.NotesCalendarNotice;
import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.utils.DominoUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class NotesCalendar.
 */
public class NotesCalendar extends BaseThreadSafe<org.openntf.domino.NotesCalendar, lotus.domino.NotesCalendar, Session>
		implements org.openntf.domino.NotesCalendar {

	/**
	 * Instantiates a new outline.
	 *
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	protected NotesCalendar(final lotus.domino.NotesCalendar delegate, final Session parent) {
		super(delegate, parent, NOTES_OUTLINE);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendar#createEntry(java.lang.String)
	 */
	@Override
	public NotesCalendarEntry createEntry(final String iCalEntry) {
		try {
			return fromLotus(getDelegate().createEntry(iCalEntry), NotesCalendarEntry.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendar#createEntry(java.lang.String, int)
	 */
	@Override
	public NotesCalendarEntry createEntry(final String iCalEntry, final int flags) {
		try {
			return fromLotus(getDelegate().createEntry(iCalEntry, flags), NotesCalendarEntry.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendar#getAutoSendNotices()
	 */
	@Override
	public boolean getAutoSendNotices() {
		try {
			return getDelegate().getAutoSendNotices();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendar#getEntries(lotus.domino.DateTime, lotus.domino.DateTime)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Vector<org.openntf.domino.NotesCalendarEntry> getEntries(final lotus.domino.DateTime start, final lotus.domino.DateTime end) {
		List recycleThis = new ArrayList();
		try {
			lotus.domino.DateTime dt1 = toLotus(start, recycleThis);
			lotus.domino.DateTime dt2 = toLotus(end, recycleThis);
			return fromLotusAsVector(getDelegate().getEntries(dt1, dt2), org.openntf.domino.NotesCalendarEntry.SCHEMA, this);

		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		} finally {
			s_recycle(recycleThis);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendar#getEntries(lotus.domino.DateTime, lotus.domino.DateTime, int, int)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Vector<org.openntf.domino.NotesCalendarEntry> getEntries(final lotus.domino.DateTime start, final lotus.domino.DateTime end,
			final int skipCount, final int maxReturn) {
		List recycleThis = new ArrayList();
		try {
			lotus.domino.DateTime dt1 = toLotus(start, recycleThis);
			lotus.domino.DateTime dt2 = toLotus(end, recycleThis);
			return fromLotusAsVector(getDelegate().getEntries(dt1, dt2, skipCount, maxReturn), org.openntf.domino.NotesCalendarEntry.SCHEMA,
					this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		} finally {
			s_recycle(recycleThis);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendar#getEntriesProcessed()
	 */
	@Override
	public int getEntriesProcessed() {
		try {
			return getDelegate().getEntriesProcessed();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendar#getEntry(java.lang.String)
	 */
	@Override
	public NotesCalendarEntry getEntry(final String uid) {
		try {
			return fromLotus(getDelegate().getEntry(uid), NotesCalendarEntry.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendar#getEntryByNoteID(java.lang.String)
	 */
	@Override
	public NotesCalendarEntry getEntryByNoteID(final String noteid) {
		try {
			return fromLotus(getDelegate().getEntryByNoteID(noteid), NotesCalendarEntry.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendar#getEntryByUNID(java.lang.String)
	 */
	@Override
	public NotesCalendarEntry getEntryByUNID(final String unid) {
		try {
			return fromLotus(getDelegate().getEntryByUNID(unid), NotesCalendarEntry.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendar#getNewInvitations()
	 */
	@Override
	public Vector<org.openntf.domino.NotesCalendarNotice> getNewInvitations() {
		try {
			return fromLotusAsVector(getDelegate().getNewInvitations(), NotesCalendarNotice.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendar#getNewInvitations(lotus.domino.DateTime, lotus.domino.DateTime)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Vector<org.openntf.domino.NotesCalendarNotice> getNewInvitations(final lotus.domino.DateTime start,
			final lotus.domino.DateTime since) {
		List recycleThis = new ArrayList();
		try {
			lotus.domino.DateTime dt1 = toLotus(start, recycleThis);
			lotus.domino.DateTime dt2 = toLotus(since, recycleThis);
			return fromLotusAsVector(getDelegate().getNewInvitations(dt1, dt2), NotesCalendarNotice.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		} finally {
			s_recycle(recycleThis);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendar#getNoticeByUNID(java.lang.String)
	 */
	@Override
	public NotesCalendarNotice getNoticeByUNID(final String unid) {
		try {
			return fromLotus(getDelegate().getNoticeByUNID(unid), NotesCalendarNotice.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public final Session getParent() {
		return parent;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendar#getReadRangeMask1()
	 */
	@Override
	public int getReadRangeMask1() {
		try {
			return getDelegate().getReadRangeMask1();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendar#getReadRangeMask2()
	 */
	@Override
	public int getReadRangeMask2() {
		try {
			return getDelegate().getReadRangeMask2();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendar#getReadXLotusPropsOutputLevel()
	 */
	@Override
	public int getReadXLotusPropsOutputLevel() {
		try {
			return getDelegate().getReadXLotusPropsOutputLevel();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendar#getUntilTime()
	 */
	@Override
	public DateTime getUntilTime() {
		try {
			return fromLotus(getDelegate().getUntilTime(), DateTime.SCHEMA, getAncestorSession());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendar#readRange(lotus.domino.DateTime, lotus.domino.DateTime)
	 */
	@Override
	public String readRange(final lotus.domino.DateTime start, final lotus.domino.DateTime end) {
		@SuppressWarnings("rawtypes")
		List recycleThis = new ArrayList();
		try {
			lotus.domino.DateTime dt1 = toLotus(start, recycleThis);
			lotus.domino.DateTime dt2 = toLotus(end, recycleThis);
			return getDelegate().readRange(dt1, dt2);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		} finally {
			s_recycle(recycleThis);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendar#readRange(lotus.domino.DateTime, lotus.domino.DateTime, int, int)
	 */
	@Override
	public String readRange(final lotus.domino.DateTime start, final lotus.domino.DateTime end, final int skipCount, final int maxRead) {
		@SuppressWarnings("rawtypes")
		List recycleThis = new ArrayList();
		try {
			lotus.domino.DateTime dt1 = toLotus(start, recycleThis);
			lotus.domino.DateTime dt2 = toLotus(end, recycleThis);
			return getDelegate().readRange(dt1, dt2, skipCount, maxRead);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		} finally {
			s_recycle(recycleThis);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendar#setAutoSendNotices(boolean)
	 */
	@Override
	public void setAutoSendNotices(final boolean flag) {
		try {
			getDelegate().setAutoSendNotices(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendar#setReadRangeMask1(int)
	 */
	@Override
	public void setReadRangeMask1(final int mask) {
		try {
			getDelegate().setReadRangeMask1(mask);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendar#setReadRangeMask2(int)
	 */
	@Override
	public void setReadRangeMask2(final int mask) {
		try {
			getDelegate().setReadRangeMask2(mask);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendar#setReadXLotusPropsOutputLevel(int)
	 */
	@Override
	public void setReadXLotusPropsOutputLevel(final int level) {
		try {
			getDelegate().setReadXLotusPropsOutputLevel(level);
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
		return parent;
	}

	/* (non-Javadoc)
	 * @see lotus.domino.NotesCalendar#getApptunidFromUID(java.lang.String)
	 */
	@Override
	public String getApptunidFromUID(final String arg0) {
		try {
			return getDelegate().getApptunidFromUID(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.NotesCalendar#getApptunidFromUID(java.lang.String, boolean)
	 */
	@Override
	@SuppressWarnings("nls")
	public String getApptunidFromUID(final String arg0, final boolean arg1) {
		String result = null;
		Class<?> klazz = getDelegate().getClass();
		try {
			Method chkMethod = klazz.getMethod("getApptunidFromUID", String.class, Boolean.TYPE); //$NON-NLS-1$
			try {
				result = (String) chkMethod.invoke(getDelegate(), arg0, arg1);
			} catch (IllegalArgumentException e) {
				DominoUtils.handleException(e);
			} catch (IllegalAccessException e) {
				DominoUtils.handleException(e);
			} catch (InvocationTargetException e) {
				DominoUtils.handleException(e);
			}
		} catch (SecurityException e) {
			DominoUtils.handleException(e);
		} catch (NoSuchMethodException e) {
			Exception noMethod = new RuntimeException("Method is not available on this version of Domino", e);
			DominoUtils.handleException(noMethod);
		}
		return result;
	}

	@Override
	protected WrapperFactory getFactory() {
		return parent.getFactory();
	}

	@Override
	public boolean getActAsDbOwner() {
		try {
			return getDelegate().getActAsDbOwner();
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean getAutoRemoveProcessedNotices() {
		try {
			return getDelegate().getAutoRemoveProcessedNotices();
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public String getRecurrenceID(final lotus.domino.DateTime arg0) {
		try {
			return getDelegate().getRecurrenceID(arg0);
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public void setActAsDbOwner(final boolean arg0) {
		try {
			getDelegate().setActAsDbOwner(arg0);
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setAutoRemoveProcessedNotices(final boolean arg0) {
		try {
			getDelegate().setAutoRemoveProcessedNotices(arg0);
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
	}

}
