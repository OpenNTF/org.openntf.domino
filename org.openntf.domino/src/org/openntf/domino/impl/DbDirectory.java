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
package org.openntf.domino.impl;

import java.util.Collection;

import lotus.domino.NotesException;

import org.openntf.domino.Database;
import org.openntf.domino.collections.DatabaseCollection;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class DbDirectory.
 */
public class DbDirectory extends Base<org.openntf.domino.DbDirectory, lotus.domino.DbDirectory> implements org.openntf.domino.DbDirectory {

	/**
	 * Instantiates a new db directory.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	public DbDirectory(lotus.domino.DbDirectory delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DbDirectory#createDatabase(java.lang.String)
	 */
	@Override
	public Database createDatabase(String dbFile) {
		try {
			return Factory.fromLotus(getDelegate().createDatabase(dbFile), Database.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DbDirectory#createDatabase(java.lang.String, boolean)
	 */
	@Override
	public Database createDatabase(String dbFile, boolean open) {
		try {
			return Factory.fromLotus(getDelegate().createDatabase(dbFile, open), Database.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DbDirectory#getClusterName()
	 */
	@Override
	public String getClusterName() {
		try {
			return getDelegate().getClusterName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DbDirectory#getClusterName(java.lang.String)
	 */
	@Override
	public String getClusterName(String server) {
		try {
			return getDelegate().getClusterName(server);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DbDirectory#getFirstDatabase(int)
	 */
	@Override
	public Database getFirstDatabase(int type) {
		try {
			return Factory.fromLotus(getDelegate().getFirstDatabase(type), Database.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DbDirectory#getName()
	 */
	@Override
	public String getName() {
		try {
			return getDelegate().getName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DbDirectory#getNextDatabase()
	 */
	@Override
	public Database getNextDatabase() {
		try {
			return Factory.fromLotus(getDelegate().getNextDatabase(), Database.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public org.openntf.domino.Session getParent() {
		return (org.openntf.domino.Session) super.getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DbDirectory#getTemplates()
	 */
	@Override
	public Collection<Database> getTemplates() {
		Collection<Database> result = new DatabaseCollection();
		Database template = getFirstDatabase(TEMPLATE);
		while (template != null) {
			result.add(template);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DbDirectory#isHonorShowInOpenDatabaseDialog()
	 */
	@Override
	public boolean isHonorShowInOpenDatabaseDialog() {
		try {
			return getDelegate().isHonorShowInOpenDatabaseDialog();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DbDirectory#openDatabase(java.lang.String)
	 */
	@Override
	public Database openDatabase(String dbFile) {
		try {
			return Factory.fromLotus(getDelegate().openDatabase(dbFile), Database.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DbDirectory#openDatabase(java.lang.String, boolean)
	 */
	@Override
	public Database openDatabase(String dbFile, boolean failover) {
		try {
			return Factory.fromLotus(getDelegate().openDatabase(dbFile, failover), Database.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DbDirectory#openDatabaseByReplicaID(java.lang.String)
	 */
	@Override
	public Database openDatabaseByReplicaID(String replicaId) {
		try {
			return Factory.fromLotus(getDelegate().openDatabaseByReplicaID(replicaId), Database.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DbDirectory#openDatabaseIfModified(java.lang.String, lotus.domino.DateTime)
	 */
	@Override
	public Database openDatabaseIfModified(String dbFile, lotus.domino.DateTime date) {
		try {
			return Factory.fromLotus(getDelegate().openDatabaseIfModified(dbFile, date), Database.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DbDirectory#openMailDatabase()
	 */
	@Override
	public Database openMailDatabase() {
		try {
			return Factory.fromLotus(getDelegate().openMailDatabase(), Database.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DbDirectory#setHonorShowInOpenDatabaseDialog(boolean)
	 */
	@Override
	public void setHonorShowInOpenDatabaseDialog(boolean flag) {
		try {
			getDelegate().setHonorShowInOpenDatabaseDialog(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

}
