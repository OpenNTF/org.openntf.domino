package org.openntf.domino;

import java.util.Vector;

public interface NotesCalendarEntry extends Base<lotus.domino.NotesCalendarEntry>, lotus.domino.NotesCalendarEntry {

	@Override
	public void accept(String comments);

	@Override
	public void accept(String comments, int scope, String recurrenceId);

	@Override
	public void cancel(String comments);

	@Override
	public void cancel(String comments, int scope, String recurrenceId);

	@Override
	public void counter(String comments, lotus.domino.DateTime start, lotus.domino.DateTime end);

	@Override
	public void counter(String comments, lotus.domino.DateTime start, lotus.domino.DateTime end, boolean keepPlaceholder);

	@Override
	public void counter(String comments, lotus.domino.DateTime start, lotus.domino.DateTime end, boolean keepPlaceholder, int scope,
			String recurrenceId);

	@Override
	public void counter(String comments, lotus.domino.DateTime start, lotus.domino.DateTime end, int scope, String recurrenceId);

	@Override
	public void decline(String comments);

	@Override
	public void decline(String comments, boolean keepInformed);

	@Override
	public void decline(String comments, boolean keepInformed, int scope, String recurrenceId);

	@Override
	public void delegate(String commentsToOrganizer, String delegateTo);

	@Override
	public void delegate(String commentsToOrganizer, String delegateTo, boolean keepInformed);

	@Override
	public void delegate(String commentsToOrganizer, String delegateTo, boolean keepInformed, int scope, String recurrenceId);

	@Override
	public void delegate(String commentsToOrganizer, String delegateTo, int scope, String recurrenceId);

	@Override
	public Document getAsDocument();

	@Override
	public Document getAsDocument(int flags);

	@Override
	public Document getAsDocument(int flags, String recurrenceId);

	@Override
	public Vector<NotesCalendarNotice> getNotices();

	@Override
	public String getUID();

	@Override
	public String read();

	@Override
	public String read(String recurrenceId);

	@Override
	public void remove();

	@Override
	public void remove(int scope, String recurrenceId);

	@Override
	public void requestInfo(String comments);

	@Override
	public void tentativelyAccept(String comments);

	@Override
	public void tentativelyAccept(String comments, int scope, String recurrenceId);

	@Override
	public void update(String iCalEntry);

	@Override
	public void update(String iCalEntry, String comments);

	@Override
	public void update(String iCalEntry, String comments, long flags);

	@Override
	public void update(String iCalEntry, String comments, long flags, String recurrenceId);

}
