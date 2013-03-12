package org.openntf.domino;

import java.util.Vector;

import lotus.domino.DateTime;
import lotus.domino.ReplicationEntry;

public interface Replication extends Base<lotus.domino.Replication>, lotus.domino.Replication {

	@Override
	public int clearHistory();

	@Override
	public DateTime getCutoffDate();

	@Override
	public long getCutoffInterval();

	@Override
	public boolean getDontSendLocalSecurityUpdates();

	@Override
	public Vector<lotus.domino.ReplicationEntry> getEntries();

	@Override
	public ReplicationEntry getEntry(String source, String destination);

	@Override
	public ReplicationEntry getEntry(String source, String destination, boolean createFlag);

	@Override
	public int getPriority();

	@Override
	public boolean isAbstract();

	@Override
	public boolean isCutoffDelete();

	@Override
	public boolean isDisabled();

	@Override
	public boolean isIgnoreDeletes();

	@Override
	public boolean isIgnoreDestDeletes();

	@Override
	public int reset();

	@Override
	public int save();

	@Override
	public void setAbstract(boolean flag);

	@Override
	public void setCutoffDelete(boolean flag);

	@Override
	public void setCutoffInterval(long interval);

	@Override
	public void setDisabled(boolean flag);

	@Override
	public void setDontSendLocalSecurityUpdates(boolean flag);

	@Override
	public void setIgnoreDeletes(boolean flag);

	@Override
	public void setIgnoreDestDeletes(boolean flag);

	@Override
	public void setPriority(int priority);

}
