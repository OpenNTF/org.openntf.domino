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

import java.util.Iterator;

import lotus.domino.NotesException;

import org.openntf.domino.Base;
import org.openntf.domino.Database;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Session;
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
public abstract class AbstractDominoIterator<T> implements Iterator<T> {
	
	/** The server name_. */
	private String serverName_;
	
	/** The file path_. */
	private String filePath_;
	
	/** The collection_. */
	private Base collection_;
	
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
	protected AbstractDominoIterator(Base collection) {
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
	public Base getCollection() {
		return collection_;
	}

	/**
	 * Sets the collection.
	 * 
	 * @param collection
	 *            the new collection
	 */
	public void setCollection(Base collection) {
		if (collection != null) {
			if (collection instanceof DocumentCollection) {
				org.openntf.domino.Database parent = ((org.openntf.domino.DocumentCollection) collection).getParent();
				database_ = Factory.fromLotus(parent, Database.class, parent);
				session_ = Factory.fromLotus(database_.getParent(), Session.class, parent.getParent()); // FIXME NTF - this is suboptimal,
																										// but we still need to
				// sort out the parent/child pattern
			} else if (collection instanceof ViewEntryCollection) {
				lotus.domino.View vw = ((ViewEntryCollection) collection).getParent();
				try {
					database_ = Factory.fromLotus(vw.getParent(), org.openntf.domino.Database.class, null); // FIXME NTF -- just trying to
																											// compile!!!
					session_ = Factory.fromLotus(database_.getParent(), Session.class, database_.getParent()); // FIXME NTF - this is
																												// suboptimal, but we still
																												// need
					// to sort out the parent/child pattern
				} catch (NotesException e) {
					DominoUtils.handleException(e);
				}
			}
			if (database_ != null) {
				setDatabase(database_);
			}
		}
		collection_ = collection;
	}
}
