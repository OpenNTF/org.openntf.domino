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
	public void logAction(String action);

	@Override
	public void logError(int code, String text);

	@Override
	public void logEvent(String text, String queue, int event, int severity);

	@Override
	public void openAgentLog();

	@Override
	public void openFileLog(String filePath);

	@Override
	public void openMailLog(Vector recipients, String subject);

	@Override
	public void openNotesLog(String server, String db);

	@Override
	public void setLogActions(boolean flag);

	@Override
	public void setLogErrors(boolean flag);

	@Override
	public void setOverwriteFile(boolean flag);

	@Override
	public void setProgramName(String name);

}
