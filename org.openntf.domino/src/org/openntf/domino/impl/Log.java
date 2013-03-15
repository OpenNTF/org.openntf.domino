package org.openntf.domino.impl;

import java.util.Vector;

public class Log extends Base<org.openntf.domino.Log, lotus.domino.Log> implements org.openntf.domino.Log {

	public Log(lotus.domino.Log delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getNumActions() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumErrors() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Session getParent() {
		return (Session) super.getParent();
	}

	@Override
	public String getProgramName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isLogActions() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLogErrors() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOverwriteFile() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void logAction(String action) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logError(int code, String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logEvent(String text, String queue, int event, int severity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void openAgentLog() {
		// TODO Auto-generated method stub

	}

	@Override
	public void openFileLog(String filePath) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	@Override
	public void openMailLog(Vector recipients, String subject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void openNotesLog(String server, String db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLogActions(boolean flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLogErrors(boolean flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOverwriteFile(boolean flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setProgramName(String name) {
		// TODO Auto-generated method stub

	}
}
