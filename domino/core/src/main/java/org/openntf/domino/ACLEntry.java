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
package org.openntf.domino;

import java.util.Vector;

import org.openntf.domino.types.DatabaseDescendant;
import org.openntf.domino.types.FactorySchema;

/**
 * Represents an entry in the Access Control List (ACL) of a Notes database. An entry may be for a person, a group, or a server. To create a
 * new ACLEntry object, use {@link ACL#createACLEntry(String, org.openntf.domino.ACL.Level)}.
 * <p>
 * The ACL class provides three ways to access an existing ACLEntry:
 * <ul>
 * <li>To access an entry in an ACL when you know its name, use {@link ACL#getEntry(String)}.</li>
 * <li>To access the first entry in the ACL, use {@link ACL#getFirstEntry()}.</li>
 * <li>To access entries after the first one, use {@link ACL#getNextEntry()}</li>
 * </ul>
 * <p>
 * After modifying an ACL entry, you must call the {@link ACL#save()} if you want the modified ACL to be saved to disk.
 * </p>
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

	/**
	 * Disables a role for an entry.
	 * <p>
	 * You must call {@link ACL#save()} if you want the modified ACL to be saved to disk.
	 * </p>
	 * <p>
	 * The role must exist in the ACL. If the role exists, but is already disabled for the entry, this method does nothing.
	 * </p>
	 *
	 * @param role
	 *            The name of the role to disable. Do not specify the brackets.
	 */
	@Override
	public void disableRole(final String role);

	/**
	 * Enables a role for an entry.
	 * <p>
	 * You must call {@link ACL#save()} if you want the modified ACL to be saved to disk.
	 * </p>
	 * <p>
	 * The role must exist in the ACL. If the role exists, but is already enabled for the entry, this method does nothing.
	 * </p>
	 *
	 * @param role
	 *            The name of the role to enable. Do not specify the brackets.
	 */
	@Override
	public void enableRole(final String role);

	/**
	 * The access level this entry has for this database.
	 *
	 * @return The access level this entry has for this database. One of the values:
	 *         <ul>
	 *         <li>ACL.LEVEL_NOACCESS</li>
	 *         <li>ACL.LEVEL_DEPOSITOR</li>
	 *         <li>ACL.LEVEL_READER</li>
	 *         <li>ACL.LEVEL_AUTHOR</li>
	 *         <li>ACL.LEVEL_EDITOR</li>
	 *         <li>ACL.LEVEL_DESIGNER</li>
	 *         <li>ACL.LEVEL_MANAGER</li>
	 *         </ul>
	 *
	 */
	@Override
	public int getLevel();

	/**
	 * The name of an ACL entry. Hierarchical names are returned in canonical format.
	 *
	 * @return The name of an ACL entry.
	 *
	 */
	@Override
	public String getName();

	/**
	 * The name of an ACL entry as a {@link Name} object.
	 */
	@Override
	public Name getNameObject();

	/**
	 * The access control list that contains this entry.
	 */
	@Override
	public ACL getParent();

	/**
	 * The roles that are enabled for an entry. Each element in the vector is the name of a role. Each role name is surrounded by brackets,
	 * for example, "[Supervisor]."
	 */
	@Override
	public Vector<String> getRoles();

	/**
	 * The user type this entry has for this database.
	 *
	 * @return One of the values:
	 *         <ul>
	 *         <li>ACLEntry.TYPE_MIXED_GROUP</li>
	 *         <li>ACLEntry.TYPE_PERSON</li>
	 *         <li>ACLEntry.TYPE_PERSON_GROUP</li>
	 *         <li>ACLEntry.TYPE_SERVER</li>
	 *         <li>ACLEntry.TYPE_SERVER_GROUP</li>
	 *         <li>ACLEntry.TYPE_UNSPECIFIED</li>
	 *         </ul>
	 */
	@Override
	public int getUserType();

	/**
	 * Indicates whether an entry that is an administration server can modify reader and author fields.
	 *
	 * @return true if an entry that is an administration server can modify reader and author fields.
	 *
	 *
	 */
	@Override
	public boolean isAdminReaderAuthor();

	/**
	 * Indicates whether this entry is an administration server.
	 */
	@Override
	public boolean isAdminServer();

	/**
	 * For an entry with Author access to a database, indicates whether the entry is allowed to create new documents.
	 * <p>
	 * If an entry has Depositor, Editor, Designer, or Manager access, this property is always true. If an entry has Reader or No access,
	 * this property is always false.
	 * </p>
	 */
	@Override
	public boolean isCanCreateDocuments();

	/**
	 * For an entry with Reader access to a database, indicates whether the entry is allowed to create LotusScript or Java agents.
	 * <p>
	 * If an entry has Manager access, this property is always true. If an entry has Depositor or No access, this property is always false.
	 * </p>
	 */
	@Override
	public boolean isCanCreateLSOrJavaAgent();

	/**
	 * For an entry with Editor, Author, or Reader access, indicates whether the entry can create private agents in a database.
	 * <p>
	 * If an entry has Designer or Manager access, this property is always true. If an entry has Depositor or No access, this property is
	 * always false.
	 * </p>
	 */
	@Override
	public boolean isCanCreatePersonalAgent();

	/**
	 * For an entry with Editor, Author, or Reader access, indicates whether the entry can create personal folders in a database.
	 * <p>
	 * If an entry has Designer or Manager access, this property is always true. If an entry has Depositor or No access, this property is
	 * always false.
	 * </p>
	 */
	@Override
	public boolean isCanCreatePersonalFolder();

	/**
	 * For an entry with Editor access to a database, indicates whether the entry can create shared folders in the database.
	 * <p>
	 * If an entry has Manager or Designer access, this property is always true. If an entry has Author, Reader, Depositor, or No access,
	 * this property is always false.
	 * </p>
	 */
	@Override
	public boolean isCanCreateSharedFolder();

	/**
	 * For an entry with Author access or higher, indicates whether the entry can delete documents from a database.
	 * <p>
	 * If an entry has Reader, Depositor, or No access, this property is always false.
	 * </p>
	 */
	@Override
	public boolean isCanDeleteDocuments();

	/**
	 * For an entry with Reader access or higher to a database, indicates whether an entry can replicate or copy documents.
	 * <p>
	 * If an entry has Depositor or No access, this property is always false.
	 * </p>
	 */
	@Override
	public boolean isCanReplicateOrCopyDocuments();

	/**
	 * Indicates whether this is a group ACL entry.
	 * <p>
	 * <code>isGroup</code> is true if {@link ACLEntry#getUserType()} is TYPE_MIXED_GROUP, TYPE_PERSON_GROUP, or TYPE_SERVER_GROUP.
	 * </p>
	 */
	@Override
	public boolean isGroup();

	/**
	 * Indicates whether this is a person ACL entry.
	 *
	 * @return true if {@link #getUserType()}is TYPE_PERSON, TYPE_MIXED_GROUP, or TYPE_PERSON_GROUP.
	 */
	@Override
	public boolean isPerson();

	/**
	 * For an entry with Depositor or No access, indicates whether the entry is a public reader of the database. If an entry has Manager,
	 * Designer, Editor, Author, or Reader access, this property is always true.
	 */
	@Override
	public boolean isPublicReader();

	/**
	 * For an entry with Author, Reader, Depositor, or No access, indicates whether the entry is a public writer of the database.
	 * <p>
	 * If an entry has Manager, Designer, or Editor access, this property is always true.
	 * </p>
	 */
	@Override
	public boolean isPublicWriter();

	/**
	 * Indicates whether a role is enabled for an entry.
	 * <p>
	 * The role must exist in the ACL.
	 * </p>
	 *
	 * @param role
	 *            The name of the role.
	 * @return true if the role is enabled for this entry or false if not
	 */
	@Override
	public boolean isRoleEnabled(final String role);

	/**
	 * Indicates whether this is a server ACL entry.
	 *
	 * @return true if {@link #getUserType()} is TYPE_SERVER, TYPE_MIXED_GROUP, or TYPE_SERVER_GROUP.
	 *
	 */
	@Override
	public boolean isServer();

	/**
	 * Removes an entry from an access control list.
	 * <p>
	 * You must call {@link ACL#save()} on the ACL if you want the modified ACL to be saved to disk.
	 * </p>
	 *
	 */
	@Override
	public void remove();

	/**
	 * Sets whether this entry that is an administration server can modify reader and author fields.
	 * <p>
	 * If the ACL entry is not an administration server, <code>setAdminReaderAuthor(true)</code> does nothing. No exception is thrown and
	 * the property remains false. {@link #isAdminServer()} and {@link #setAdminServer(boolean)} to check and change the entry.
	 * </p>
	 * <p>
	 * For <code>setAdminReaderAuthor</code>, you must call {@link ACL#save()} if you want the modified ACL to be saved to disk.
	 * </p>
	 *
	 * @param flag whether the entry should modify reader and author fields
	 */
	@Override
	public void setAdminReaderAuthor(final boolean flag);

	/**
	 * Sets whether this entry is an administration server.
	 * <p>
	 * You must call {@link ACL#save()} if you want the modified ACL to be saved to disk.
	 * </p>
	 *
	 * @param flag whether the entry should be an administration server
	 *
	 */
	@Override
	public void setAdminServer(final boolean flag);

	/**
	 * For an entry with Author access to a database, sets whether the entry is allowed to create new documents.
	 * <p>
	 * You must call {@link ACL#save()} if you want the modified ACL to be saved to disk.
	 * </p>
	 * <p>
	 * Setting this property has no effect on an ACL entry unless the entry has Author access to a database. If an entry has Depositor,
	 * Editor, Designer, or Manager access, this property is always true. If an entry has Reader or No access, this property is always
	 * false.
	 * </p>
	 * <p>
	 * By default, this property is false for a new entry with Author access.
	 * </p>
	 *
	 * @param flag whether the entry should be able to create documents
	 */
	@Override
	public void setCanCreateDocuments(final boolean flag);

	/**
	 * For an entry with Reader access to a database, sets whether the entry is allowed to create LotusScript or Java agents.
	 * <p>
	 * You must call {@link ACL#save()} if you want the modified ACL to be saved to disk.
	 * </p>
	 * <p>
	 * Setting this property has no effect on an ACL entry unless the entry has Designer, Editor, Author, or Reader access to a database. If
	 * an entry has Manager access, this property is always true. If an entry has Depositor or No access, this property is always false.
	 * </p>
	 * <p>
	 * By default, this property is false for a new entry with Designer, Editor, Author, or Reader access.
	 * </p>
	 *
	 * @param flag whether the entry should be able to create LS or Java agents
	 */
	@Override
	public void setCanCreateLSOrJavaAgent(final boolean flag);

	/**
	 * For an entry with Editor, Author, or Reader access, sets whether the entry can create private agents in a database.
	 * <p>
	 * You must call {@link ACL#save()} if you want the modified ACL to be saved to disk.
	 * </p>
	 * <p>
	 * Setting this property has no effect on an ACL entry unless the entry has Editor, Author, or Reader access to a database. If an entry
	 * has Designer or Manager access, this property is always true. If an entry has Depositor or No access, this property is always false.
	 * </p>
	 * <p>
	 * By default, this property is false for a new entry with Editor, Author, or Reader access.
	 * </p>
	 *
	 * @param flag whether the entry should be able to create personal agents
	 */
	@Override
	public void setCanCreatePersonalAgent(final boolean flag);

	/**
	 * For an entry with Editor, Author, or Reader access, sets whether the entry can create personal folders in a database.
	 * <p>
	 * You must call {@link ACL#save()} if you want the modified ACL to be saved to disk.
	 * </p>
	 * <p>
	 * Setting this property has no effect on an ACL entry unless the entry has Editor, Author, or Reader access to a database. If an entry
	 * has Designer or Manager access, this property is always true. If an entry has Depositor or No access, this property is always false.
	 * </p>
	 * <p>
	 * By default, this property is false for a new entry with Editor, Author, or Reader access.
	 * </p>
	 *
	 * @param flag whether the entry should be able to create personal folders
	 *
	 */
	@Override
	public void setCanCreatePersonalFolder(final boolean flag);

	/**
	 * For an entry with Editor access to a database, sets whether the entry can create shared folders in the database.
	 * <p>
	 * You must call {@link ACL#save()} if you want the modified ACL to be saved to disk.
	 * </p>
	 * <p>
	 * Setting this property has no effect on an ACL entry unless the entry has Editor access to a database. If an entry has Manager or
	 * Designer access, this property is always true. If an entry has Author, Reader, Depositor, or No access, this property is always
	 * false.
	 * </p>
	 * <p>
	 * By default, this property is false for a new entry with Editor access.
	 * </p>
	 *
	 * @param flag whether the entry should be able to create shared folders
	 */
	@Override
	public void setCanCreateSharedFolder(final boolean flag);

	/**
	 * For an entry with Author access or higher, sets whether the entry can delete documents from a database.
	 * <p>
	 * You must call {@link ACL#save()} if you want the modified ACL to be saved to disk.
	 * </p>
	 * <p>
	 * Setting this property has no effect on an ACL entry unless the entry has Manager, Designer, Editor, or Author access to a database.
	 * If an entry has Reader, Depositor, or No access, this property is always false.
	 * </p>
	 * <p>
	 * By default, this property is false for a new entry with Manager, Designer, Editor, or Author access.
	 * </p>
	 *
	 * @param flag whether the entry should be able to delete documents
	 *
	 */
	@Override
	public void setCanDeleteDocuments(final boolean flag);

	/**
	 * For an entry with Reader access or higher to a database, sets whether an entry can replicate or copy documents.
	 * <p>
	 * You must call {@link ACL#save()} for the changes to take effect
	 * </p>
	 * <p>
	 * Setting this property has no effect on an ACL entry unless the entry has Reader access or higher to a database. If an entry has
	 * Depositor or No access, this property is always false.
	 * </p>
	 *
	 */
	@Override
	public void setCanReplicateOrCopyDocuments(final boolean flag);

	/**
	 * Sets whether this is a group ACL entry.
	 *
	 * <p>
	 * You must call {@link ACL#save()} if you want the modified ACL to be saved to disk.
	 * </p>
	 *
	 * @param flag whether the entry is a group
	 */
	@Override
	public void setGroup(final boolean flag);

	/**
	 * Sets the access level this entry has for this database.
	 * <p>
	 * You must call {@link ACL#save()} if you want the modified ACL to be saved to disk.
	 * </p>
	 *
	 * @param level
	 *            One of
	 *            <ul>
	 *            <li>ACL.LEVEL_NOACCESS</li>
	 *            <li>ACL.LEVEL_DEPOSITOR</li>
	 *            <li>ACL.LEVEL_READER</li>
	 *            <li>ACL.LEVEL_AUTHOR</li>
	 *            <li>ACL.LEVEL_EDITOR</li>
	 *            <li>ACL.LEVEL_DESIGNER</li>
	 *            <li>ACL.LEVEL_MANAGER</li>
	 *            </ul>
	 *
	 */
	@Override
	public void setLevel(final int level);

	/**
	 * Sets the access level this entry has for this database.
	 * <p>
	 * You must call {@link ACL#save()} if you want the modified ACL to be saved to disk.
	 * </p>
	 *
	 * @param level
	 *            the new level
	 */
	@Override
	public void setLevel(final ACL.Level level);

	/**
	 * Sets the name of an ACL entry.
	 * <p>
	 * You must call {@link ACL#save()} if you want the modified ACL to be saved to disk.
	 * </p>
	 * <p>
	 * If you change the name of an entry, the other properties of the entry (such as Level) are preserved.
	 * </p>
	 *
	 * @param name
	 *            new name for this entry, cannot be null
	 */
	@Override
	public void setName(final lotus.domino.Name name);

	/**
	 * Sets the name of an ACL entry.
	 * <p>
	 * You must call {@link ACL#save()} if you want the modified ACL to be saved to disk.
	 * </p>
	 * <p>
	 * If you change the name of an entry, the other properties of the entry (such as Level) are preserved.
	 * </p>
	 *
	 * @param name
	 *            the new name in canonical or abbreviated format
	 */
	@Override
	public void setName(final String name);

	/**
	 * Sets whether this is a person ACL entry.
	 * <p>
	 * You must call {@link ACL#save()} if you want the modified ACL to be saved to disk.
	 * </p>
	 *
	 * @param flag whether the entry is a person
	 */
	@Override
	public void setPerson(final boolean flag);

	/**
	 * For an entry with Depositor or No access, sets whether the entry is a public reader of the database.
	 * <p>
	 * Setting this property has no effect on an ACL entry unless the entry has Depositor or No access to a database. If an entry has
	 * Manager, Designer, Editor, Author, or Reader access, this property is always true.
	 * </p>
	 * <p>
	 * By default, this property is false for a new entry with Depositor or No access.
	 * </p>
	 *
	 * @param flag whether the entry should be able to read public documents
	 */
	@Override
	public void setPublicReader(final boolean flag);

	/**
	 * For an entry with Author, Reader, Depositor, or No access, sets whether the entry is a public writer of the database.
	 * <p>
	 * Setting this property has no effect on an ACL entry unless the entry has Author, Reader, Depositor, or No access to a database. If an
	 * entry has Manager, Designer, or Editor access, this property is always true.
	 * </p>
	 * <p>
	 * By default, this property is false for a new entry with Author, Reader, Depositor, or No access.
	 * </p>
	 *
	 * @param flag whether the entry should be able to write public documents
	 */
	@Override
	public void setPublicWriter(final boolean flag);

	/**
	 * Sets whether this is a server ACL entry.
	 *
	 * <p>
	 * You must call {@link ACL#save()} if you want the modified ACL to be saved to disk.
	 * </p>
	 *
	 * @param flag whether the entry is a server
	 */
	@Override
	public void setServer(final boolean flag);

	/**
	 * Sets the user type this entry has for this database.
	 * <p>
	 * You must call {@link ACL#save()} if you want the modified ACL to be saved to disk.
	 * </p>
	 *
	 * @param type
	 *            One of
	 *            <ul>
	 *            <li>ACLEntry.TYPE_MIXED_GROUP</li>
	 *            <li>ACLEntry.TYPE_PERSON</li>
	 *            <li>ACLEntry.TYPE_PERSON_GROUP</li>
	 *            <li>ACLEntry.TYPE_SERVER</li>
	 *            <li>ACLEntry.TYPE_SERVER_GROUP</li>
	 *            <li>ACLEntry.TYPE_UNSPECIFIED</li>
	 *            </ul>
	 */
	@Override
	public void setUserType(final int type);

}
