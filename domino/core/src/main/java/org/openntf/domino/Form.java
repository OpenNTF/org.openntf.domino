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
import org.openntf.domino.types.Design;
import org.openntf.domino.types.FactorySchema;

/**
 * Represents a form in a database.
 * <h3>Notable enhancements and changes</h3>
 * <ul>
 * <li>Easily locate all documents created with this form by calling {@link Database#search(String)} with a formula returned by
 * {@link org.openntf.domino.ext.Form#getSelectionFormula()}</li>
 * <li>Get all documents created with this form as a {@link NoteCollection} using
 * {@link org.openntf.domino.ext.Form#getNoteCollection()}</li>
 * </ul>
 * <h3>Access</h3>
 * <p>
 * There are two ways to access a form:
 * </p>
 * <ul>
 * <li>To get all the forms in a database, use {@link Database#getForms()}.</li>
 * <li>To get a form by its name, use {@link Database#getForm(String)}.</li>
 * </ul>
 * <p>
 * You can't access private forms belonging to other people.
 * </p>
 * 
 * @see org.openntf.domino.ext.Form
 */
public interface Form extends Base<lotus.domino.Form>, lotus.domino.Form, org.openntf.domino.ext.Form, Design, DatabaseDescendant {

	public static class Schema extends FactorySchema<Form, lotus.domino.Form, Database> {
		@Override
		public Class<Form> typeClass() {
			return Form.class;
		}

		@Override
		public Class<lotus.domino.Form> delegateClass() {
			return lotus.domino.Form.class;
		}

