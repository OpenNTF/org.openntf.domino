/*
 * Copyright 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package org.openntf.domino.impl;

import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.Database;
import org.openntf.domino.DateTime;
import org.openntf.domino.ReplicationEntry;
import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.utils.DominoUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class Replication.
 */
public class Replication extends BaseNonThreadSafe<org.openntf.domino.Replication, lotus.domino.Replication, Database> implements
		org.openntf.domino.Replication {

	/**
	 * Instantiates a new outline.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 * @param wf
	 *            the wrapperfactory
	 * @param cppId
	 *            the cpp-id
	 */
	public Replication(final lotus.domino.Replication delegate, final Database parent, final WrapperFactory wf, final long cppId) {
		super(delegate, parent, wf, cppId, NOTES_REPLICATION);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Replication#clearHistory()
	 */
	@Override
	public int clearHistory() {
		try {
			return getDelegate().clearHistory();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Replication#getCutoffDate()
	 */
	@Override
	public DateTime getCutoffDate() {
		try {
			return fromLotus(getDelegate().getCutoffDate(), DateTime.SCHEMA, getAncestorSession());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Replication#getCutoffInterval()
	 */
	@Override
	public long getCutoffInterval() {
		try {
			return getDelegate().getCutoffInterval();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Replication#getDontSendLocalSecurityUpdates()
	 */
	@Override
	public boolean getDontSendLocalSecurityUpdates() {
		try {
			return getDelegate().getDontSendLocalSecurityUpdates();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Replication#getEntries()
	 */
	@Override
	public Vector<org.openntf.domino.ReplicationEntry> getEntries() {
		try {
			return fromLotusAsVector(getDelegate().getEntries(), org.openntf.domino.ReplicationEntry.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Replication#getEntry(java.lang.String, java.lang.String)
	 */
	@Override
	public ReplicationEntry getEntry(final String source, final String destination) {
		try {
			return fromLotus(getDelegate().getEntry(source, destination), ReplicationEntry.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Replication#getEntry(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public ReplicationEntry getEntry(final String source, final String destination, final boolean createFlag) {
		try {
			return fromLotus(getDelegate().getEntry(source, destination, createFlag), ReplicationEntry.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public final Database getParent() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Replication#getPriority()
	 */
	@Override
	public int getPriority() {
		try {
			return getDelegate().getPriority();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Replication#isAbstract()
	 */
	@Override
	public boolean isAbstract() {
		try {
			return getDelegate().isAbstract();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Replication#isCutoffDelete()
	 */
	@Override
	public boolean isCutoffDelete() {
		try {
			return getDelegate().isCutoffDelete();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Replication#isDisabled()
	 */
	@Override
	public boolean isDisabled() {
		try {
			return getDelegate().isDisabled();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Replication#isIgnoreDeletes()
	 */
	@Override
	public boolean isIgnoreDeletes() {
		try {
			return getDelegate().isIgnoreDeletes();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Replication#isIgnoreDestDeletes()
	 */
	@Override
	public boolean isIgnoreDestDeletes() {
		try {
			return getDelegate().isIgnoreDestDeletes();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Replication#reset()
	 */
	@Override
	public int reset() {
		try {
			return getDelegate().reset();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Replication#save()
	 */
	@Override
	public int save() {
		try {
			return getDelegate().save();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Replication#setAbstract(boolean)
	 */
	@Override
	public void setAbstract(final boolean flag) {
		try {
			getDelegate().setAbstract(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Replication#setCutoffDelete(boolean)
	 */
	@Override
	public void setCutoffDelete(final boolean flag) {
		try {
			getDelegate().setCutoffDelete(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Replication#setCutoffInterval(long)
	 */
	@Override
	public void setCutoffInterval(final long interval) {
		try {
			getDelegate().setCutoffInterval(interval);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Replication#setDisabled(boolean)
	 */
	@Override
	public void setDisabled(final boolean flag) {
		try {
			getDelegate().setDisabled(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		((org.openntf.domino.impl.Database) getAncestorDatabase()).setReplication(flag);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Replication#setDontSendLocalSecurityUpdates(boolean)
	 */
	@Override
	public void setDontSendLocalSecurityUpdates(final boolean flag) {
		try {
			getDelegate().setDontSendLocalSecurityUpdates(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Replication#setIgnoreDeletes(boolean)
	 */
	@Override
	public void setIgnoreDeletes(final boolean flag) {
		try {
			getDelegate().setIgnoreDeletes(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Replication#setIgnoreDestDeletes(boolean)
	 */
	@Override
	public void setIgnoreDestDeletes(final boolean flag) {
		try {
			getDelegate().setIgnoreDestDeletes(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Replication#setPriority(int)
	 */
	@Override
	public void setPriority(final int priority) {
		try {
			getDelegate().setPriority(priority);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	public final Database getParentDatabase() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.DatabaseDescendant#getAncestorDatabase()
	 */
	@Override
	public final Database getAncestorDatabase() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public final Session getAncestorSession() {
		return this.getAncestorDatabase().getAncestorSession();
	}

	@Override
	protected WrapperFactory getFactory() {
		return parent.getAncestorSession().getFactory();
	}

}
