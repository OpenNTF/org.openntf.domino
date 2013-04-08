/*
 * Copyright OpenNTF 2013
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
import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.types.DatabaseDescendant;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class ViewEntry.
 */
public class ViewEntry extends Base<org.openntf.domino.ViewEntry, lotus.domino.ViewEntry> implements org.openntf.domino.ViewEntry {
	private static final Logger log_ = Logger.getLogger(ViewEntry.class.getName());

	/**
	 * Instantiates a new view entry.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	public ViewEntry(lotus.domino.ViewEntry delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#getChildCount()
	 */
	@Override
	public int getChildCount() {
		try {
			return getDelegate().getChildCount();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#getColumnIndentLevel()
	 */
	@Override
	public int getColumnIndentLevel() {
		try {
			return getDelegate().getColumnIndentLevel();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#getColumnValues()
	 */
	@Override
	public Vector<Object> getColumnValues() {
		try {
			return Factory.wrapColumnValues(getDelegate().getColumnValues());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#getDescendantCount()
	 */
	@Override
	public int getDescendantCount() {
		try {
			return getDelegate().getDescendantCount();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#getDocument()
	 */
	@Override
	public Document getDocument() {
		try {
			return Factory.fromLotus(getDelegate().getDocument(), Document.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#getFTSearchScore()
	 */
	@Override
	public int getFTSearchScore() {
		try {
			return getDelegate().getFTSearchScore();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#getIndentLevel()
	 */
	@Override
	public int getIndentLevel() {
		try {
			return getDelegate().getIndentLevel();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#getNoteID()
	 */
	@Override
	public String getNoteID() {
		try {
			return getDelegate().getNoteID();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#getNoteIDAsInt()
	 */
	@Override
	public int getNoteIDAsInt() {
		try {
			return getDelegate().getNoteIDAsInt();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public org.openntf.domino.Base<?> getParent() {
		return super.getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#getPosition(char)
	 */
	@Override
	public String getPosition(char separator) {
		try {
			return getDelegate().getPosition(separator);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#getRead()
	 */
	@Override
	public boolean getRead() {
		try {
			return getDelegate().getRead();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#getRead(java.lang.String)
	 */
	@Override
	public boolean getRead(String userName) {
		try {
			return getDelegate().getRead(userName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#getSiblingCount()
	 */
	@Override
	public int getSiblingCount() {
		try {
			return getDelegate().getSiblingCount();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#getUniversalID()
	 */
	@Override
	public String getUniversalID() {
		try {
			return getDelegate().getUniversalID();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#isCategory()
	 */
	@Override
	public boolean isCategory() {
		try {
			return getDelegate().isCategory();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#isConflict()
	 */
	@Override
	public boolean isConflict() {
		try {
			return getDelegate().isConflict();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#isDocument()
	 */
	@Override
	public boolean isDocument() {
		try {
			return getDelegate().isDocument();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#isPreferJavaDates()
	 */
	@Override
	public boolean isPreferJavaDates() {
		try {
			return getDelegate().isPreferJavaDates();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#isTotal()
	 */
	@Override
	public boolean isTotal() {
		try {
			return getDelegate().isTotal();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#isValid()
	 */
	@Override
	public boolean isValid() {
		try {
			return getDelegate().isValid();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#setPreferJavaDates(boolean)
	 */
	@Override
	public void setPreferJavaDates(boolean flag) {
		try {
			getDelegate().setPreferJavaDates(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.DatabaseDescendant#getAncestorDatabase()
	 */
	@Override
	public Database getAncestorDatabase() {
		return (Database) ((DatabaseDescendant) this.getParent()).getAncestorDatabase();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public Session getAncestorSession() {
		return this.getAncestorDatabase().getAncestorSession();
	}
}
