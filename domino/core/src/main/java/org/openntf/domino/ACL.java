/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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
package org.openntf.domino;

import java.util.EnumSet;
import java.util.Vector;

import org.openntf.domino.types.DatabaseDescendant;
import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.Resurrectable;

/**
 * The Interface that represents the access control list (ACL) of an IBM Domino database.
 * <p>
 * Every Database object contains an <code>ACL</code> object representing the access control list of that database. To get it, use
 * {@link Database#getACL()}.
 * </p>
 * <p>
 * The ACL class supports the foreach loop to iterate over the entries like in the example:
 *
 * <pre>
 * for (ACLEntry aclEntry : db.getACL()) {
 * 	// process the entry
 * }
 * </pre>
 * </p>
 * <h3>Usage</h3>
 * <p>
 * The {@link Database} class has three methods you can use to access and modify an ACL without getting an ACL object:
 * {@link Database#queryAccess(String)}, {@link Database#grantAccess(String, Level)}, and {@link Database#revokeAccess(String)}. However,
 * using these methods at the same time that an ACL object is in use may produce inconsistent results.
 * </p>
 */
public interface ACL extends Base<lotus.domino.ACL>, lotus.domino.ACL, org.openntf.domino.ext.ACL, Iterable<org.openntf.domino.ACLEntry>,
		Resurrectable, DatabaseDescendant {

	/**
	 * @author Roland Praml, Foconis AG
	 *
	 */
	public static class Schema extends FactorySchema<ACL, lotus.domino.ACL, Database> {
		@Override
		public Class<ACL> typeClass() {
			return ACL.class;
		}

		@Override
		public Class<lotus.domino.ACL> delegateClass() {
			return lotus.domino.ACL.class;
		}

		@Override
		public Class<Database> parentClass() {
			return Database.class;
		}
	};

	/** the Schema */
	public static final Schema SCHEMA = new Schema();

	/**
	 * The Enum Level, corresponding to the ACL Level
	 */
	public static enum Level {

		/** The noaccess. */
		NOACCESS(ACL.LEVEL_NOACCESS),
		/** The depositor. */
		DEPOSITOR(ACL.LEVEL_DEPOSITOR),
		/** The reader. */
		READER(ACL.LEVEL_READER),
		/** The author. */
		AUTHOR(ACL.LEVEL_AUTHOR),
		/** The editor. */
		EDITOR(ACL.LEVEL_EDITOR),
		/** The designer. */
		DESIGNER(ACL.LEVEL_DESIGNER),
		/** The manager. */
		MANAGER(ACL.LEVEL_MANAGER);

		public static Level getLevel(final int value) {
			for (Level level : Level.values()) {
				if (level.getValue() == value) {
					return level;
				}
			}
			return null;
		}

		/** The value_. */
		private final int value_;

		/**
		 * Instantiates a new level.
		 *
		 * @param value
		 *            the value
		 */
		private Level(final int value) {
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

	public static enum Privilege {
		CREATE_DOCS(Database.DBACL_CREATE_DOCS), DELETE_DOCS(Database.DBACL_DELETE_DOCS),
		CREATE_PRIVATE_AGENTS(Database.DBACL_CREATE_PRIV_AGENTS), CREATE_PRIVATE_VIEWS(Database.DBACL_CREATE_PRIV_FOLDERS_VIEWS),
		CREATE_SHARED_VIEWS(Database.DBACL_CREATE_SHARED_FOLDERS_VIEWS), CREATE_LS_JAVA_AGENTS(Database.DBACL_CREATE_SCRIPT_AGENTS),
		READ_PUBLIC_DOCS(Database.DBACL_READ_PUBLIC_DOCS), WRITE_PUBLIC_DOCS(Database.DBACL_WRITE_PUBLIC_DOCS),
		REPLICATE_COPY_DOCS(Database.DBACL_REPLICATE_COPY_DOCS);

		public static Privilege getPrivilege(final int value) {
			for (Privilege privilege : Privilege.values()) {
				if (privilege.getValue() == value) {
					return privilege;
				}
			}
			return null;
		}

		public static EnumSet<Privilege> getPrivileges(final int value) {
			EnumSet<Privilege> result = EnumSet.noneOf(Privilege.class);
			if ((value & Database.DBACL_CREATE_DOCS) > 0) {
				result.add(CREATE_DOCS);
			}
			if ((value & Database.DBACL_DELETE_DOCS) > 0) {
				result.add(DELETE_DOCS);
			}
			if ((value & Database.DBACL_CREATE_PRIV_AGENTS) > 0) {
				result.add(CREATE_PRIVATE_AGENTS);
			}
			if ((value & Database.DBACL_CREATE_PRIV_FOLDERS_VIEWS) > 0) {
				result.add(CREATE_PRIVATE_VIEWS);
			}
			if ((value & Database.DBACL_CREATE_SHARED_FOLDERS_VIEWS) > 0) {
				result.add(CREATE_SHARED_VIEWS);
			}
			if ((value & Database.DBACL_CREATE_SCRIPT_AGENTS) > 0) {
				result.add(CREATE_LS_JAVA_AGENTS);
			}
			if ((value & Database.DBACL_READ_PUBLIC_DOCS) > 0) {
				result.add(READ_PUBLIC_DOCS);
			}
			if ((value & Database.DBACL_WRITE_PUBLIC_DOCS) > 0) {
				result.add(WRITE_PUBLIC_DOCS);
			}
			if ((value & Database.DBACL_REPLICATE_COPY_DOCS) > 0) {
				result.add(REPLICATE_COPY_DOCS);
			}
			return result;
		}

		/** The value_. */
		private final int value_;

		/**
		 * Instantiates a new level.
		 *
		 * @param value
		 *            the value
		 */
		private Privilege(final int value) {
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
	 * <p>
	 * You must call {@link ACL#save()} if you want the modified ACL to be saved to disk.
	 * </p>
	 *
	 * @param name
	 *            The name of the role to add. Do not put brackets around the name.
	 *
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public void addRole(final String name);

	/**
	 * Creates an entry in the ACL with the name and level that you specify.
	 * <p>
	 * You must call {@link ACL#save()} if you want the modified ACL to be saved to disk.
	 * </p>
	 *
	 * @param name
	 *            The name of the person, group, or server for whom you want to create an entry in the ACL. You must supply the complete
	 *            name, but hierarchical names can be in abbreviated format. Case is not significant.
	 * @param level
	 *            The level that you want to assign to this person, group, or server in the ACL. May be any of the following :<br>
	 *            <br>
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
	 * @deprecated in favour of {@link org.openntf.domino.ext.ACL#createACLEntry(String, Level)}
	 *
	 */
	@Deprecated
	@Override
	public ACLEntry createACLEntry(final String name, final int level);

	/**
	 * Deletes a role with the specified name from an ACL.
	 * <p>
	 * You must call {@link ACL#save()} if you want the modified ACL to be saved to disk.
	 * </p>
	 *
	 * @param name
	 *            The name of the role to delete. Do not put brackets around the name.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public void deleteRole(final String name);

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
	 * <p>
	 * This method can find people, groups, or servers in an ACL. If a person is not listed explicitly in the ACL, but is a member of a
	 * group listed in the ACL, <code>getEntry</code> does not find that person's name.
	 * </p>
	 *
	 * @param name
	 *            The name whose ACL Entry you want to find. You must supply the complete name, but hierarchical names can be in abbreviated
	 *            format. Case is not significant.
	 * @return The {@link ACLEntry} that matches the name. If name is not in the ACL, returns <code>null</code>.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public ACLEntry getEntry(final String name);

	/**
	 * Returns the first entry in an ACL.
	 * <p>
	 * The first entry is typically the -Default- entry.
	 * </p>
	 *
	 * @return The first {@link ACLEntry} in the ACL.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public ACLEntry getFirstEntry();

	/**
	 * The maximum Internet access level for this database.
	 *
	 * @return The current maximum internet access level of the database. May be any of the following :
	 *         <ul>
	 *         <li>ACL.LEVEL_NOACCESS (0)
	 *         <li>ACL.LEVEL_DEPOSITOR (1)
	 *         <li>ACL.LEVEL_READER (2)
	 *         <li>ACL.LEVEL_AUTHOR (3)
	 *         <li>ACL.LEVEL_EDITOR (4)
	 *         <li>ACL.LEVEL_DESIGNER (5)
	 *         <li>ACL.LEVEL_MANAGER (6)
	 *         </ul>
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public int getInternetLevel();

	/**
	 * Returns the ACL Entry following the last ACL Entry retrieved.
	 * <p>
	 * The no-parameter method improves performance for remote operations because ACL entries are cached locally.
	 * </p>
	 * <p>
	 * The order of the ACL entries is unspecified. The order is not alphabetical and does not correspond to UI displays.
	 * </p>
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
	 * </p>
	 *
	 * @param entry
	 *            Any entry in the ACL. Cannot be <code>null</code>.
	 * @return The the {@link ACLEntry} following the entry specified as the parameter. Returns <code>null</code> if there are no more
	 *         entries.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public ACLEntry getNextEntry(final lotus.domino.ACLEntry entry);

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
	 * The database must have an administration server. See {@link #getAdministrationServer} in ACL and {@link ACLEntry#isAdminServer} to
	 * check for and set the administration server.
	 * </p>
	 * <p>
	 * You must call {@link ACL#save()} if you want the modified ACL to be saved to disk.
	 * </p>
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
	 * The database must have an administration server. See {@link #getAdministrationServer} in ACL and {@link ACLEntry#isAdminServer} to
	 * check for and set the administration server.
	 * </p>
	 * <p>
	 * You must call {@link ACL#save()} if you want the modified ACL to be saved to disk.
	 * </p>
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
	public void removeACLEntry(final String name);

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
	public void renameRole(final String oldName, final String newName);

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
	public void setAdministrationServer(final String server);

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
	public void setAdminNames(final boolean flag);

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
	public void setAdminReaderAuthor(final boolean flag);

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
	public void setExtendedAccess(final boolean flag);

	/**
	 * Sets the maximum Internet access level for this database.
	 *
	 * @param level
	 *            The new maximum Internet level you want to set in the ACL. May be any of the following :<br>
	 *            <br>
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
	public void setInternetLevel(final int level);

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
	public void setUniformAccess(final boolean flag);

}
