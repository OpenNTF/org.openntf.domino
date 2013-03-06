package org.openntf.domino;

import java.util.Vector;

import lotus.domino.DateTime;
import lotus.domino.NotesException;
import lotus.domino.ReplicationEntry;

public interface Replication extends Base<lotus.domino.Replication>, lotus.domino.Replication {

	@Override
	public int clearHistory() throws NotesException;

	@Override
	public DateTime getCutoffDate() throws NotesException;

	@Override
	public long getCutoffInterval() throws NotesException;

	@Override
	public lotus.domino.Replication getDelegate();

	@Override
	public boolean getDontSendLocalSecurityUpdates() throws NotesException;

	@Override
	public Vector getEntries() throws NotesException;

	@Override
	public ReplicationEntry getEntry(String arg0, String arg1) throws NotesException;

	@Override
	public ReplicationEntry getEntry(String arg0, String arg1, boolean arg2) throws NotesException;

	@Override
	public int getPriority() throws NotesException;

	@Override
	public boolean isAbstract() throws NotesException;

	@Override
	public boolean isCutoffDelete() throws NotesException;

	@Override
	public boolean isDisabled() throws NotesException;

	@Override
	public boolean isIgnoreDeletes() throws NotesException;

	@Override
	public boolean isIgnoreDestDeletes() throws NotesException;

	@Override
	public void recycle() throws NotesException;

	@Override
	public void recycle(Vector arg0) throws NotesException;

	@Override
	public int reset() throws NotesException;

	@Override
	public int save() throws NotesException;

	@Override
	public void setAbstract(boolean arg0) throws NotesException;

	@Override
	public void setCutoffDelete(boolean arg0) throws NotesException;

	@Override
	public void setCutoffInterval(long arg0) throws NotesException;

	@Override
	public void setDisabled(boolean arg0) throws NotesException;

	@Override
	public void setDontSendLocalSecurityUpdates(boolean arg0) throws NotesException;

	@Override
	public void setIgnoreDeletes(boolean arg0) throws NotesException;

	@Override
	public void setIgnoreDestDeletes(boolean arg0) throws NotesException;

	@Override
	public void setPriority(int arg0) throws NotesException;

	@Override
	public boolean equals(Object obj);

	@Override
	public int hashCode();

	@Override
	public String toString();

}
