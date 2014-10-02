package org.openntf.domino.big;

import java.util.Date;
import java.util.List;

import org.openntf.domino.Session;

public interface DbMeta extends BaseMeta {

	public String getReplid();

	public org.openntf.domino.Database getDatabase(Session session, String server);

	public Date getLastModifiedDate();

	public String getTitle();

	public NoteSequence getDefaultSequence();

	public List<NoteSequence> getSequences();

}
