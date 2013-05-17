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
package org.openntf.domino.iterators;

import java.util.ListIterator;

import org.openntf.domino.Base;
import org.openntf.domino.Database;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntryCollection;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractDominoIterator.
 * 
 * @param <T>
 *            the generic type
 */
@SuppressWarnings({ "rawtypes" })
public abstract class AbstractDominoListIterator<T> implements ListIterator<T> {

	/** The server name_. */
	private String serverName_;

	/** The file path_. */
	private String filePath_;

	/** The collection_. */
	private Base<?> collection_;

	/** The session_. */
	private transient Session session_;

	/** The database_. */
	private transient Database database_;

	/**
	 * Instantiates a new abstract domino iterator.
	 * 
	 * @param collection
	 *            the collection
	 */
	protected AbstractDominoListIterator(Base<?> collection) {
		setCollection(collection);
	}

	/**
	 * Gets the session.
	 * 
	 * @return the session
	 */
	protected Session getSession() {
		if (session_ == null) {
			try {
				session_ = Factory.getSession();
			} catch (Throwable e) {
				DominoUtils.handleException(e);
				return null;
			}
		}
		return session_;
	}

	/**
	 * Gets the database.
	 * 
	 * @return the database
	 */
	protected Database getDatabase() {
		if (database_ == null) {
			Session session = getSession();
			try {
				database_ = session.getDatabase(getServerName(), getFilePath());
			} catch (Throwable e) {
				DominoUtils.handleException(e);
				return null;
			}
		}
		return database_;

	}

	/**
	 * Gets the file path.
	 * 
	 * @return the file path
	 */
	protected String getFilePath() {
		return filePath_;
	}

	/**
	 * Gets the server name.
	 * 
	 * @return the server name
	 */
	protected String getServerName() {
		return serverName_;
	}

	/**
	 * Sets the database.
	 * 
	 * @param database
	 *            the new database
	 */
	protected void setDatabase(Database database) {
		if (database != null) {
			try {
				setFilePath(database.getFilePath());
				setServerName(database.getServer());
			} catch (Throwable e) {
				DominoUtils.handleException(e);
			}
		}
	}

	/**
	 * Sets the file path.
	 * 
	 * @param filePath
	 *            the new file path
	 */
	protected void setFilePath(String filePath) {
		filePath_ = filePath;
	}

	/**
	 * Sets the server name.
	 * 
	 * @param serverName
	 *            the new server name
	 */
	protected void setServerName(String serverName) {
		serverName_ = serverName;
	}

	/**
	 * Gets the collection.
	 * 
	 * @return the collection
	 */
	public Base<?> getCollection() {
		return collection_;
	}

	/**
	 * Sets the collection.
	 * 
	 * @param collection
	 *            the new collection
	 */
	public void setCollection(Base<?> collection) {
		if (collection != null) {
			if (collection instanceof DocumentCollection) {
				org.openntf.domino.Database parent = ((org.openntf.domino.DocumentCollection) collection).getParent();
				database_ = Factory.fromLotus(parent, Database.class, parent);
				session_ = Factory.fromLotus(database_.getParent(), Session.class, parent.getParent()); // FIXME NTF - this is suboptimal,
				// but we still need to
				// sort out the parent/child pattern
			} else if (collection instanceof ViewEntryCollection) {
				View vw = ((ViewEntryCollection) collection).getParent();
				database_ = vw.getParent();
				session_ = Factory.getSession(database_);
			}
			if (database_ != null) {
				setDatabase(database_);
			}
		}
		collection_ = collection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ListIterator#add(java.lang.Object)
	 */
	@Override
	public void add(T arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ListIterator#hasNext()
	 */
	@Override
	public abstract boolean hasNext();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ListIterator#hasPrevious()
	 */
	@Override
	public abstract boolean hasPrevious();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ListIterator#next()
	 */
	@Override
	public abstract T next();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ListIterator#nextIndex()
	 */
	@Override
	public abstract int nextIndex();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ListIterator#previous()
	 */
	@Override
	public abstract T previous();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ListIterator#previousIndex()
	 */
	@Override
	public abstract int previousIndex();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ListIterator#remove()
	 */
	@Override
	public abstract void remove();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ListIterator#set(java.lang.Object)
	 */
	@Override
	public abstract void set(T arg0);
}
