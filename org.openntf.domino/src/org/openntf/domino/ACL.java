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

/**
 * The Interface that represents the access control list (ACL) of an IBM Domino database.
 */
public interface ACL extends Base<lotus.domino.ACL>, lotus.domino.ACL, Iterable<org.openntf.domino.ACLEntry> {

	/**
	 * Adds a role with the specified name to an ACL.
	 * 
	 * @param name
	 *            The name of the role to add
	 */
	@Override
	public void addRole(String name);

	/**
	 * Creates an entry in the ACL with the name and level that you specify.
	 * 
	 * @param name
	 *            The name of the person, group, or server for whom you want to create an entry in the ACL. You must supply the complete
	 *            name, but hierarchical names can be in abbreviated format. Case is not significant.
	 * @param level
	 *            The level that you want to assign to this person, group, or server in the ACL. May be any of the following :<br>
	 * <br>
	 *            <ul>
	 *            <li>ACL.LEVEL_NOACCESS (0)</li>
	 *            <li>ACL.LEVEL_DEPOSITOR (1)</li>
	 *            <li>ACL.LEVEL_READER (2)</li>
	 *            <li>ACL.LEVEL_AUTHOR (3)</li>
	 *            <li>ACL.LEVEL_EDITOR (4)</li>
	 *            <li>ACL.LEVEL_DESIGNER (5)</li>
	 *            <li>ACL.LEVEL_MANAGER (6)</li>
	 *            </ul>
	 * 
	 * @return The newly-created {@link org.openntf.domino.ACLEntry}.
	 * 
	 */
	@Override
	public ACLEntry createACLEntry(String name, int level);

	/**
	 * Deletes a role with the specified name from an ACL.
	 * 
	 * @param name
	 *            The name of the role to delete
	 */
	@Override
	public void deleteRole(String name);

	/**
	 * Returns the name of the administration server for an IBM Domino database.
	 * 
	 * <p>
	 * The administration server is also the master lock server.<br>
	 * This property is an <code>empty string</code> if the database does not have an administration server.
	 * </p>
	 */
	@Override
	public String getAdministrationServer();

	/**
	 * Given a name, finds its entry in an ACL.
	 * 
	 * @param name
	 *            The name whose ACL Entry you want to find. You must supply the complete name, but hierarchical names can be in abbreviated
	 *            format. Case is not significant.
	 * @return The {@link org.openntf.domino.ACLEntry} that matches the name. If name is not in the ACL, returns <code>null</code>.
	 */
	@Override
	public ACLEntry getEntry(String name);

	/**
	 * Returns the first entry in an ACL.
	 * 
	 * <p>
	 * The first entry is typically the -Default- entry.
	 * </p>
	 * 
	 * @return The first {@link org.openntf.domino.ACLEntry} in the ACL.
	 */
	@Override
	public ACLEntry getFirstEntry();

	/**
	 * The maximum Internet access level for this database.
	 * 
	 * @return The current maximum internet access level of the database. May be any of the following :<br>
	 * <br>
	 *         <ul>
	 *         <li>ACL.LEVEL_NOACCESS (0)</li>
	 *         <li>ACL.LEVEL_DEPOSITOR (1)</li>
	 *         <li>ACL.LEVEL_READER (2)</li>
	 *         <li>ACL.LEVEL_AUTHOR (3)</li>
	 *         <li>ACL.LEVEL_EDITOR (4)</li>
	 *         <li>ACL.LEVEL_DESIGNER (5)</li>
	 *         <li>ACL.LEVEL_MANAGER (6)</li>
	 *         </ul>
	 */
	@Override
	public int getInternetLevel();

	/**
	 * Returns the ACL Entry following the last ACL Entry retrieved.
	 * 
	 * <p>
	 * The no-parameter method improves performance for remote operations because ACL entries are cached locally.
	 * </p>
	 * 
	 * <p>
	 * The order of the ACL entries is unspecified. The order is not alphabetical and does not correspond to UI displays.
	 * </p>
	 * 
	 * @return The next {@link org.openntf.domino.ACLEntry} in the ACL. Returns <code>null</code> if there are no more entries.
	 */
	@Override
	public ACLEntry getNextEntry();

