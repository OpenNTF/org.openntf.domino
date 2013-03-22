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

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class Newsletter.
 */
public class Newsletter extends Base<org.openntf.domino.Newsletter, lotus.domino.Newsletter> implements org.openntf.domino.Newsletter {

	/**
	 * Instantiates a new newsletter.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	public Newsletter(lotus.domino.Newsletter delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Newsletter#formatDocument(lotus.domino.Database, int)
	 */
	@Override
	public Document formatDocument(lotus.domino.Database db, int index) {
		try {
			return Factory.fromLotus(getDelegate().formatDocument((lotus.domino.Database) toLotus(db), index), Document.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Newsletter#formatMsgWithDoclinks(lotus.domino.Database)
	 */
	@Override
	public Document formatMsgWithDoclinks(lotus.domino.Database db) {
		try {
			return Factory.fromLotus(getDelegate().formatMsgWithDoclinks((lotus.domino.Database) toLotus(db)), Document.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public Session getParent() {
		return (Session) super.getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Newsletter#getSubjectItemName()
	 */
	@Override
	public String getSubjectItemName() {
		try {
			return getDelegate().getSubjectItemName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Newsletter#isDoScore()
	 */
	@Override
	public boolean isDoScore() {
		try {
			return getDelegate().isDoScore();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Newsletter#isDoSubject()
	 */
	@Override
	public boolean isDoSubject() {
		try {
			return getDelegate().isDoSubject();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Newsletter#setDoScore(boolean)
	 */
	@Override
	public void setDoScore(boolean flag) {
		try {
			getDelegate().setDoScore(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Newsletter#setDoSubject(boolean)
	 */
	@Override
	public void setDoSubject(boolean flag) {
		try {
			getDelegate().setDoSubject(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Newsletter#setSubjectItemName(java.lang.String)
	 */
	@Override
	public void setSubjectItemName(String name) {
		try {
			getDelegate().setSubjectItemName(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}
}
