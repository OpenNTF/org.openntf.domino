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

import org.openntf.domino.DirectoryNavigator;
import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class Directory.
 */
public class Directory extends Base<org.openntf.domino.Directory, lotus.domino.Directory, Session> implements org.openntf.domino.Directory {

	/**
	 * Instantiates a new directory.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	@Deprecated
	public Directory(final lotus.domino.Directory delegate, final org.openntf.domino.Base<?> parent) {
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
	public Directory(final lotus.domino.Directory delegate, final Session parent, final WrapperFactory wf, final long cppId) {
		super(delegate, parent, wf, cppId, NOTES_DIRECTORY);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.impl.Base#findParent(lotus.domino.Base)
	 */
	@Override
	protected Session findParent(final lotus.domino.Directory delegate) {
		return fromLotus(Base.getSession(delegate), Session.SCHEMA, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Directory#createNavigator()
	 */
	@Override
	public DirectoryNavigator createNavigator() {
		try {
			return fromLotus(getDelegate().createNavigator(), DirectoryNavigator.SCHEMA, this);
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
	public Vector<String> getMailInfo(final String userName) {
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
	public Vector<String> getMailInfo(final String userName, final boolean getVersion, final boolean errorOnMultipleMatches) {
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
		return getAncestor();
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
	public DirectoryNavigator lookupAllNames(final String view, final String item) {
		try {
			return fromLotus(getDelegate().lookupAllNames(view, item), DirectoryNavigator.SCHEMA, this);
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
	@SuppressWarnings("rawtypes")
	@Override
	public DirectoryNavigator lookupAllNames(final String view, final Vector items) {
		try {
			return fromLotus(getDelegate().lookupAllNames(view, items), DirectoryNavigator.SCHEMA, this);
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
	public DirectoryNavigator lookupNames(final String view, final String name, final String item) {
		try {
			return fromLotus(getDelegate().lookupNames(view, name, item), DirectoryNavigator.SCHEMA, this);
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
	@SuppressWarnings("rawtypes")
	@Override
	public DirectoryNavigator lookupNames(final String view, final Vector names, final Vector items, final boolean partialMatches) {
		try {
			return fromLotus(getDelegate().lookupNames(view, names, items, partialMatches), DirectoryNavigator.SCHEMA, this);
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
	public void setGroupAuthorizationOnly(final boolean flag) {
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
	public void setLimitMatches(final boolean flag) {
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
	public void setSearchAllDirectories(final boolean flag) {
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
	public void setTrustedOnly(final boolean flag) {
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
	public void setUseContextServer(final boolean flag) {
		try {
			getDelegate().setUseContextServer(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public org.openntf.domino.Session getAncestorSession() {
		return this.getParent();
	}
}
