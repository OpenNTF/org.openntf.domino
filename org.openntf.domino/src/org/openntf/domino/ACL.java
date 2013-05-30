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

import org.openntf.domino.types.DatabaseDescendant;

// TODO: Auto-generated Javadoc
/**
 * The Interface that represents the access control list (ACL) of an IBM Domino database.
 */
public interface ACL extends Base<lotus.domino.ACL>, lotus.domino.ACL, org.openntf.domino.ext.ACL, Iterable<org.openntf.domino.ACLEntry>,
		DatabaseDescendant {

	/**
	 * The Enum Level.
	 */
	public static enum Level {
		
		/** The noaccess. */
		NOACCESS(ACL.LEVEL_NOACCESS), 
 /** The despositor. */
 DESPOSITOR(ACL.LEVEL_DEPOSITOR), 
 /** The reader. */
 READER(ACL.LEVEL_READER), 
 /** The author. */
 AUTHOR(ACL.LEVEL_AUTHOR), 
 /** The editor. */
 EDITOR(
				ACL.LEVEL_EDITOR), 
 /** The designer. */
 DESIGNER(ACL.LEVEL_DESIGNER), 
 /** The manager. */
 MANAGER(ACL.LEVEL_MANAGER);

		/** The value_. */
		private final int value_;

		/**
		 * Instantiates a new level.
		 * 
		 * @param value
		 *            the value
		 */
		private Level(int value) {
			value_ = value;
		}

		/**
		 * Gets the value.
		 * 
		 * @return the value
		 */
		public int getValue() {
			return value_;
		}
	}

	/**
	 * Adds a role with the specified name to an ACL.
	 * 
	 * @param name
	 *            The name of the role to add
	 * @since lotus.domino 4.5.0
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
	 * @since lotus.domino 4.5.0
	 * 
	 */
	@Override
	public ACLEntry createACLEntry(String name, int level);

	/**
	 * Deletes a role with the specified name from an ACL.
	 * 
	 * @param name
	 *            The name of the role to delete
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public void deleteRole(String name);

	/**
	 * Returns the name of the administration server for an IBM Domino database.
	 * <p>
	 * The administration server is also the master lock server.<br>
	 * This property is an <code>empty string</code> if the database does not have an administration server.
	 * 
	 * @return the administration server
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public String getAdministrationServer();

	/**
	 * Given a name, finds its entry in an ACL.
	 * 
	 * @param name
	 *            The name whose ACL Entry you want to find. You must supply the complete name, but hierarchical names can be in abbreviated
	 *            format. Case is not significant.
	 * @return The {@link ACLEntry} that matches the name. If name is not in the ACL, returns <code>null</code>.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public ACLEntry getEntry(String name);

	/**
	 * Returns the first entry in an ACL.
	 * <p>
	 * The first entry is typically the -Default- entry.
	 * 
	 * @return The first {@link ACLEntry} in the ACL.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public ACLEntry getFirstEntry();

	/**
	 * The maximum Internet access level for this database.
	 * <p>
	 * <ul>
	 * <li>ACL.LEVEL_NOACCESS (0)
	 * <li>ACL.LEVEL_DEPOSITOR (1)
	 * <li>ACL.LEVEL_READER (2)
	 * <li>ACL.LEVEL_AUTHOR (3)
	 * <li>ACL.LEVEL_EDITOR (4)
	 * <li>ACL.LEVEL_DESIGNER (5)
	 * <li>ACL.LEVEL_MANAGER (6)
	 * </ul>
	 * 
	 * @return The current maximum internet access level of the database. May be any of the following :
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public int getInternetLevel();

	/**
	 * Returns the ACL Entry following the last ACL Entry retrieved.
	 * <p>
	 * The no-parameter method improves performance for remote operations because ACL entries are cached locally.
	 * <p>
	 * The order of the ACL entries is unspecified. The order is not alphabetical and does not correspond to UI displays.
	 * 
	 * @return The next {@link ACLEntry} in the ACL. Returns <code>null</code> if there are no more entries.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public ACLEntry getNextEntry();

	/**
	 * Returns the ACL entry following the entry specified as the parameter.
	 * <p>
	 * The order of the ACL entries is unspecified. The order is not alphabetical and does not correspond to UI displays.
	 * 
	 * @param entry
	 *            Any entry in the ACL. Cannot be <code>null</code>.
	 * @return The the {@link ACLEntry} following the entry specified as the parameter. Returns <code>null</code> if there are no more
	 *         entries.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public ACLEntry getNextEntry(lotus.domino.ACLEntry entry);

	/**
	 * Returns the IBM Domino database that owns the ACL.
	 * 
	 * @return The {@link Database} that this ACL belongs to.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public Database getParent();

	/**
	 * Returns all the roles defined in an access control list.
	 * <p>
	 * Each element in the vector is the name of a role. Each role name is surrounded by square brackets, for example, "[Supervisor]."
	 * <p>
	 * Notes Release 2 "privileges" are surrounded by parentheses. Properties and methods affecting roles, such as disableRole and
	 * enableRole, do not work on privileges.
	 * 
	 * @return A {@link java.lang.Vector Vector} of all the roles in the ACL. Elements are of type {@link java.lang.String String}
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public Vector<String> getRoles();

	/**
	 * Indicates whether the administration server for the database can modify all Names fields in a database.
	 * <p>
	 * The database must have an administration server. See {@link #getAdministrationServer} in ACL and {@link ACLEntry#isAdminServer} in
	 * 
	 * @return Returns <code>true</code> if the administration server can modify all names fields, <code>false</code> if it cannot.
	 *         {@link ACLEntry} to check for the administration server.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public boolean isAdminNames();

	/**
	 * Indicates whether the administration server for the database can modify Readers and Authors fields in a database.
	 * <p>
	 * The database must have an administration server. See {@link #getAdministrationServer} in ACL and {@link ACLEntry#isAdminServer} in
	 * 
	 * @return Returns <code>true</code> if the administration server can modify Readers and Authors fields, <code>false</code> if it
	 *         cannot. {@link ACLEntry} to check for the administration server.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public boolean isAdminReaderAuthor();

	/**
	 * Indicates whether extended access is enabled.
	 * <p>
	 * In the Notes UI, this property is in the advanced section of ACL properties: "Enable Extended Access." and only applies to Domino
	 * Directory, Extended Directory Catalog, and Administration Requests databases.
	 * 
	 * @return Returns <code>true</code> if extended access is enabled, <code>false</code> if it is not enabled.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public boolean isExtendedAccess();

	/**
	 * Indicates whether a consistent access control list is enforced across all replicas of a database.
	 * 
	 * @return Returns <code>true</code> if uniform access is enabled, <code>false</code> if it is not enabled.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public boolean isUniformAccess();

	/**
	 * Removes an entry from the ACL.
	 * <p>
	 * After calling this method, you must call {@link #save()} for the changes to take effect. If you do not call save, your changes to the
	 * ACL are lost.
	 * 
	 * @param name
	 *            The name of the person, group, or server whose entry you want to remove. You must supply the complete name, but
	 *            hierarchical names can be in abbreviated format. Case is not significant.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public void removeACLEntry(String name);

	/**
	 * Changes the name of a role.
	 * <p>
	 * After calling this method, you must call {@link #save} for the changes to take effect. If you do not call save, your changes to the
	 * ACL are lost.
	 * <p>
	 * When you rename a role, any entries in the ACL that had the old role get the new role.
	 * 
	 * @param oldName
	 *            The current name of the role. Do not put square brackets around the name.
	 * @param newName
	 *            The current name of the role. Do not put square brackets around the name.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public void renameRole(String oldName, String newName);

	/**
	 * Saves changes that you've made to the ACL.
	 * <p>
	 * If you don't call save before closing a database, the changes you've made to its ACL are lost.
	 * 
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public void save();

	/**
	 * Sets the name of the administration server for a database.
	 * <p>
	 * The administration server is also the master lock server.<br>
	 * This property is an <code>empty string</code> if the database does not have an administration server.
	 * 
	 * @param server
	 *            the name of the server you want to set as the Administration Server.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public void setAdministrationServer(String server);

	/**
	 * Sets whether the administration server for the database can modify all Names fields in a database.
	 * <p>
	 * The database must have an administration server. See {@link ACL#getAdministrationServer} in ACL and
	 * 
	 * @param flag
	 *            The boolean value to set {@link org.openntf.domino.ACLEntry#isAdminServer()} in {@link ACLEntry} to set the administration
	 *            server.
	 *            <p>
	 *            After setting this property, you must call {@link #save} for the changes to take effect. If you do not call save, your
	 *            changes to the ACL are lost.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public void setAdminNames(boolean flag);

	/**
	 * Sets whether the administration server for the database can modify Readers and Authors fields in a database.
	 * <p>
	 * The database must have an administration server. See {@link ACL#getAdministrationServer} in ACL and
	 * 
	 * @param flag
	 *            The boolean value to set {@link org.openntf.domino.ACLEntry#isAdminServer} in {@link ACLEntry} to set the administration
	 *            server.
	 *            <p>
	 *            After setting this property, you must call {@link #save} for the changes to take effect. If you do not call save, your
	 *            changes to the ACL are lost.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public void setAdminReaderAuthor(boolean flag);

	/**
	 * Sets whether extended access is enabled.
	 * <p>
	 * In the Notes UI, this property is in the advanced section of ACL properties: "Enable Extended Access." and only applies to Domino
	 * Directory, Extended Directory Catalog, and Administration Requests databases.
	 * <p>
	 * Setting this property true automatically sets {@link #setUniformAccess(boolean)} true. Setting this property false does not
	 * automatically set {@link #setUniformAccess(boolean)} false.
	 * <p>
	 * After setting this property, you must call {@link #save} for the changes to take effect. If you do not call save, your changes to the
	 * ACL are lost.
	 * 
	 * @param flag
	 *            The boolean value to set
	 * @since lotus.domino 4.5.0
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
	 *            <li>ACL.LEVEL_NOACCESS (0)
	 *            <li>ACL.LEVEL_DEPOSITOR (1)
	 *            <li>ACL.LEVEL_READER (2)
	 *            <li>ACL.LEVEL_AUTHOR (3)
	 *            <li>ACL.LEVEL_EDITOR (4)
	 *            <li>ACL.LEVEL_DESIGNER (5)
	 *            <li>ACL.LEVEL_MANAGER (6)
	 *            </ul>
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public void setInternetLevel(int level);

	/**
	 * Sets the maximum Internet access level for this database.
	 * 
	 * @param level
	 *            The new maximum Internet level you want to set in the ACL, of type ACL.Level.
	 * @since openntf.domino 1.0.0
	 */
	public void setInternetLevel(Level level);

	/**
	 * Sets whether a consistent access control list is enforced across all replicas of a database.
	 * 
	 * <p>
	 * After setting this property, you must call {@link #save} for the changes to take effect. If you do not call save, your changes to the
	 * ACL are lost.
	 * </p>
	 * 
	 * @param flag
	 *            The boolean value to set
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public void setUniformAccess(boolean flag);

}
