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

import java.util.Iterator;
import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.ACLEntry;
import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.iterators.AclIterator;
import org.openntf.domino.utils.DominoUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class ACL.
 */
public class ACL extends Base<org.openntf.domino.ACL, lotus.domino.ACL, Database> implements org.openntf.domino.ACL {

	/**
	 * Instantiates a new acl.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent database
	 * @param wf
	 *            the wrapper factory
	 * @param cppId
	 *            the cpp-id
	 */
	public ACL(final lotus.domino.ACL delegate, final Database parent, final WrapperFactory wf, final long cppId) {
		super(delegate, parent, wf, cppId, NOTES_ACL);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.impl.Base#findParent(lotus.domino.Base)
	 */
	@Override
	protected Database findParent(final lotus.domino.ACL delegate) throws NotesException {
		return fromLotus(delegate.getParent(), Database.SCHEMA, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACL#addRole(java.lang.String)
	 */
	@Override
	public void addRole(final String name) {
		try {
			getDelegate().addRole(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACL#createACLEntry(java.lang.String, int)
	 */
	@Override
	public ACLEntry createACLEntry(final String name, final int level) {
		try {
			return fromLotus(getDelegate().createACLEntry(name, level), ACLEntry.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ACL#createACLEntry(java.lang.String, org.openntf.domino.ACL.Level)
	 */
	@Override
	public ACLEntry createACLEntry(final String name, final Level level) {
		return this.createACLEntry(name, level.getValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACL#deleteRole(java.lang.String)
	 */
	@Override
	public void deleteRole(final String name) {
		try {
			getDelegate().deleteRole(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACL#getAdministrationServer()
	 */
	@Override
	public String getAdministrationServer() {
		try {
			return getDelegate().getAdministrationServer();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public Database getAncestorDatabase() {
		return this.getParent();
	}

	@Override
	public Session getAncestorSession() {
		return this.getAncestorDatabase().getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACL#getEntry(java.lang.String)
	 */
	@Override
	public ACLEntry getEntry(final String ename) {
		try {
			return fromLotus(getDelegate().getEntry(ename), ACLEntry.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACL#getFirstEntry()
	 */
	@Override
	public ACLEntry getFirstEntry() {
		try {
			return fromLotus(getDelegate().getFirstEntry(), ACLEntry.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACL#getInternetLevel()
	 */
	@Override
	public int getInternetLevel() {
		try {
			return getDelegate().getInternetLevel();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return ACL.LEVEL_NOACCESS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACL#getNextEntry()
	 */
	@Override
	public ACLEntry getNextEntry() {
		try {
			return fromLotus(getDelegate().getNextEntry(), ACLEntry.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACL#getNextEntry(lotus.domino.ACLEntry)
	 */
	@Override
	public ACLEntry getNextEntry(final lotus.domino.ACLEntry entry) {
		try {
			return fromLotus(getDelegate().getNextEntry(toLotus(entry)), ACLEntry.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public Database getParent() {
		return getAncestor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACL#getRoles()
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Vector<String> getRoles() {
		try {
			return new Vector(getDelegate().getRoles());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACL#isAdminNames()
	 */
	@Override
	public boolean isAdminNames() {
		try {
			return getDelegate().isAdminNames();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACL#isAdminReaderAuthor()
	 */
	@Override
	public boolean isAdminReaderAuthor() {
		try {
			return getDelegate().isAdminReaderAuthor();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACL#isExtendedAccess()
	 */
	@Override
	public boolean isExtendedAccess() {
		try {
			return getDelegate().isExtendedAccess();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACL#isUniformAccess()
	 */
	@Override
	public boolean isUniformAccess() {
		try {
			return getDelegate().isUniformAccess();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACL#removeACLEntry(java.lang.String)
	 */
	@Override
	public void removeACLEntry(final String name) {
		try {
			getDelegate().removeACLEntry(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACL#renameRole(java.lang.String, java.lang.String)
	 */
	@Override
	public void renameRole(final String oldName, final String newName) {
		try {
			getDelegate().renameRole(oldName, newName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACL#save()
	 */
	@Override
	public void save() {
		try {
			getDelegate().save();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACL#setAdminNames(boolean)
	 */
	@Override
	public void setAdminNames(final boolean flag) {
		try {
			getDelegate().setAdminNames(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACL#setAdminReaderAuthor(boolean)
	 */
	@Override
	public void setAdminReaderAuthor(final boolean flag) {
		try {
			getDelegate().setAdminReaderAuthor(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACL#setAdministrationServer(java.lang.String)
	 */
	@Override
	public void setAdministrationServer(final String serverName) {
		try {
			getDelegate().setAdministrationServer(serverName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACL#setExtendedAccess(boolean)
	 */
	@Override
	public void setExtendedAccess(final boolean flag) {
		try {
			getDelegate().setExtendedAccess(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACL#setInternetLevel(int)
	 */
	@Override
	public void setInternetLevel(final int level) {
		try {
			getDelegate().setInternetLevel(level);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ACL#setInternetLevel(org.openntf.domino.ACL.Level)
	 */
	@Override
	public void setInternetLevel(final Level level) {
		this.setInternetLevel(level.getValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACL#setUniformAccess(boolean)
	 */
	@Override
	public void setUniformAccess(final boolean flag) {
		try {
			getDelegate().setUniformAccess(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<ACLEntry> iterator() {
		return new AclIterator(this);
	}

	@Override
	protected void resurrect() {
		try {
			lotus.domino.Database db = toLotus(getParent());
			lotus.domino.ACL d = db.getACL();
			setDelegate(d, 0);
			// Recaching is done in setDelegate now
			//getFactory().recacheLotusObject(d, this, parent_);
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
	}

}
