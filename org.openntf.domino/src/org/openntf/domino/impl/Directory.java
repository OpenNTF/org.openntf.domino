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

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class Directory.
 */
public class Directory extends Base<org.openntf.domino.Directory, lotus.domino.Directory> implements org.openntf.domino.Directory {

	/**
	 * Instantiates a new directory.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	public Directory(lotus.domino.Directory delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Directory#createNavigator()
	 */
	@Override
	public DirectoryNavigator createNavigator() {
		try {
			return Factory.fromLotus(getDelegate().createNavigator(), DirectoryNavigator.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Directory#freeLookupBuffer()
	 */
	@Override
	public void freeLookupBuffer() {
		try {
			getDelegate().freeLookupBuffer();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Directory#getAvailableItems()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Vector<String> getAvailableItems() {
		try {
			return getDelegate().getAvailableItems();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Directory#getAvailableNames()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Vector<String> getAvailableNames() {
		try {
			return getDelegate().getAvailableNames();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Directory#getAvailableView()
	 */
	@Override
	public String getAvailableView() {
		try {
			return getDelegate().getAvailableView();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Directory#getMailInfo(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Vector<String> getMailInfo(String userName) {
		try {
			return getDelegate().getMailInfo(userName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Directory#getMailInfo(java.lang.String, boolean, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Vector<String> getMailInfo(String userName, boolean getVersion, boolean errorOnMultipleMatches) {
		try {
			return getDelegate().getMailInfo(userName, getVersion, errorOnMultipleMatches);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public Session getParent() {
		return (Session) super.getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Directory#getServer()
	 */
	@Override
	public String getServer() {
		try {
			return getDelegate().getServer();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Directory#isGroupAuthorizationOnly()
	 */
	@Override
	public boolean isGroupAuthorizationOnly() {
		try {
			return getDelegate().isGroupAuthorizationOnly();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Directory#isLimitMatches()
	 */
	@Override
	public boolean isLimitMatches() {
		try {
			return getDelegate().isLimitMatches();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Directory#isPartialMatches()
	 */
	@Override
	public boolean isPartialMatches() {
		try {
			return getDelegate().isPartialMatches();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Directory#isSearchAllDirectories()
	 */
	@Override
	public boolean isSearchAllDirectories() {
		try {
			return getDelegate().isSearchAllDirectories();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Directory#isTrustedOnly()
	 */
	@Override
	public boolean isTrustedOnly() {
		try {
			return getDelegate().isTrustedOnly();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Directory#isUseContextServer()
	 */
	@Override
	public boolean isUseContextServer() {
		try {
			return getDelegate().isUseContextServer();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Directory#lookupAllNames(java.lang.String, java.lang.String)
	 */
	@Override
	public DirectoryNavigator lookupAllNames(String view, String item) {
		try {
			return Factory.fromLotus(getDelegate().lookupAllNames(view, item), DirectoryNavigator.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Directory#lookupAllNames(java.lang.String, java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public DirectoryNavigator lookupAllNames(String view, Vector items) {
		try {
			return Factory.fromLotus(getDelegate().lookupAllNames(view, items), DirectoryNavigator.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Directory#lookupNames(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public DirectoryNavigator lookupNames(String view, String name, String item) {
		try {
			return Factory.fromLotus(getDelegate().lookupNames(view, name, item), DirectoryNavigator.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Directory#lookupNames(java.lang.String, java.util.Vector, java.util.Vector, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public DirectoryNavigator lookupNames(String view, Vector names, Vector items, boolean partialMatches) {
		try {
			return Factory.fromLotus(getDelegate().lookupNames(view, names, items, partialMatches), DirectoryNavigator.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Directory#setGroupAuthorizationOnly(boolean)
	 */
	@Override
	public void setGroupAuthorizationOnly(boolean flag) {
		try {
			getDelegate().setGroupAuthorizationOnly(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Directory#setLimitMatches(boolean)
	 */
	@Override
	public void setLimitMatches(boolean flag) {
		try {
			getDelegate().setLimitMatches(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Directory#setSearchAllDirectories(boolean)
	 */
	@Override
	public void setSearchAllDirectories(boolean flag) {
		try {
			getDelegate().setSearchAllDirectories(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Directory#setTrustedOnly(boolean)
	 */
	@Override
	public void setTrustedOnly(boolean flag) {
		try {
			getDelegate().setTrustedOnly(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Directory#setUseContextServer(boolean)
	 */
	@Override
	public void setUseContextServer(boolean flag) {
		try {
			getDelegate().setUseContextServer(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}
}
