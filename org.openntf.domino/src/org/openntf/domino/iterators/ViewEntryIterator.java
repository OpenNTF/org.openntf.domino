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

import lotus.domino.ViewEntry;

import org.openntf.domino.Base;
import org.openntf.domino.ViewEntryCollection;
import org.openntf.domino.utils.DominoUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class ViewEntryIterator.
 */
public class ViewEntryIterator extends AbstractDominoIterator<ViewEntry> {
	
	/** The current entry_. */
	private transient ViewEntry currentEntry_;
	
	/** The started_. */
	private boolean started_;
	
	/** The done_. */
	private boolean done_;

	/**
	 * Instantiates a new view entry iterator.
	 * 
	 * @param collection
	 *            the collection
	 */
	public ViewEntryIterator(ViewEntryCollection collection) {
		super(collection);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.iterators.AbstractDominoIterator#getCollection()
	 */
	@Override
	public ViewEntryCollection getCollection() {
		ViewEntryCollection result = null;
		Base<?> collection = super.getCollection();
		if (collection instanceof ViewEntryCollection) {
			result = (ViewEntryCollection) collection;
		}
		return result;
	}

	/**
	 * Gets the current entry.
	 * 
	 * @return the current entry
	 */
	public ViewEntry getCurrentEntry() {
		return currentEntry_;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		boolean result = false;
		ViewEntry currentEntry = getCurrentEntry();
		ViewEntry nextEntry = null;
		try {
			nextEntry = ((currentEntry == null) ? (isDone() ? null : getCollection().getFirstEntry()) : getCollection().getNextEntry(
					currentEntry));
			result = (nextEntry != null);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		} finally {
			DominoUtils.incinerate(nextEntry);
		}
		return result;
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
	 * Checks if is started.
	 * 
	 * @return true, if is started
	 */
	public boolean isStarted() {
		return started_;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	public ViewEntry next() {
		ViewEntry result = null;
		ViewEntry currentEntry = getCurrentEntry();
		try {
			result = ((currentEntry == null) ? getCollection().getFirstEntry() : getCollection().getNextEntry(currentEntry));
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
	public void remove() {
		// NOOP
	}

	/**
	 * Sets the current entry.
	 * 
	 * @param currentEntry
	 *            the new current entry
	 */
	public void setCurrentEntry(ViewEntry currentEntry) {
		currentEntry_ = currentEntry;
		setStarted(currentEntry != null);
		setDone(currentEntry == null);
	}

	/**
	 * Sets the done.
	 * 
	 * @param done
	 *            the new done
	 */
	public void setDone(boolean done) {
		done_ = done;
	}

	/**
	 * Sets the started.
	 * 
	 * @param started
	 *            the new started
	 */
	public void setStarted(boolean started) {
		started_ = started;
	}
}
