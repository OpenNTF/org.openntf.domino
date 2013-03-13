package org.openntf.domino;

import java.util.Vector;

public interface Agent extends Base<lotus.domino.Agent>, lotus.domino.Agent {

	@Override
	public String getComment();

	@Override
	public String getCommonOwner();

	@Override
	public String getHttpURL();

	@Override
	public DateTime getLastRun();

	@Override
	public Vector getLockHolders();

	@Override
	public String getName();

	@Override
	public String getNotesURL();

	@Override
	public String getOnBehalfOf();

	@Override
	public String getOwner();

	@Override
	public String getParameterDocID();

	@Override
	public Database getParent();

	@Override
	public String getQuery();

	@Override
	public String getServerName();

	@Override
	public int getTarget();

	@Override
	public int getTrigger();

	@Override
	public String getURL();

	@Override
	public boolean isActivatable();

	@Override
	public boolean isEnabled();

	@Override
	public boolean isNotesAgent();

	@Override
	public boolean isProhibitDesignUpdate();

	@Override
	public boolean isPublic();

	@Override
	public boolean isWebAgent();

	@Override
	public boolean lock();

	@Override
	public boolean lock(boolean provisionalOk);

	@Override
	public boolean lock(String name);

	@Override
	public boolean lock(String name, boolean provisionalOk);

	@Override
	public boolean lock(Vector names);

	@Override
	public boolean lock(Vector names, boolean provisionalOk);

	@Override
	public boolean lockProvisional();

	@Override
	public boolean lockProvisional(String name);

	@Override
	public boolean lockProvisional(Vector names);

	@Override
	public void remove();

	@Override
	public void run();

	@Override
	public void run(String noteid);

	@Override
	public int runOnServer();

	@Override
	public int runOnServer(String noteid);

	@Override
	public void runWithDocumentContext(lotus.domino.Document doc);

	@Override
	public void runWithDocumentContext(lotus.domino.Document doc, String noteid);

	@Override
	public void save();

	@Override
	public void setEnabled(boolean flag);

	@Override
	public void setProhibitDesignUpdate(boolean flag);

	@Override
	public void setServerName(String server);

	@Override
	public void unlock();

}
