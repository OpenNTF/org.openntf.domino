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
package org.openntf.domino;

import java.util.Vector;

// TODO: Auto-generated Javadoc
/**
 * The Interface ACL.
 */
public interface ACL extends Base<lotus.domino.ACL>, lotus.domino.ACL, Iterable<org.openntf.domino.ACLEntry> {

	/* (non-Javadoc)
	 * @see lotus.domino.ACL#addRole(java.lang.String)
	 */
	@Override
	public void addRole(String name);

	/* (non-Javadoc)
	 * @see lotus.domino.ACL#createACLEntry(java.lang.String, int)
	 */
	@Override
	public ACLEntry createACLEntry(String name, int level);

	/* (non-Javadoc)
	 * @see lotus.domino.ACL#deleteRole(java.lang.String)
	 */
	@Override
	public void deleteRole(String name);

	/* (non-Javadoc)
	 * @see lotus.domino.ACL#getAdministrationServer()
	 */
	@Override
	public String getAdministrationServer();

	/* (non-Javadoc)
	 * @see lotus.domino.ACL#getEntry(java.lang.String)
	 */
	@Override
	public ACLEntry getEntry(String name);

	/* (non-Javadoc)
	 * @see lotus.domino.ACL#getFirstEntry()
	 */
	@Override
	public ACLEntry getFirstEntry();

	/* (non-Javadoc)
	 * @see lotus.domino.ACL#getInternetLevel()
	 */
	@Override
	public int getInternetLevel();

	/* (non-Javadoc)
	 * @see lotus.domino.ACL#getNextEntry()
	 */
	@Override
	public ACLEntry getNextEntry();

	/* (non-Javadoc)
	 * @see lotus.domino.ACL#getNextEntry(lotus.domino.ACLEntry)
	 */
	@Override
	public ACLEntry getNextEntry(lotus.domino.ACLEntry entry);

	/* (non-Javadoc)
	 * @see lotus.domino.ACL#getParent()
	 */
	@Override
	public Database getParent();

	/* (non-Javadoc)
	 * @see lotus.domino.ACL#getRoles()
	 */
	@Override
	public Vector<String> getRoles();

	/* (non-Javadoc)
	 * @see lotus.domino.ACL#isAdminNames()
	 */
	@Override
	public boolean isAdminNames();

	/* (non-Javadoc)
	 * @see lotus.domino.ACL#isAdminReaderAuthor()
	 */
	@Override
	public boolean isAdminReaderAuthor();

	/* (non-Javadoc)
	 * @see lotus.domino.ACL#isExtendedAccess()
	 */
	@Override
	public boolean isExtendedAccess();

	/* (non-Javadoc)
	 * @see lotus.domino.ACL#isUniformAccess()
	 */
	@Override
	public boolean isUniformAccess();

	/* (non-Javadoc)
	 * @see lotus.domino.ACL#removeACLEntry(java.lang.String)
	 */
	@Override
	public void removeACLEntry(String name);

	/* (non-Javadoc)
	 * @see lotus.domino.ACL#renameRole(java.lang.String, java.lang.String)
	 */
	@Override
	public void renameRole(String oldName, String newName);

	/* (non-Javadoc)
	 * @see lotus.domino.ACL#save()
	 */
	@Override
	public void save();

	/* (non-Javadoc)
	 * @see lotus.domino.ACL#setAdministrationServer(java.lang.String)
	 */
	@Override
	public void setAdministrationServer(String server);

	/* (non-Javadoc)
	 * @see lotus.domino.ACL#setAdminNames(boolean)
	 */
	@Override
	public void setAdminNames(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.ACL#setAdminReaderAuthor(boolean)
	 */
	@Override
	public void setAdminReaderAuthor(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.ACL#setExtendedAccess(boolean)
	 */
	@Override
	public void setExtendedAccess(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.ACL#setInternetLevel(int)
	 */
	@Override
	public void setInternetLevel(int level);

	/* (non-Javadoc)
	 * @see lotus.domino.ACL#setUniformAccess(boolean)
	 */
	@Override
	public void setUniformAccess(boolean flag);

}
