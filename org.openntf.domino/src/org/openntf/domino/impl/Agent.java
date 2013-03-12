package org.openntf.domino.impl;

import java.util.Vector;

import lotus.domino.Document;
import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class Agent extends Base<org.openntf.domino.Agent, lotus.domino.Agent> implements org.openntf.domino.Agent {

	protected Agent(lotus.domino.Agent delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, (parent instanceof org.openntf.domino.Session) ? parent : Factory.getSession(parent));
	}

	@Override
	public String getComment() {
		try {
			return getDelegate().getComment();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getCommonOwner() {
		try {
			return getDelegate().getCommonOwner();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getHttpURL() {
		try {
			return getDelegate().getHttpURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public DateTime getLastRun() {
		try {
			return Factory.fromLotus(getDelegate().getLastRun(), DateTime.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public Vector getLockHolders() {
		try {
			return getDelegate().getLockHolders();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getName() {
		try {
			return getDelegate().getName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getNotesURL() {
		try {
			return getDelegate().getNotesURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getOnBehalfOf() {
		try {
			return getDelegate().getOnBehalfOf();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getOwner() {
		try {
			return getDelegate().getOwner();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getParameterDocID() {
		try {
			return getDelegate().getParameterDocID();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public org.openntf.domino.Database getParent() {
		return (org.openntf.domino.Database) super.getParent();
	}

	@Override
	public String getQuery() {
		try {
			return getDelegate().getQuery();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getServerName() {
		try {
			return getDelegate().getServerName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public int getTarget() {
		try {
			return getDelegate().getTarget();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int getTrigger() {
		try {
			return getDelegate().getTrigger();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public String getURL() {
		try {
			return getDelegate().getURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public boolean isActivatable() {
		try {
			return getDelegate().isActivatable();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isEnabled() {
		try {
			return getDelegate().isEnabled();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isNotesAgent() {
		try {
			return getDelegate().isNotesAgent();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isProhibitDesignUpdate() {
		try {
			return getDelegate().isProhibitDesignUpdate();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isPublic() {
		try {
			return getDelegate().isPublic();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isWebAgent() {
		try {
			return getDelegate().isWebAgent();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean lock() {
		try {
			return getDelegate().lock();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean lock(boolean arg0) {
		try {
			return getDelegate().lock(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean lock(String arg0) {
		try {
			return getDelegate().lock(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean lock(String arg0, boolean arg1) {
		try {
			return getDelegate().lock(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean lock(Vector arg0) {
		try {
			return getDelegate().lock(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean lock(Vector arg0, boolean arg1) {
		try {
			return getDelegate().lock(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean lockProvisional() {
		try {
			return getDelegate().lockProvisional();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean lockProvisional(String arg0) {
		try {
			return getDelegate().lockProvisional(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean lockProvisional(Vector arg0) {
		try {
			return getDelegate().lockProvisional(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public void remove() {
		try {
			getDelegate().remove();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void run() {
		try {
			getDelegate().run();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void run(String arg0) {
		try {
			getDelegate().run(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public int runOnServer() {
		try {
			return getDelegate().runOnServer();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int runOnServer(String arg0) {
		try {
			return getDelegate().runOnServer(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public void runWithDocumentContext(Document arg0) {
		try {
			getDelegate().runWithDocumentContext(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void runWithDocumentContext(Document arg0, String arg1) {
		try {
			getDelegate().runWithDocumentContext(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void save() {
		try {
			getDelegate().save();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setEnabled(boolean arg0) {
		try {
			getDelegate().setEnabled(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setProhibitDesignUpdate(boolean arg0) {
		try {
			getDelegate().setProhibitDesignUpdate(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setServerName(String arg0) {
		try {
			getDelegate().setServerName(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void unlock() {
		// TODO Auto-generated method stub

	}

}