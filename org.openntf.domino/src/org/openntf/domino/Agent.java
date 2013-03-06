package org.openntf.domino;

import java.util.Vector;

import lotus.domino.Database;
import lotus.domino.DateTime;
import lotus.domino.Document;

public interface Agent extends Base<lotus.domino.Agent>, lotus.domino.Agent {

	@Override
	public String getComment();

	@Override
	public String getCommonOwner();

	@Override
	public lotus.domino.Agent getDelegate();

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
	public boolean lock(boolean arg0);

	@Override
	public boolean lock(String arg0);

	@Override
	public boolean lock(String arg0, boolean arg1);

	@Override
	public boolean lock(Vector arg0);

	@Override
	public boolean lock(Vector arg0, boolean arg1);

	@Override
	public boolean lockProvisional();

	@Override
	public boolean lockProvisional(String arg0);

	@Override
	public boolean lockProvisional(Vector arg0);

	@Override
	public void recycle();

	@Override
	public void recycle(Vector arg0);

	@Override
	public void remove();

	@Override
	public void run();

	@Override
	public void run(String arg0);

	@Override
	public int runOnServer();

	@Override
	public int runOnServer(String arg0);

	@Override
	public void runWithDocumentContext(Document arg0);

	@Override
	public void runWithDocumentContext(Document arg0, String arg1);

	@Override
	public void save();

	@Override
	public void setEnabled(boolean arg0);

	@Override
	public void setProhibitDesignUpdate(boolean arg0);

	@Override
	public void setServerName(String arg0);

	@Override
	public void unlock();

	@Override
	public boolean equals(Object obj);

	@Override
	public int hashCode();

	@Override
	public String toString();

}
