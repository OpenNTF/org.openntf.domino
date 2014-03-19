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

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class Log.
 */
public class Log extends Base<org.openntf.domino.Log, lotus.domino.Log, Session> implements org.openntf.domino.Log {

	/**
	 * Instantiates a new log.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	@Deprecated
	public Log(final lotus.domino.Log delegate, final org.openntf.domino.Base<?> parent) {
		super(delegate, Factory.getSession(parent));
	}

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
	public Log(final lotus.domino.Log delegate, final Session parent, final WrapperFactory wf, final long cppId) {
		super(delegate, parent, wf, cppId, NOTES_AGENTLOG);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.impl.Base#findParent(lotus.domino.Base)
	 */
	@Override
	protected Session findParent(final lotus.domino.Log delegate) throws NotesException {
		return fromLotus(delegate.getParent(), Session.SCHEMA, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Log#close()
	 */
	@Override
	public void close() {
		try {
			getDelegate().close();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Log#getNumActions()
	 */
	@Override
	public int getNumActions() {
		try {
			return getDelegate().getNumActions();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Log#getNumErrors()
	 */
	@Override
	public int getNumErrors() {
		try {
			return getDelegate().getNumErrors();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public Session getParent() {
		return getAncestor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Log#getProgramName()
	 */
	@Override
	public String getProgramName() {
		try {
			return getDelegate().getProgramName();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Log#isLogActions()
	 */
	@Override
	public boolean isLogActions() {
		try {
			return getDelegate().isLogActions();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Log#isLogErrors()
	 */
	@Override
	public boolean isLogErrors() {
		try {
			return getDelegate().isLogErrors();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Log#isOverwriteFile()
	 */
	@Override
	public boolean isOverwriteFile() {
		try {
			return getDelegate().isOverwriteFile();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Log#logAction(java.lang.String)
	 */
	@Override
	public void logAction(final String action) {
		try {
			getDelegate().logAction(action);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Log#logError(int, java.lang.String)
	 */
	@Override
	public void logError(final int code, final String text) {
		try {
			getDelegate().logError(code, text);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Log#logEvent(java.lang.String, java.lang.String, int, int)
	 */
	@Override
	public void logEvent(final String text, final String queue, final int event, final int severity) {
		try {
			getDelegate().logEvent(text, queue, event, severity);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Log#openAgentLog()
	 */
	@Override
	public void openAgentLog() {
		try {
			getDelegate().openAgentLog();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Log#openFileLog(java.lang.String)
	 */
	@Override
	public void openFileLog(final String filePath) {
		try {
			getDelegate().openFileLog(filePath);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Log#openMailLog(java.util.Vector, java.lang.String)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void openMailLog(final Vector recipients, final String subject) {
		List recycleThis = new ArrayList();
		try {
			java.util.Vector v = toDominoFriendly(recipients, this, recycleThis);
			getDelegate().openMailLog(v, subject);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		} finally {
			s_recycle(recycleThis);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Log#openNotesLog(java.lang.String, java.lang.String)
	 */
	@Override
	public void openNotesLog(final String server, final String db) {
		try {
			getDelegate().openNotesLog(server, db);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Log#setLogActions(boolean)
	 */
	@Override
	public void setLogActions(final boolean flag) {
		try {
			getDelegate().setLogActions(flag);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Log#setLogErrors(boolean)
	 */
	@Override
	public void setLogErrors(final boolean flag) {
		try {
			getDelegate().setLogErrors(flag);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Log#setOverwriteFile(boolean)
	 */
	@Override
	public void setOverwriteFile(final boolean flag) {
		try {
			getDelegate().setOverwriteFile(flag);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Log#setProgramName(java.lang.String)
	 */
	@Override
	public void setProgramName(final String name) {
		try {
			getDelegate().setProgramName(name);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public Session getAncestorSession() {
		return this.getParent();
	}
}
