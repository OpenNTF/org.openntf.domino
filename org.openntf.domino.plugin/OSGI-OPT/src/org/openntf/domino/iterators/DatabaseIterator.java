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
package org.openntf.domino.iterators;

import java.util.Iterator;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.DbDirectory;
import org.openntf.domino.utils.DominoUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class AclIterator.
 */
@Deprecated
public class DatabaseIterator implements Iterator<Database> {

	/** The Constant log_. */
	private static final Logger log_ = Logger.getLogger(DatabaseIterator.class.getName());

	/** The DbDirectory. */
	private DbDirectory dbDirectory;

	/** The current database. */
	private transient Database currentDatabase_;

	/** The next database. */
	private transient Database nextDatabase_;

	/** The type_. */
	private final DbDirectory.Type type_;

	/** The started_. */
	private boolean started_;

	/** The done_. */
	private boolean done_;

	/**
	 * Instantiates a new database iterator.
	 * 
	 * @param dbDirectory
	 *            the dbDirectory
	 * @param type
	 *            the type
	 */
	public DatabaseIterator(final DbDirectory dbDirectory, final DbDirectory.Type type) {
		setDbDirectory(dbDirectory);
		type_ = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		boolean result = false;
		try {
			nextDatabase_ = ((currentDatabase_ == null) ? (isDone() ? null : getDbDirectory().getFirstDatabase(type_)) : getDbDirectory()
					.getNextDatabase());
			result = (nextDatabase_ != null);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#next()
	 */
	@Override
	public Database next() {
		// FIXME NTF - I am almost certain this is wrong. I think there's a bit more checking involved.
		// The basic problem is that .hasNext() needs to move the cursor forward in order to answer the question. Once it's been moved
		// then we need to not move it again on a .next(). However, if .hasNext() isn't called, and .next() needs to just move the cursor,
		// then it should. Therefore while .hasNext() should move forward along the directory, it needs to track the result that .next()
		// should return, and then .next() should just return that if it's already set.
		Database result = null;
		try {
			if (nextDatabase_ == null) {
				result = ((currentDatabase_ == null) ? getDbDirectory().getFirstDatabase(type_) : getDbDirectory().getNextDatabase());
			} else {
				result = nextDatabase_;
				nextDatabase_ = null;
			}

			if (result == null) {
				setDone(true);
			} else {
				setStarted(true);
			}
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		} finally {
			setCurrentDatabase(result);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public void remove() {
		// NOOP
	}

	/**
	 * Gets the current entry.
	 * 
	 * @return the current entry
	 */
	public Database getCurrentDatabase() {
		return currentDatabase_;
	}

	/**
	 * Sets the current entry.
	 * 
	 * @param currentDatabase
	 *            the new current database
	 */
	public void setCurrentDatabase(final Database currentDatabase) {
		currentDatabase_ = currentDatabase;
	}

	/**
	 * Gets the acl.
	 * 
	 * @return the acl
	 */
	public DbDirectory getDbDirectory() {
		return dbDirectory;
	}

	/**
	 * Sets the acl.
	 * 
	 * @param dbDirectory
	 *            the new db directory
	 */
	public void setDbDirectory(final DbDirectory dbDirectory) {
		this.dbDirectory = dbDirectory;
	}

	/**
	 * Checks if is done.
	 * 
	 * @return true, if is done
	 */
	public boolean isDone() {
		return done_;
	}

	/**
	 * Sets the done.
	 * 
	 * @param done
	 *            the new done
	 */
	public void setDone(final boolean done) {
		done_ = done;
	}

	/**
	 * Checks if is started.
	 * 
	 * @return true, if is started
	 */
	public boolean isStarted() {
		return started_;
	}

	/**
	 * Sets the started.
	 * 
	 * @param started
	 *            the new started
	 */
	public void setStarted(final boolean started) {
		started_ = started;
	}
}