	/**
	 * Returns the ACL entry following the entry specified as the parameter.
	 * 
	 * <p>
	 * The order of the ACL entries is unspecified. The order is not alphabetical and does not correspond to UI displays.
	 * </p>
	 * 
	 * @param entry
	 *            Any entry in the ACL. Cannot be <code>null</code>.
	 * 
	 * @return The the {@link org.openntf.domino.ACLEntry} following the entry specified as the parameter. Returns <code>null</code> if
	 *         there are no more entries.
	 */
	@Override
	public ACLEntry getNextEntry(lotus.domino.ACLEntry entry);

	/**
	 * Returns the IBM Domino database that owns the ACL.
	 * 
	 * @return The {@link org.openntf.domino.Database} that this ACL belongs to.
	 */
	@Override
	public Database getParent();

	/**
	 * Returns all the roles defined in an access control list.
	 * 
	 * <p>
	 * Each element in the vector is the name of a role. Each role name is surrounded by square brackets, for example, "[Supervisor]."
	 * </p>
	 * 
	 * <p>
	 * Notes® Release 2 "privileges" are surrounded by parentheses. Properties and methods affecting roles, such as disableRole and
	 * enableRole, do not work on privileges.
	 * </p>
	 * 
	 * @return A Vector of all the roles in the ACL. Elements are of type {@link java.lang.String}
	 */
	@Override
	public Vector<String> getRoles();

	/**
	 * Indicates whether the administration server for the database can modify all Names fields in a database.
	 * 
	 * <p>
	 * The database must have an administration server. See {@link org.openntf.domino.ACL#getAdministrationServer()} in ACL and
	 * {@link org.openntf.domino.ACLEntry#isAdminServer()} in {@link org.openntf.domino.ACLEntry} to check for the administration server.
	 * </p>
	 * 
	 * @return Returns <code>true</code> if the administration server can modify all names fields, <code>false</code> if it cannot.
	 */
	@Override
	public boolean isAdminNames();

	/**
	 * Indicates whether the administration server for the database can modify Readers and Authors fields in a database.
	 * 
	 * <p>
	 * The database must have an administration server. See {@link org.openntf.domino.ACL#getAdministrationServer()} in ACL and
	 * {@link org.openntf.domino.ACLEntry#isAdminServer()} in {@link org.openntf.domino.ACLEntry} to check for the administration server.
	 * </p>
	 * 
	 * @return Returns <code>true</code> if the administration server can modify Readers and Authors fields, <code>false</code> if it
	 *         cannot.
	 */
	@Override
	public boolean isAdminReaderAuthor();

	/**
	 * Indicates whether extended access is enabled.
	 * 
	 * <p>
	 * In the Notes UI, this property is in the advanced section of ACL properties: "Enable Extended Access." and only applies to Domino
	 * Directory, Extended Directory Catalog, and Administration Requests databases.
	 * </p>
	 * 
	 * @return Returns <code>true</code> if extended access is enabled, <code>false</code> if it is not enabled.
	 */
	@Override
	public boolean isExtendedAccess();

	/**
	 * Indicates whether a consistent access control list is enforced across all replicas of a database.
	 * 
	 * @return Returns <code>true</code> if uniform access is enabled, <code>false</code> if it is not enabled.
	 */
	@Override
	public boolean isUniformAccess();

	/**
	 * Removes an entry from the ACL.
	 * 
	 * <p>
	 * After calling this method, you must call {@link org.openntf.domino.ACL#save()} for the changes to take effect. If you do not call
	 * save, your changes to the ACL are lost.
	 * </p>
	 * 
	 * @param name
	 *            The name of the person, group, or server whose entry you want to remove. You must supply the complete name, but
	 *            hierarchical names can be in abbreviated format. Case is not significant.
	 * 
	 */
	@Override
	public void removeACLEntry(String name);

	/**
	 * Changes the name of a role.
	 * 
	 * <p>
	 * After calling this method, you must call {@link org.openntf.domino.ACL#save()} for the changes to take effect. If you do not call
	 * save, your changes to the ACL are lost.
	 * </p>
	 * 
	 * <p>
	 * When you rename a role, any entries in the ACL that had the old role get the new role.
	 * </p>
	 * 
	 * @param oldName
	 *            The current name of the role. Do not put square brackets around the name.
	 * @param newName
	 *            The current name of the role. Do not put square brackets around the name.
	 * 
	 */
	@Override
	public void renameRole(String oldName, String newName);

