package org.openntf.domino.impl;

import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class Replication extends Base<org.openntf.domino.Replication, lotus.domino.Replication> implements org.openntf.domino.Replication {

	public Replication(lotus.domino.Replication delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	@Override
	public int clearHistory() {
		try {
			return getDelegate().clearHistory();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public DateTime getCutoffDate() {
		try {
			return Factory.fromLotus(getDelegate().getCutoffDate(), DateTime.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public long getCutoffInterval() {
		try {
			return getDelegate().getCutoffInterval();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public boolean getDontSendLocalSecurityUpdates() {
		try {
			return getDelegate().getDontSendLocalSecurityUpdates();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vector<org.openntf.domino.ReplicationEntry> getEntries() {
		try {
			Vector<org.openntf.domino.ReplicationEntry> result = new Vector<org.openntf.domino.ReplicationEntry>();
			java.util.Vector<lotus.domino.ReplicationEntry> entries = (java.util.Vector<lotus.domino.ReplicationEntry>) getDelegate()
					.getEntries();
			for (lotus.domino.ReplicationEntry entry : entries) {
				result.add((ReplicationEntry) Factory.fromLotus(entry, ReplicationEntry.class, this));
			}
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public ReplicationEntry getEntry(String source, String destination) {
		try {
			return Factory.fromLotus(getDelegate().getEntry(source, destination), ReplicationEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public ReplicationEntry getEntry(String source, String destination, boolean createFlag) {
		try {
			return Factory.fromLotus(getDelegate().getEntry(source, destination, createFlag), ReplicationEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public int getPriority() {
		try {
			return getDelegate().getPriority();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public boolean isAbstract() {
		try {
			return getDelegate().isAbstract();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isCutoffDelete() {
		try {
			return getDelegate().isCutoffDelete();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isDisabled() {
		try {
			return getDelegate().isDisabled();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isIgnoreDeletes() {
		try {
			return getDelegate().isIgnoreDeletes();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isIgnoreDestDeletes() {
		try {
			return getDelegate().isIgnoreDestDeletes();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public int reset() {
		try {
			return getDelegate().reset();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int save() {
		try {
			return getDelegate().save();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public void setAbstract(boolean flag) {
		try {
			getDelegate().setAbstract(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setCutoffDelete(boolean flag) {
		try {
			getDelegate().setCutoffDelete(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setCutoffInterval(long interval) {
		try {
			getDelegate().setCutoffInterval(interval);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setDisabled(boolean flag) {
		try {
			getDelegate().setDisabled(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setDontSendLocalSecurityUpdates(boolean flag) {
		try {
			getDelegate().setDontSendLocalSecurityUpdates(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setIgnoreDeletes(boolean flag) {
		try {
			getDelegate().setIgnoreDeletes(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setIgnoreDestDeletes(boolean flag) {
		try {
			getDelegate().setIgnoreDestDeletes(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setPriority(int priority) {
		try {
			getDelegate().setPriority(priority);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}
}
