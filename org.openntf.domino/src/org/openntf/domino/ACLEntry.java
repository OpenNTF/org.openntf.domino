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
package org.openntf.domino;

import java.util.Vector;

import org.openntf.domino.types.DatabaseDescendant;
import org.openntf.domino.types.FactorySchema;

/**
 * The Interface ACLEntry.
 */
public interface ACLEntry extends Base<lotus.domino.ACLEntry>, lotus.domino.ACLEntry, org.openntf.domino.ext.ACLEntry, DatabaseDescendant {

	public static class Schema extends FactorySchema<ACLEntry, lotus.domino.ACLEntry, ACL> {
		@Override
		public Class<ACLEntry> typeClass() {
			return ACLEntry.class;
		}

		@Override
		public Class<lotus.domino.ACLEntry> delegateClass() {
			return lotus.domino.ACLEntry.class;
		}

		@Override
		public Class<ACL> parentClass() {
			return ACL.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#disableRole(java.lang.String)
	 */
	@Override
	public void disableRole(final String role);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#enableRole(java.lang.String)
	 */
	@Override
	public void enableRole(final String role);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#getLevel()
	 */
	@Override
	public int getLevel();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#getName()
	 */
	@Override
	public String getName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#getNameObject()
	 */
	@Override
	public Name getNameObject();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#getParent()
	 */
	@Override
	public ACL getParent();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#getRoles()
	 */
	@Override
	public Vector<String> getRoles();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#getUserType()
	 */
	@Override
	public int getUserType();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#isAdminReaderAuthor()
	 */
	@Override
	public boolean isAdminReaderAuthor();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#isAdminServer()
	 */
	@Override
	public boolean isAdminServer();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#isCanCreateDocuments()
	 */
	@Override
	public boolean isCanCreateDocuments();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#isCanCreateLSOrJavaAgent()
	 */
	@Override
	public boolean isCanCreateLSOrJavaAgent();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#isCanCreatePersonalAgent()
	 */
	@Override
	public boolean isCanCreatePersonalAgent();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#isCanCreatePersonalFolder()
	 */
	@Override
	public boolean isCanCreatePersonalFolder();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#isCanCreateSharedFolder()
	 */
	@Override
	public boolean isCanCreateSharedFolder();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#isCanDeleteDocuments()
	 */
	@Override
	public boolean isCanDeleteDocuments();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#isCanReplicateOrCopyDocuments()
	 */
	@Override
	public boolean isCanReplicateOrCopyDocuments();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#isGroup()
	 */
	@Override
	public boolean isGroup();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#isPerson()
	 */
	@Override
	public boolean isPerson();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#isPublicReader()
	 */
	@Override
	public boolean isPublicReader();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#isPublicWriter()
	 */
	@Override
	public boolean isPublicWriter();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#isRoleEnabled(java.lang.String)
	 */
	@Override
	public boolean isRoleEnabled(final String role);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#isServer()
	 */
	@Override
	public boolean isServer();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#remove()
	 */
	@Override
	public void remove();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#setAdminReaderAuthor(boolean)
	 */
	@Override
	public void setAdminReaderAuthor(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#setAdminServer(boolean)
	 */
	@Override
	public void setAdminServer(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#setCanCreateDocuments(boolean)
	 */
	@Override
	public void setCanCreateDocuments(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#setCanCreateLSOrJavaAgent(boolean)
	 */
	@Override
	public void setCanCreateLSOrJavaAgent(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#setCanCreatePersonalAgent(boolean)
	 */
	@Override
	public void setCanCreatePersonalAgent(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#setCanCreatePersonalFolder(boolean)
	 */
	@Override
	public void setCanCreatePersonalFolder(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#setCanCreateSharedFolder(boolean)
	 */
	@Override
	public void setCanCreateSharedFolder(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#setCanDeleteDocuments(boolean)
	 */
	@Override
	public void setCanDeleteDocuments(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#setCanReplicateOrCopyDocuments(boolean)
	 */
	@Override
	public void setCanReplicateOrCopyDocuments(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#setGroup(boolean)
	 */
	@Override
	public void setGroup(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#setLevel(int)
	 */
	@Override
	public void setLevel(final int level);

	/**
	 * Sets the level.
	 * 
	 * @param level
	 *            the new level
	 */
	@Override
	public void setLevel(final ACL.Level level);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#setName(lotus.domino.Name)
	 */
	@Override
	public void setName(final lotus.domino.Name name);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#setName(java.lang.String)
	 */
	@Override
	public void setName(final String name);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#setPerson(boolean)
	 */
	@Override
	public void setPerson(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#setPublicReader(boolean)
	 */
	@Override
	public void setPublicReader(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#setPublicWriter(boolean)
	 */
	@Override
	public void setPublicWriter(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#setServer(boolean)
	 */
	@Override
	public void setServer(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ACLEntry#setUserType(int)
	 */
	@Override
	public void setUserType(final int type);

}
