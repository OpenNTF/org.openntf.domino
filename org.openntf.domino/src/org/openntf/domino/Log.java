package org.openntf.domino;

import java.util.Vector;

import lotus.domino.Session;

public interface Log extends Base<lotus.domino.Log>, lotus.domino.Log {

	@Override
	public void close();

	@Override
	public int getNumActions();

	@Override
	public int getNumErrors();

	@Override
	public Session getParent();

	@Override
	public String getProgramName();

	@Override
	public boolean isLogActions();

	@Override
	public boolean isLogErrors();

	@Override
	public boolean isOverwriteFile();

	@Override
	public void logAction(String arg0);

	@Override
	public void logError(int arg0, String arg1);

	@Override
	public void logEvent(String arg0, String arg1, int arg2, int arg3);

	@Override
	public void openAgentLog();

	@Override
	public void openFileLog(String arg0);

	@Override
	public void openMailLog(Vector arg0, String arg1);

	@Override
	public void openNotesLog(String arg0, String arg1);

	@Override
	public void recycle();

	@Override
	public void recycle(Vector arg0);

	@Override
	public void setLogActions(boolean arg0);

	@Override
	public void setLogErrors(boolean arg0);

	@Override
	public void setOverwriteFile(boolean arg0);

	@Override
	public void setProgramName(String arg0);

	@Override
	public boolean equals(Object obj);

	@Override
	public int hashCode();

	@Override
	public String toString();

}
