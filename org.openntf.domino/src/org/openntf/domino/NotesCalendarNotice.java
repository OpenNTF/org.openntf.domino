package org.openntf.domino;

import java.util.Vector;

public interface NotesCalendarNotice extends Base<lotus.domino.NotesCalendarNotice>, lotus.domino.NotesCalendarNotice {

	@Override
	public void accept(String comments);

	@Override
	public void acceptCounter(String comments);

	@Override
	public void counter(String comments, lotus.domino.DateTime start, lotus.domino.DateTime end);

	@Override
	public void counter(String comments, lotus.domino.DateTime start, lotus.domino.DateTime end, boolean keepPlaceholder);

	@Override
	public void decline(String comments);

	@Override
	public void decline(String comments, boolean keepInformed);

	@Override
	public void declineCounter(String comments);

	@Override
	public void delegate(String commentsToOrganizer, String delegateTo);

	@Override
	public void delegate(String commentsToOrganizer, String delegateTo, boolean keepInformed);

	@Override
	public Document getAsDocument();

	@Override
	public String getNoteID();

	@Override
	public Vector<NotesCalendarNotice> getOutstandingInvitations();

	@Override
	public String getUNID();

	@Override
	public boolean isOverwriteCheckEnabled();

	@Override
	public String read();

	@Override
	public void removeCancelled();

	@Override
	public void requestInfo(String comments);

	@Override
	public void sendUpdatedInfo(String comments);

	@Override
	public void setOverwriteCheckEnabled(boolean flag);

	@Override
	public void tentativelyAccept(String comments);
}
