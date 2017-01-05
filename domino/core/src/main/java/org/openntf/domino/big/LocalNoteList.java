package org.openntf.domino.big;

import java.io.Externalizable;
import java.util.Date;

public interface LocalNoteList extends NoteList, Externalizable {
	public String getReplicaIdString();

	public long getReplicaIdLong();

	public NoteList toFullNoteList();

	public Date getBuildDate();

	public void setBuildDate(Date date);

}
