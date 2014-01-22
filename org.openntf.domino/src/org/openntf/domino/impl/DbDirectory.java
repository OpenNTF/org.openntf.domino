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
package org.openntf.domino.impl;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.annotations.Legacy;
import org.openntf.domino.types.Encapsulated;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class DbDirectory.
 */
public class DbDirectory extends Base<org.openntf.domino.DbDirectory, lotus.domino.DbDirectory, Session> implements
		org.openntf.domino.DbDirectory, Encapsulated {
	private static final Logger log_ = Logger.getLogger(DbDirectory.class.getName());

	private SortedSet<org.openntf.domino.Database> dbSet_;
	private org.openntf.domino.DbDirectory.Type type_;
	private boolean isDateSorted_ = false;
	private String name_;
	private String clusterName_;
	private boolean isInitialized_ = false;
	private boolean isHonorOpenDialog_ = false;

	/**
	 * Instantiates a new DbDirectory.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 * @param wf
	 *            the wrapperfactory
	 * @param cppId
	 *            the cpp-id
	 */
	public DbDirectory(final lotus.domino.DbDirectory delegate, final Session parent, final WrapperFactory wf, final long cppId) {
		super(delegate, parent, wf, cppId, NOTES_SERVER);
		try {
			name_ = delegate.getName();
			clusterName_ = delegate.getClusterName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		dbSet_ = new ConcurrentSkipListSet<Database>(Database.FILEPATH_COMPARATOR);
		type_ = Type.TEMPLATE_CANDIDATE;
	}

	/**
	 * Instantiates a new db directory.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	@Deprecated
	public DbDirectory(final lotus.domino.DbDirectory delegate, final Session parent) {
		super(delegate, parent);
		try {
			name_ = delegate.getName();
			clusterName_ = delegate.getClusterName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		dbSet_ = new ConcurrentSkipListSet<Database>(Database.FILEPATH_COMPARATOR);
		type_ = Type.TEMPLATE_CANDIDATE;
	}

	@Deprecated
	public DbDirectory(final lotus.domino.DbDirectory delegate, final Session parent, final Type type) {
		super(delegate, parent);
		try {
			name_ = delegate.getName();
			clusterName_ = delegate.getClusterName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		dbSet_ = new ConcurrentSkipListSet<Database>(Database.FILEPATH_COMPARATOR);
		type_ = type;
	}

	@Deprecated
	public DbDirectory(final lotus.domino.DbDirectory delegate, final Session parent, final boolean sortByLastModified) {
		super(delegate, parent);
		try {
			name_ = delegate.getName();
			clusterName_ = delegate.getClusterName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		if (sortByLastModified) {
			dbSet_ = new ConcurrentSkipListSet<Database>(Database.LASTMOD_COMPARATOR);
			isDateSorted_ = true;
		} else {
			dbSet_ = new ConcurrentSkipListSet<Database>(Database.FILEPATH_COMPARATOR);
		}
		type_ = Type.TEMPLATE_CANDIDATE;
	}

	@Deprecated
	public DbDirectory(final lotus.domino.DbDirectory delegate, final Session parent, final Type type, final boolean sortByLastModified) {
		super(delegate, parent);
		try {
			name_ = delegate.getName();
			clusterName_ = delegate.getClusterName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		isDateSorted_ = sortByLastModified;
		type_ = type;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.impl.Base#findParent(lotus.domino.Base)
	 */
	@Override
	protected Session findParent(final lotus.domino.DbDirectory delegate) throws NotesException {
		return fromLotus(delegate.getParent(), Session.SCHEMA, null);
	}

	public boolean isSortByLastModified() {
		return isDateSorted_;
	}

	public void setSortByLastModified(final boolean value) {
		if (isDateSorted_ != value) {
			isDateSorted_ = value;
			isInitialized_ = false;
		}
	}

	public Type getDirectoryType() {
		return type_;
	}

	public void setDirectoryType(final Type type) {
		if (type_ != type) {
			type_ = type;
			isInitialized_ = false;
		}
	}

	private void initialize(final lotus.domino.DbDirectory delegate) {
		if (isDateSorted_) {
			dbSet_ = new ConcurrentSkipListSet<Database>(Database.LASTMOD_COMPARATOR);
		} else {
			dbSet_ = new ConcurrentSkipListSet<Database>(Database.FILEPATH_COMPARATOR);
		}
		boolean isExtended = type_ == Type.REPLICA_CANDIDATE || isDateSorted_;
		try {
			delegate.setHonorShowInOpenDatabaseDialog(isHonorOpenDialog_);
			lotus.domino.Database rawdb = delegate.getFirstDatabase(type_.getValue());
			lotus.domino.Database nextdb;
			Database db;
			while (rawdb != null) {
				nextdb = delegate.getNextDatabase();
				// TODO RPr: Should we do that with factory
				db = new org.openntf.domino.impl.Database(rawdb, this, isExtended);
				if (type_ == Type.REPLICA_CANDIDATE) {
					if (org.openntf.domino.Database.Utils.isReplicaCandidate(db))
						dbSet_.add(db);
				} else if (type_ == Type.TEMPLATE) {
					if (org.openntf.domino.Database.Utils.isTemplate(db))
						dbSet_.add(db);
				} else if (type_ == Type.DATABASE) {
					if (org.openntf.domino.Database.Utils.isDatabase(db))
						dbSet_.add(db);
				} else {
					if (org.openntf.domino.Database.Utils.isTemplateCandidate(db))
						dbSet_.add(db);
				}
				rawdb = nextdb;
			}
			Base.s_recycle(delegate);
		} catch (NotesException ne) {
			ne.printStackTrace();
		}
		isInitialized_ = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DbDirectory#createDatabase(java.lang.String)
	 */
	@Override
	public Database createDatabase(final String dbFile) {
		try {
			return fromLotus(getDelegate().createDatabase(dbFile), Database.SCHEMA, getAncestorSession());
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
	public Database createDatabase(final String dbFile, final boolean open) {
		try {
			return fromLotus(getDelegate().createDatabase(dbFile, open), Database.SCHEMA, getAncestorSession());
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
		return clusterName_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DbDirectory#getClusterName(java.lang.String)
	 */
	@Override
	public String getClusterName(final String server) {
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
	@Deprecated
	@Legacy(org.openntf.domino.annotations.Legacy.ITERATION_WARNING)
	public Database getFirstDatabase(final int type) {
		if (type_ == null || type != type_.getValue()) {
			type_ = Type.getType(type);
			isInitialized_ = false;
		}
		return getFirstDatabase();
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.DbDirectory#getFirstDatabase(org.openntf.domino.DbDirectory.Type)
	 */
	@Deprecated
	@Legacy(org.openntf.domino.annotations.Legacy.ITERATION_WARNING)
	public Database getFirstDatabase(final Type type) {
		if (type != type_) {
			type_ = type;
			isInitialized_ = false;
		}
		return getFirstDatabase();
	}

	@Deprecated
	@Legacy(org.openntf.domino.annotations.Legacy.ITERATION_WARNING)
	public Database getFirstDatabase() {
		if (!isInitialized_) {
			initialize(getDelegate());
		}
		return dbSet_.first();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DbDirectory#getName()
	 */
	@Override
	public String getName() {
		return name_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DbDirectory#getNextDatabase()
	 */
	@Override
	@Deprecated
	@Legacy(org.openntf.domino.annotations.Legacy.ITERATION_WARNING)
	public Database getNextDatabase() {
		try {
			return fromLotus(getDelegate().getNextDatabase(), Database.SCHEMA, getAncestorSession());
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
		return getAncestor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DbDirectory#isHonorShowInOpenDatabaseDialog()
	 */
	@Override
	public boolean isHonorShowInOpenDatabaseDialog() {
		return isHonorOpenDialog_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<org.openntf.domino.Database> iterator() {
		if (!isInitialized_) {
			initialize(getDelegate());
		}
		return dbSet_.iterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DbDirectory#openDatabase(java.lang.String)
	 */
	@Override
	public Database openDatabase(final String dbFile) {
		return getAncestorSession().getDatabase(getName(), dbFile);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DbDirectory#openDatabase(java.lang.String, boolean)
	 */
	@Override
	public Database openDatabase(final String dbFile, final boolean failover) {
		if (failover) {
			return getAncestorSession().getDatabaseWithFailover(getName(), dbFile);
		} else {
			return openDatabase(dbFile);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DbDirectory#openDatabaseByReplicaID(java.lang.String)
	 */
	@Override
	public Database openDatabaseByReplicaID(final String replicaId) {
		return getAncestorSession().getDatabaseByReplicaID(getName(), replicaId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DbDirectory#openDatabaseIfModified(java.lang.String, lotus.domino.DateTime)
	 */
	@Override
	public Database openDatabaseIfModified(final String dbFile, final lotus.domino.DateTime date) {
		return getAncestorSession().getDatabaseIfModified(getName(), dbFile, date);
	}

	public Database openDatabaseIfModified(final String dbFile, final Date date) {
		return getAncestorSession().getDatabaseIfModified(getName(), dbFile, date);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DbDirectory#openMailDatabase()
	 */
	@Override
	public Database openMailDatabase() {
		return getAncestorSession().getMailDatabase();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.DbDirectory#setHonorShowInOpenDatabaseDialog(boolean)
	 */
	@Override
	public void setHonorShowInOpenDatabaseDialog(final boolean flag) {
		isHonorOpenDialog_ = flag;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public org.openntf.domino.Session getAncestorSession() {
		return this.getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.impl.Base#getDelegate()
	 */
	@Override
	protected lotus.domino.DbDirectory getDelegate() {
		lotus.domino.DbDirectory dir = super.getDelegate();
		try {
			dir.isHonorShowInOpenDatabaseDialog();
		} catch (NotesException e) {
			resurrect();
		}
		return super.getDelegate();
	}

	void resurrect() {
		Session rawSessionUs = (Session) Factory.getSession();
		lotus.domino.Session rawSession = toLotus(rawSessionUs);
		try {
			lotus.domino.DbDirectory dir = rawSession.getDbDirectory(name_);
			dir.setHonorShowInOpenDatabaseDialog(isHonorOpenDialog_);
			setDelegate(dir, 0);
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
	public void readExternal(final ObjectInput arg0) throws IOException, ClassNotFoundException {
		isDateSorted_ = arg0.readBoolean();
		isInitialized_ = arg0.readBoolean();
		isHonorOpenDialog_ = arg0.readBoolean();
		type_ = Type.getType(arg0.readInt());
		name_ = arg0.readUTF();
		clusterName_ = arg0.readUTF();
		dbSet_ = (SortedSet) arg0.readObject();
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	public void writeExternal(final ObjectOutput arg0) throws IOException {
		arg0.writeBoolean(isDateSorted_);
		arg0.writeBoolean(isInitialized_);
		arg0.writeBoolean(isHonorOpenDialog_);
		arg0.writeInt(type_.getValue());
		arg0.writeUTF(name_);
		arg0.writeUTF(clusterName_);
		arg0.writeObject(dbSet_);
	}

}
