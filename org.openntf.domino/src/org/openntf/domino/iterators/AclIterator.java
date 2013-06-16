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

import org.openntf.domino.ACL;
import org.openntf.domino.ACLEntry;
import org.openntf.domino.utils.DominoUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class AclIterator.
 */
public class AclIterator implements Iterator<ACLEntry> {
	
	/** The Constant log_. */
	private static final Logger log_ = Logger.getLogger(AclIterator.class.getName());
	
	/** The acl. */
	private ACL acl;
	
	/** The current entry_. */
	private transient ACLEntry currentEntry_;
	
	/** The started_. */
	private boolean started_;
	
	/** The done_. */
	private boolean done_;

	/**
	 * Instantiates a new acl iterator.
	 * 
	 * @param acl
	 *            the acl
	 */
	public AclIterator(final ACL acl) {
		setAcl(acl);
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		boolean result = false;
		ACLEntry currentEntry = getCurrentEntry();
		ACLEntry nextEntry = null;
		try {
			nextEntry = ((currentEntry == null) ? (isDone() ? null : getAcl().getFirstEntry()) : getAcl().getNextEntry(currentEntry));
			result = (nextEntry != null);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		} finally {
			DominoUtils.incinerate(nextEntry);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	@Override
	public ACLEntry next() {
		ACLEntry result = null;
		ACLEntry currentEntry = getCurrentEntry();
		try {
			result = ((currentEntry == null) ? getAcl().getFirstEntry() : getAcl().getNextEntry(currentEntry));
			if (result == null) {
				setDone(true);
			} else {
				setStarted(true);
			}
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		} finally {
			DominoUtils.incinerate(currentEntry);
			setCurrentEntry(result);
		}
		return result;
	}

	/* (non-Javadoc)
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
	public ACLEntry getCurrentEntry() {
		return currentEntry_;
	}

	/**
	 * Sets the current entry.
	 * 
	 * @param currentEntry
	 *            the new current entry
	 */
	public void setCurrentEntry(final ACLEntry currentEntry) {
		currentEntry_ = currentEntry;
	}

	/**
	 * Gets the acl.
	 * 
	 * @return the acl
	 */
	public ACL getAcl() {
		return acl;
	}

	/**
	 * Sets the acl.
	 * 
	 * @param acl
	 *            the new acl
	 */
	public void setAcl(final ACL acl) {
		this.acl = acl;
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
