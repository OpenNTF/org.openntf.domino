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
 * The Interface Form.
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Form#getAliases()
	 */
	@Override
	public Vector<String> getAliases();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Form#getFields()
	 */
	@Override
	public Vector<String> getFields();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Form#getFieldType(java.lang.String)
	 */
	@Override
	public int getFieldType(final String name);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Form#getFormUsers()
	 */
	@Override
	public Vector<String> getFormUsers();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Form#getHttpURL()
	 */
	@Override
	public String getHttpURL();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Form#getLockHolders()
	 */
	@Override
	public Vector<String> getLockHolders();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Form#getName()
	 */
	@Override
	public String getName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Form#getNotesURL()
	 */
	@Override
	public String getNotesURL();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Form#getParent()
	 */
	@Override
	public Database getParent();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Form#getReaders()
	 */
	@Override
	public Vector<String> getReaders();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Form#getURL()
	 */
	@Override
	public String getURL();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Form#isProtectReaders()
	 */
	@Override
	public boolean isProtectReaders();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Form#isProtectUsers()
	 */
	@Override
	public boolean isProtectUsers();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Form#isSubForm()
	 */
	@Override
	public boolean isSubForm();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Form#lock()
	 */
	@Override
	public boolean lock();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Form#lock(boolean)
	 */
	@Override
	public boolean lock(final boolean provisionalOk);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Form#lock(java.lang.String)
	 */
	@Override
	public boolean lock(final String name);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Form#lock(java.lang.String, boolean)
	 */
	@Override
	public boolean lock(final String name, final boolean provisionalOk);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Form#lock(java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean lock(final Vector names);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Form#lock(java.util.Vector, boolean)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean lock(final Vector names, final boolean provisionalOk);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Form#lockProvisional()
	 */
	@Override
	public boolean lockProvisional();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Form#lockProvisional(java.lang.String)
	 */
	@Override
	public boolean lockProvisional(final String name);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Form#lockProvisional(java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean lockProvisional(final Vector names);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Form#remove()
	 */
	@Override
	public void remove();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Form#setFormUsers(java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void setFormUsers(final Vector names);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Form#setProtectReaders(boolean)
	 */
	@Override
	public void setProtectReaders(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Form#setProtectUsers(boolean)
	 */
	@Override
	public void setProtectUsers(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Form#setReaders(java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void setReaders(final Vector names);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Form#unlock()
	 */
	@Override
	public void unlock();
}