		@Override
		public Class<Database> parentClass() {
			return Database.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/**
	 * The aliases of a form.
	 * <p>
	 * This property returns all but the first in the list of all the form's names. The {@link #getName() Name} property returns the first
	 * name in the list.
	 * </p>
	 *
	 * @return The aliases of a form.
	 *
	 */
	@Override
	public Vector<String> getAliases();

	/**
	 * The names of all the fields of a form.
	 */
	@Override
	public Vector<String> getFields();

	/**
	 * Gets the type of a field on the form.
	 *
	 * @param name
	 *            The name of the field.
	 * @return The type of the field as specified in {@link Item#getType()}.
	 */
	@Override
	public int getFieldType(final String name);

	/**
	 * Who can create documents with this form.
	 *
	 * @return names, groups and roles from the $FormUsers field
	 */
	@Override
	public Vector<String> getFormUsers();

	/**
	 * The Domino URL of a form when HTTP protocols are in effect.
	 * <p>
	 * If HTTP protocols are not available, this property returns an empty string.
	 * </p>
	 * <p>
	 * See {@link Session#resolve(String)} for additional information and examples.
	 * </p>
	 *
	 * @return The Domino URL of a form when HTTP protocols are in effect.
	 *
	 */
	@Override
	public String getHttpURL();

	/**
	 * The names of the holders of a lock.
	 * <p>
	 * If the form is locked, the vector contains the names of the lock holders. The form can be locked by one or more users or groups.
	 * </p>
	 * <p>
	 * If the form is not locked, the vector contains one element whose value is an empty string ("").
	 * </p>
	 *
	 * @return The names of the holders of a lock.
	 *
	 */
	@Override
	public Vector<String> getLockHolders();

	/**
	 * The name of a form.
	 * <p>
	 * This property returns the first name associated with a form. Use the {@link #getAliases()} property to return any additional names.
	 * </p>
	 *
	 * @return The name of a form.
	 *
	 */
	@Override
	public String getName();

	/**
	 * The Domino URL of a form when Notes protocols are in effect.
	 * <p>
	 * If Notes protocols are not available, this property returns an empty string.
	 * <p>
	 * See {@link Session#resolve(String)} for additional information and examples.
	 * </p>
	 *
	 * @return The Domino URL of a form when Notes protocols are in effect.
	 *
	 */
	@Override
	public String getNotesURL();

	/**
	 * The database that contains a form.
	 */
	@Override
	public Database getParent();

	/**
	 * Default read access for documents created with this form.
	 *
	 * @return names, groups and roles from the $Readers field
	 */
	@Override
	public Vector<String> getReaders();

	/**
	 * Returns the Domino URL for the database that contains this form.
	 */
	@Override
	public String getURL();

	/**
	 * Protects $Readers items from being overwritten by replication.
	 *
	 * @return true to protect $Readers, false to not protect $Readers
	 */
	@Override
	public boolean isProtectReaders();

	/**
	 * Protects $FormUsers items from being overwritten by replication.
	 */
	@Override
	public boolean isProtectUsers();

	/**
	 * Indicates whether a form is a subform.
	 *
	 * @return true if the form is a subform. false if the form is not a subform
	 */
	@Override
	public boolean isSubForm();

	/**
	 * Locks a form for the effective user.
	 * <p>
	 * {@link Database#isDesignLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * This method:
	 * </p>
	 * <ul>
	 * <li>Places a persistent lock if the administration (master lock) server is available.</li>
	 * <li>Throws an exception if the administration server is not available</li>
	 * </ul>
	 * <p>
	 * The following actions occur depending on the current lock status:
	 * </p>
	 * <ul>
	 * <li>If the form is not locked, this method places the lock and returns true.</li>
	 * <li>If the form is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the form is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * <p>
	 * If the form is modified by another user before the lock can be placed, this method throws an exception.
	 * </p>
	 *
	 * @return true if the lock is placed, false if the lock is not placed
	 */
	@Override
	public boolean lock();

	/**
	 * Locks a form for the effective user.
	 * <p>
	 * {@link Database#isDesignLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * This method:
	 * </p>
	 * <ul>
	 * <li>Places a persistent lock if the administration (master lock) server is available.</li>
	 * <li>Places a provisional lock if the administration server is not available and the parameter is true.</li>
	 * <li>Throws an exception if the administration server is not available and the parameter is false.</li>
	 * </ul>
	 * <p>
	 * The following actions occur depending on the current lock status:
	 * </p>
	 * <ul>
	 * <li>If the form is not locked, this method places the lock and returns true.</li>
	 * <li>If the form is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the form is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * <p>
	 * If the form is modified by another user before the lock can be placed, this method throws an exception.
	 * </p>
	 *
	 *
	 * @param provisionalok
	 *            true to permit the placement of a provisional lock or false to not permit a provisional lock
	 *
	 * @return true if the lock is placed, false if the lock is not placed
	 */
	@Override
	public boolean lock(final boolean provisionalOk);

	/**
	 * Locks a form for the given user name.
	 * <p>
	 * {@link Database#isDesignLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * This method:
	 * </p>
	 * <ul>
	 * <li>Places a persistent lock if the administration (master lock) server is available.</li>
	 * <li>Throws an exception if the administration server is not available.</li>
	 * </ul>
	 * <p>
	 * The following actions occur depending on the current lock status:
	 * </p>
	 * <ul>
	 * <li>If the form is not locked, this method places the lock and returns true.</li>
	 * <li>If the form is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the form is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * <p>
	 * If the form is modified by another user before the lock can be placed, this method throws an exception.
	 * </p>
	 *
	 *
	 * @param name
	 *            The name of the lock holder. Must be a user or group. The empty string ("") is not permitted.
	 *
	 * @return true if the lock is placed, false if the lock is not placed
	 */
	@Override
	public boolean lock(final String name);

	/**
	 * Locks a form for the given user name.
	 * <p>
	 * {@link Database#isDesignLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * This method:
	 * </p>
	 * <ul>
	 * <li>Places a persistent lock if the administration (master lock) server is available.</li>
	 * <li>Places a provisional lock if the administration server is not available and the second parameter is true.</li>
	 * <li>Throws an exception if the administration server is not available and the second parameter is false.</li>
	 * </ul>
	 * <p>
	 * The following actions occur depending on the current lock status:
	 * </p>
	 * <ul>
	 * <li>If the form is not locked, this method places the lock and returns true.</li>
	 * <li>If the form is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the form is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * <p>
	 * If the form is modified by another user before the lock can be placed, this method throws an exception.
	 * </p>
	 *
	 *
	 * @param name
	 *            The name of the lock holder. Must be a user or group. The empty string ("") is not permitted.
	 * @param provisionalok
	 *            true to permit the placement of a provisional lock false to not permit a provisional lock
	 *
	 * @return true if the lock is placed, false if the lock is not placed
	 */
	@Override
	public boolean lock(final String name, final boolean provisionalOk);

	/**
	 * Locks a form for the given users or groups.
	 * <p>
	 * {@link Database#isDesignLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * This method:
	 * </p>
	 * <ul>
	 * <li>Places a persistent lock if the administration (master lock) server is available.</li>
	 * <li>Throws an exception if the administration server is not available.</li>
	 * </ul>
	 * <p>
	 * The following actions occur depending on the current lock status:
	 * </p>
	 * <ul>
	 * <li>If the form is not locked, this method places the lock and returns true.</li>
	 * <li>If the form is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the form is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * <p>
	 * If the form is modified by another user before the lock can be placed, this method throws an exception.
	 * </p>
	 *
	 *
	 * @param name
	 *            The names of the lock holders. Each lock holder must be a user or group. The empty string ("") is not permitted.
	 *
	 * @return true if the lock is placed, false if the lock is not placed
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean lock(final Vector names);

	/**
	 * Locks a form for the given users or groups.
	 * <p>
	 * {@link Database#isDesignLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * This method:
	 * </p>
	 * <ul>
	 * <li>Places a persistent lock if the administration (master lock) server is available.</li>
	 * <li>Places a provisional lock if the administration server is not available and the second parameter is true.</li>
	 * <li>Throws an exception if the administration server is not available and the second parameter is false.</li>
	 * </ul>
	 * <p>
	 * The following actions occur depending on the current lock status:
	 * </p>
	 * <ul>
	 * <li>If the form is not locked, this method places the lock and returns true.</li>
	 * <li>If the form is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the form is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * <p>
	 * If the form is modified by another user before the lock can be placed, this method throws an exception.
	 * </p>
	 *
	 *
	 * @param names
	 *            The names of the lock holders. Each lock holder must be a user or group. The empty string ("") is not permitted.
	 * @param provisionalok
	 *            true to permit the placement of a provisional lock false to not permit a provisional lock
	 *
	 * @return true if the lock is placed, false if the lock is not placed
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean lock(final Vector names, final boolean provisionalOk);

	/**
	 * Locks a form provisionally for the effective user name.
	 * <p>
	 * {@link Database#isDesignLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * The following actions occur depending on the current lock status:
	 * </p>
	 * <ul>
	 * <li>If the form is not locked, this method places the lock and returns true.</li>
	 * <li>If the form is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the form is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * <p>
	 * If the form is modified by another user before the lock can be placed, this method throws an exception.
	 * </p>
	 *
	 *
	 * @return true if the lock is placed, false if the lock is not placed
	 */
	@Override
	public boolean lockProvisional();

	/**
	 * Locks a form provisionally for the effective user name.
	 * <p>
	 * {@link Database#isDesignLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * The following actions occur depending on the current lock status:
	 * </p>
	 * <ul>
	 * <li>If the form is not locked, this method places the lock and returns true.</li>
	 * <li>If the form is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the form is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * <p>
	 * If the form is modified by another user before the lock can be placed, this method throws an exception.
	 * </p>
	 *
	 * @param name
	 *            The name of the lock holder. Must be a user or group. The empty string ("") is not permitted.
	 * @return true if the lock is placed, false if the lock is not placed
	 */
	@Override
	public boolean lockProvisional(final String name);

	/**
	 * Locks a form provisionally for the effective user name.
	 * <p>
	 * {@link Database#isDesignLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * The following actions occur depending on the current lock status:
	 * </p>
	 * <ul>
	 * <li>If the form is not locked, this method places the lock and returns true.</li>
	 * <li>If the form is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the form is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * <p>
	 * If the form is modified by another user before the lock can be placed, this method throws an exception.
	 * </p>
	 *
	 * @param name
	 *            The names of the lock holders. Each lock holder must be a user or group. The empty string ("") is not permitted.
	 * @return true if the lock is placed, false if the lock is not placed
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean lockProvisional(final Vector names);

	/**
	 * Permanently deletes a form from a database.
	 */
	@Override
	public void remove();

	/**
	 * Sets who can create documents with this form.
	 *
	 * @param names
	 *            Names of users or groups who can create documents based on this form
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void setFormUsers(final Vector names);

	/**
	 * Protects $Readers items from being overwritten by replication.
	 *
	 *
	 * @param flag
	 *            true to protect $Readers items from being overwritten by replication.
	 */
	@Override
	public void setProtectReaders(final boolean flag);

	/**
	 * Protects $FormUsers items from being overwritten by replication.
	 *
	 * @param flag
	 *            true to protect $FormUsers items from being overwritten by replication.
	 */
	@Override
	public void setProtectUsers(final boolean flag);

	/**
	 * Sets the default read access for documents created with this form
	 *
	 * @param names
	 *            names, groups or roles who will have read access to documents
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void setReaders(final Vector names);

	/**
	 * Unlocks a form.
	 * <p>
	 * {@link Database#isDesignLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * This method throws an exception if the current user is not one of the lock holders and does not have lock breaking authority.
	 * </p>
	 *
	 */
	@Override
	public void unlock();
}
