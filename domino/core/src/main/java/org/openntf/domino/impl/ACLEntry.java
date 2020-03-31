/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.ACL;
import org.openntf.domino.Database;
import org.openntf.domino.Name;
import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.utils.DominoUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class ACLEntry.
 */
public class ACLEntry extends BaseThreadSafe<org.openntf.domino.ACLEntry, lotus.domino.ACLEntry, org.openntf.domino.ACL> implements
		org.openntf.domino.ACLEntry {

	/**
	 * Instantiates a new ACL entry.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	protected ACLEntry(final lotus.domino.ACLEntry delegate, final org.openntf.domino.ACL parent) {
		super(delegate, parent, NOTES_ACLENTRY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#disableRole(java.lang.String)
	 */
	@Override
	public void disableRole(final String role) {
		try {
			getDelegate().disableRole(role);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#enableRole(java.lang.String)
	 */
	@Override
	public void enableRole(final String role) {
		try {
			getDelegate().enableRole(role);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#getLevel()
	 */
	@Override
	public int getLevel() {
		try {
			return getDelegate().getLevel();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return ACL.LEVEL_NOACCESS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#getName()
	 */
	@Override
	public String getName() {
		try {
			return getDelegate().getName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#getNameObject()
	 */
	@Override
	public Name getNameObject() {
		try {
			return fromLotus(getDelegate().getNameObject(), Name.SCHEMA, getAncestorSession());
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
	public final ACL getParent() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#getRoles()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Vector<String> getRoles() {
		try {
			return getDelegate().getRoles();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#getUserType()
	 */
	@Override
	public int getUserType() {
		try {
			return getDelegate().getUserType();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return ACLEntry.TYPE_UNSPECIFIED;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#isAdminReaderAuthor()
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
	 * @see org.openntf.domino.ACLEntry#isAdminServer()
	 */
	@Override
	public boolean isAdminServer() {
		try {
			return getDelegate().isAdminServer();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#isCanCreateDocuments()
	 */
	@Override
	public boolean isCanCreateDocuments() {
		try {
			return getDelegate().isCanCreateDocuments();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#isCanCreateLSOrJavaAgent()
	 */
	@Override
	public boolean isCanCreateLSOrJavaAgent() {
		try {
			return getDelegate().isCanCreateLSOrJavaAgent();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#isCanCreatePersonalAgent()
	 */
	@Override
	public boolean isCanCreatePersonalAgent() {
		try {
			return getDelegate().isCanCreatePersonalAgent();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#isCanCreatePersonalFolder()
	 */
	@Override
	public boolean isCanCreatePersonalFolder() {
		try {
			return getDelegate().isCanCreatePersonalFolder();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#isCanCreateSharedFolder()
	 */
	@Override
	public boolean isCanCreateSharedFolder() {
		try {
			return getDelegate().isCanCreateSharedFolder();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#isCanDeleteDocuments()
	 */
	@Override
	public boolean isCanDeleteDocuments() {
		try {
			return getDelegate().isCanDeleteDocuments();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#isCanReplicateOrCopyDocuments()
	 */
	@Override
	public boolean isCanReplicateOrCopyDocuments() {
		try {
			return getDelegate().isCanReplicateOrCopyDocuments();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#isGroup()
	 */
	@Override
	public boolean isGroup() {
		try {
			return getDelegate().isGroup();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#isPerson()
	 */
	@Override
	public boolean isPerson() {
		try {
			return getDelegate().isPerson();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#isPublicReader()
	 */
	@Override
	public boolean isPublicReader() {
		try {
			return getDelegate().isPublicReader();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#isPublicWriter()
	 */
	@Override
	public boolean isPublicWriter() {
		try {
			return getDelegate().isPublicWriter();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#isRoleEnabled(java.lang.String)
	 */
	@Override
	public boolean isRoleEnabled(final String role) {
		try {
			return getDelegate().isRoleEnabled(role);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#isServer()
	 */
	@Override
	public boolean isServer() {
		try {
			return getDelegate().isServer();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#remove()
	 */
	@Override
	public void remove() {
		try {
			getDelegate().remove();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#setAdminReaderAuthor(boolean)
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
	 * @see org.openntf.domino.ACLEntry#setAdminServer(boolean)
	 */
	@Override
	public void setAdminServer(final boolean flag) {
		try {
			getDelegate().setAdminServer(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#setCanCreateDocuments(boolean)
	 */
	@Override
	public void setCanCreateDocuments(final boolean flag) {
		try {
			getDelegate().setCanCreateDocuments(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#setCanCreateLSOrJavaAgent(boolean)
	 */
	@Override
	public void setCanCreateLSOrJavaAgent(final boolean flag) {
		try {
			getDelegate().setCanCreateLSOrJavaAgent(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#setCanCreatePersonalAgent(boolean)
	 */
	@Override
	public void setCanCreatePersonalAgent(final boolean flag) {
		try {
			getDelegate().setCanCreatePersonalAgent(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#setCanCreatePersonalFolder(boolean)
	 */
	@Override
	public void setCanCreatePersonalFolder(final boolean flag) {
		try {
			getDelegate().setCanCreatePersonalFolder(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#setCanCreateSharedFolder(boolean)
	 */
	@Override
	public void setCanCreateSharedFolder(final boolean flag) {
		try {
			getDelegate().setCanCreateSharedFolder(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#setCanDeleteDocuments(boolean)
	 */
	@Override
	public void setCanDeleteDocuments(final boolean flag) {
		try {
			getDelegate().setCanDeleteDocuments(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#setCanReplicateOrCopyDocuments(boolean)
	 */
	@Override
	public void setCanReplicateOrCopyDocuments(final boolean flag) {
		try {
			getDelegate().setCanReplicateOrCopyDocuments(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#setGroup(boolean)
	 */
	@Override
	public void setGroup(final boolean flag) {
		try {
			getDelegate().setGroup(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#setLevel(int)
	 */
	@Override
	public void setLevel(final int level) {
		try {
			getDelegate().setLevel(level);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ACLEntry#setLevel(org.openntf.domino.ACL.Level)
	 */
	@Override
	public void setLevel(final ACL.Level level) {
		this.setLevel(level.getValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#setName(java.lang.String)
	 */
	@Override
	public void setName(final String name) {
		try {
			getDelegate().setName(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#setName(lotus.domino.Name)
	 */
	@Override
	public void setName(final lotus.domino.Name n) {
		@SuppressWarnings("rawtypes")
		List recycleThis = new ArrayList();
		try {
			getDelegate().setName(toLotus(n, recycleThis));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		} finally {
			s_recycle(recycleThis);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#setPerson(boolean)
	 */
	@Override
	public void setPerson(final boolean flag) {
		try {
			getDelegate().setPerson(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#setPublicReader(boolean)
	 */
	@Override
	public void setPublicReader(final boolean flag) {
		try {
			getDelegate().setPublicReader(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#setPublicWriter(boolean)
	 */
	@Override
	public void setPublicWriter(final boolean flag) {
		try {
			getDelegate().setPublicWriter(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#setServer(boolean)
	 */
	@Override
	public void setServer(final boolean flag) {
		try {
			getDelegate().setServer(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ACLEntry#setUserType(int)
	 */
	@Override
	public void setUserType(final int tp) {
		try {
			getDelegate().setUserType(tp);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public final Database getAncestorDatabase() {
		return parent.getAncestorDatabase();
	}

	@Override
	public final Session getAncestorSession() {
		return parent.getAncestorSession();
	}

	@Override
	protected final WrapperFactory getFactory() {
		return parent.getAncestorSession().getFactory();
	}

}
