package org.openntf.domino;

import java.util.Vector;

public interface NotesCalendar extends Base<lotus.domino.NotesCalendar>, lotus.domino.NotesCalendar {

	@Override
	public NotesCalendarEntry createEntry(String iCalEntry);

	@Override
	public NotesCalendarEntry createEntry(String iCalEntry, int flags);

	@Override
	public boolean getAutoSendNotices();

	@Override
	public Vector<NotesCalendarEntry> getEntries(lotus.domino.DateTime start, lotus.domino.DateTime end);

	@Override
	public Vector<NotesCalendarEntry> getEntries(lotus.domino.DateTime start, lotus.domino.DateTime end, int skipCount, int maxReturn);

	@Override
	public int getEntriesProcessed();

	@Override
	public NotesCalendarEntry getEntry(String uid);

	@Override
	public NotesCalendarEntry getEntryByNoteID(String noteid);

	@Override
	public NotesCalendarEntry getEntryByUNID(String unid);

	@Override
	public Vector<NotesCalendarNotice> getNewInvitations();

	@Override
	public Vector<NotesCalendarNotice> getNewInvitations(lotus.domino.DateTime start, lotus.domino.DateTime since);

	@Override
	public NotesCalendarNotice getNoticeByUNID(String unid);

	@Override
	public int getReadRangeMask1();

	@Override
	public int getReadRangeMask2();

	@Override
	public int getReadXLotusPropsOutputLevel();

	@Override
	public DateTime getUntilTime();

	@Override
	public String readRange(lotus.domino.DateTime start, lotus.domino.DateTime end);

	@Override
	public String readRange(lotus.domino.DateTime start, lotus.domino.DateTime end, int skipCount, int maxRead);

	@Override
	public void setAutoSendNotices(boolean flag);

	@Override
	public void setReadRangeMask1(int mask);

	@Override
	public void setReadRangeMask2(int mask);

	@Override
	public void setReadXLotusPropsOutputLevel(int level);

}
