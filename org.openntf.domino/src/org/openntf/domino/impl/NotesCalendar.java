package org.openntf.domino.impl;

import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class NotesCalendar extends Base<org.openntf.domino.NotesCalendar, lotus.domino.NotesCalendar> implements
		org.openntf.domino.NotesCalendar {

	public NotesCalendar(lotus.domino.NotesCalendar delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	@Override
	public NotesCalendarEntry createEntry(String iCalEntry) {
		try {
			return Factory.fromLotus(getDelegate().createEntry(iCalEntry), NotesCalendarEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public NotesCalendarEntry createEntry(String iCalEntry, int flags) {
		try {
			return Factory.fromLotus(getDelegate().createEntry(iCalEntry, flags), NotesCalendarEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public boolean getAutoSendNotices() {
		try {
			return getDelegate().getAutoSendNotices();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public Vector<org.openntf.domino.NotesCalendarEntry> getEntries(lotus.domino.DateTime start, lotus.domino.DateTime end) {
		try {
			return Factory.fromLotusAsVector(getDelegate().getEntries((lotus.domino.DateTime) toLotus(start),
					(lotus.domino.DateTime) toLotus(end)), org.openntf.domino.NotesCalendarEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public Vector<org.openntf.domino.NotesCalendarEntry> getEntries(lotus.domino.DateTime start, lotus.domino.DateTime end, int skipCount,
			int maxReturn) {
		try {
			return Factory.fromLotusAsVector(getDelegate().getEntries((lotus.domino.DateTime) toLotus(start),
					(lotus.domino.DateTime) toLotus(end), skipCount, maxReturn), org.openntf.domino.NotesCalendarEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public int getEntriesProcessed() {
		try {
			return getDelegate().getEntriesProcessed();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public NotesCalendarEntry getEntry(String uid) {
		try {
			return Factory.fromLotus(getDelegate().getEntry(uid), NotesCalendarEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public NotesCalendarEntry getEntryByNoteID(String noteid) {
		try {
			return Factory.fromLotus(getDelegate().getEntryByNoteID(noteid), NotesCalendarEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public NotesCalendarEntry getEntryByUNID(String unid) {
		try {
			return Factory.fromLotus(getDelegate().getEntryByUNID(unid), NotesCalendarEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public Vector<org.openntf.domino.NotesCalendarNotice> getNewInvitations() {
		try {
			return Factory.fromLotusAsVector(getDelegate().getNewInvitations(), NotesCalendarNotice.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public Vector<org.openntf.domino.NotesCalendarNotice> getNewInvitations(lotus.domino.DateTime start, lotus.domino.DateTime since) {
		try {
			return Factory.fromLotusAsVector(getDelegate().getNewInvitations((lotus.domino.DateTime) toLotus(start),
					(lotus.domino.DateTime) toLotus(since)), NotesCalendarNotice.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public NotesCalendarNotice getNoticeByUNID(String unid) {
		try {
			return Factory.fromLotus(getDelegate().getNoticeByUNID(unid), NotesCalendarNotice.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public Session getParent() {
		return (Session) super.getParent();
	}

	@Override
	public int getReadRangeMask1() {
		try {
			return getDelegate().getReadRangeMask1();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int getReadRangeMask2() {
		try {
			return getDelegate().getReadRangeMask2();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int getReadXLotusPropsOutputLevel() {
		try {
			return getDelegate().getReadXLotusPropsOutputLevel();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public org.openntf.domino.DateTime getUntilTime() {
		try {
			return Factory.fromLotus(getDelegate().getUntilTime(), DateTime.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String readRange(lotus.domino.DateTime start, lotus.domino.DateTime end) {
		try {
			return getDelegate().readRange((lotus.domino.DateTime) toLotus(start), (lotus.domino.DateTime) toLotus(end));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String readRange(lotus.domino.DateTime start, lotus.domino.DateTime end, int skipCount, int maxRead) {
		try {
			return getDelegate()
					.readRange((lotus.domino.DateTime) toLotus(start), (lotus.domino.DateTime) toLotus(end), skipCount, maxRead);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public void setAutoSendNotices(boolean flag) {
		try {
			getDelegate().setAutoSendNotices(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setReadRangeMask1(int mask) {
		try {
			getDelegate().setReadRangeMask1(mask);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setReadRangeMask2(int mask) {
		try {
			getDelegate().setReadRangeMask2(mask);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setReadXLotusPropsOutputLevel(int level) {
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
	public org.openntf.domino.Session getAncestorSession() {
		return this.getParent();
	}
}
