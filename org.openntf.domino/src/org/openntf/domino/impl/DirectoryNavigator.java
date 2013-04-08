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

import lotus.domino.NotesException;

import org.openntf.domino.Session;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class DirectoryNavigator.
 */
public class DirectoryNavigator extends Base<org.openntf.domino.DirectoryNavigator, lotus.domino.DirectoryNavigator> implements
		org.openntf.domino.DirectoryNavigator {

	/**
	 * Instantiates a new directory navigator.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	public DirectoryNavigator(lotus.domino.DirectoryNavigator delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DirectoryNavigator#findFirstMatch()
	 */
	@Override
	public boolean findFirstMatch() {
		try {
			return getDelegate().findFirstMatch();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DirectoryNavigator#findFirstName()
	 */
	@Override
	public long findFirstName() {
		try {
			return getDelegate().findFirstName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DirectoryNavigator#findNextMatch()
	 */
	@Override
	public boolean findNextMatch() {
		try {
			return getDelegate().findNextMatch();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DirectoryNavigator#findNextName()
	 */
	@Override
	public long findNextName() {
		try {
			return getDelegate().findNextName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DirectoryNavigator#findNthMatch(long)
	 */
	@Override
	public boolean findNthMatch(long n) {
		try {
			return getDelegate().findNthMatch(n);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DirectoryNavigator#findNthName(int)
	 */
	@Override
	public long findNthName(int n) {
		try {
			return getDelegate().findNthName(n);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DirectoryNavigator#getCurrentItem()
	 */
	@Override
	public String getCurrentItem() {
		try {
			return getDelegate().getCurrentItem();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DirectoryNavigator#getCurrentMatch()
	 */
	@Override
	public long getCurrentMatch() {
		try {
			return getDelegate().getCurrentMatch();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DirectoryNavigator#getCurrentMatches()
	 */
	@Override
	public long getCurrentMatches() {
		try {
			return getDelegate().getCurrentMatches();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DirectoryNavigator#getCurrentName()
	 */
	@Override
	public String getCurrentName() {
		try {
			return getDelegate().getCurrentName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DirectoryNavigator#getCurrentView()
	 */
	@Override
	public String getCurrentView() {
		try {
			return getDelegate().getCurrentView();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DirectoryNavigator#getFirstItemValue()
	 */
	@Override
	public Vector<Object> getFirstItemValue() {
		try {
			return Factory.wrapColumnValues(getDelegate().getFirstItemValue(), this.getAncestorSession());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DirectoryNavigator#getNextItemValue()
	 */
	@Override
	public Vector<Object> getNextItemValue() {
		try {
			return Factory.wrapColumnValues(getDelegate().getNextItemValue(), this.getAncestorSession());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DirectoryNavigator#getNthItemValue(int)
	 */
	@Override
	public Vector<Object> getNthItemValue(int n) {
		try {
			return Factory.wrapColumnValues(getDelegate().getNthItemValue(n), this.getAncestorSession());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public Directory getParent() {
		return (Directory) super.getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DirectoryNavigator#isMatchLocated()
	 */
	@Override
	public boolean isMatchLocated() {
		try {
			return getDelegate().isMatchLocated();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DirectoryNavigator#isNameLocated()
	 */
	@Override
	public boolean isNameLocated() {
		try {
			return getDelegate().isNameLocated();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public Session getAncestorSession() {
		return this.getParent().getAncestorSession();
	}

}