	/**
	 * Saves changes that you've made to the ACL.
	 * 
	 * <p>
	 * If you don't call save before closing a database, the changes you've made to its ACL are lost.
	 * </p>
	 */
	@Override
	public void save();

	/**
	 * Sets the name of the administration server for a database.
	 * 
	 * <p>
	 * The administration server is also the master lock server.<br>
	 * This property is an <code>empty string</code> if the database does not have an administration server.
	 * </p>
	 * 
	 * @param server
	 *            the name of the server you want to set as the Administration Server.
	 */
	@Override
	public void setAdministrationServer(String server);

	/**
	 * Sets whether the administration server for the database can modify all Names fields in a database.
	 * 
	 * <p>
	 * The database must have an administration server. See {@link org.openntf.domino.ACL#getAdministrationServer()} in ACL and
	 * {@link org.openntf.domino.ACLEntry#isAdminServer()} in {@link org.openntf.domino.ACLEntry} to set the administration server.
	 * </p>
	 * 
	 * <p>
	 * After setting this property, you must call {@link org.openntf.domino.ACL#save()} for the changes to take effect. If you do not call
	 * save, your changes to the ACL are lost.
	 * </p>
	 * 
	 * @param flag
	 *            The boolean value to set
	 */
	@Override
	public void setAdminNames(boolean flag);

	/**
	 * Sets whether the administration server for the database can modify Readers and Authors fields in a database.
	 * 
	 * <p>
	 * The database must have an administration server. See {@link org.openntf.domino.ACL#getAdministrationServer()} in ACL and
	 * {@link org.openntf.domino.ACLEntry#isAdminServer()} in {@link org.openntf.domino.ACLEntry} to set the administration server.
	 * </p>
	 * 
	 * <p>
	 * After setting this property, you must call {@link org.openntf.domino.ACL#save()} for the changes to take effect. If you do not call
	 * save, your changes to the ACL are lost.
	 * </p>
	 * 
	 * @param flag
	 *            The boolean value to set
	 */
	@Override
	public void setAdminReaderAuthor(boolean flag);

	/**
	 * Sets whether extended access is enabled.
	 * 
	 * <p>
	 * In the Notes UI, this property is in the advanced section of ACL properties: "Enable Extended Access." and only applies to Domino
	 * Directory, Extended Directory Catalog, and Administration Requests databases.
	 * </p>
	 * 
	 * <p>
	 * Setting this property true automatically sets {@link #setUniformAccess(boolean)} true. Setting this property false does not
	 * automatically set {@link #setUniformAccess(boolean)} false.
	 * </p>
	 * 
	 * <p>
	 * After setting this property, you must call {@link org.openntf.domino.ACL#save()} for the changes to take effect. If you do not call
	 * save, your changes to the ACL are lost.
	 * </p>
	 * 
	 * @param flag
	 *            The boolean value to set
	 */
	@Override
	public void setExtendedAccess(boolean flag);

	/**
	 * Sets the maximum Internet access level for this database.
	 * 
	 * @param level
	 *            The new maximum Internet level you want to set in the ACL. May be any of the following :<br>
	 * <br>
	 *            <ul>
	 *            <li>ACL.LEVEL_NOACCESS (0)</li>
	 *            <li>ACL.LEVEL_DEPOSITOR (1)</li>
	 *            <li>ACL.LEVEL_READER (2)</li>
	 *            <li>ACL.LEVEL_AUTHOR (3)</li>
	 *            <li>ACL.LEVEL_EDITOR (4)</li>
	 *            <li>ACL.LEVEL_DESIGNER (5)</li>
	 *            <li>ACL.LEVEL_MANAGER (6)</li>
	 *            </ul>
	 */
	@Override
	public void setInternetLevel(int level);

	/**
	 * Sets whether a consistent access control list is enforced across all replicas of a database.
	 * 
	 * <p>
	 * After setting this property, you must call {@link org.openntf.domino.ACL#save()} for the changes to take effect. If you do not call
	 * save, your changes to the ACL are lost.
	 * </p>
	 * 
	 * @param flag
	 *            The boolean value to set
	 */
	@Override
	public void setUniformAccess(boolean flag);

}
