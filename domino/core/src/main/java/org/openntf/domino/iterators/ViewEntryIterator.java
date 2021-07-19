/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
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
package org.openntf.domino.iterators;

import java.util.Iterator;

import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewEntryCollection;
import org.openntf.domino.utils.DominoUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class ViewEntryIterator.
 */
public class ViewEntryIterator implements Iterator<ViewEntry> {

	/** The current entry_. */
	private transient ViewEntry currentEntry_;

	/** The started_. */
	private boolean started_;

	/** The done_. */
	private boolean done_;

	/** The count_. */
	private int count_ = 0;

	/** The current index_. */
	private int currentIndex_ = 0;

	private ViewEntryCollection collection_;

	/**
	 * Instantiates a new view entry iterator.
	 * 
	 * @param collection
	 *            the collection
	 */
	public ViewEntryIterator(final ViewEntryCollection collection) {
		collection_ = collection;

		// TODO replace this with a less-expensive operation
		count_ = collection.getCount();
	}

	//	/*
	//	 * (non-Javadoc)
	//	 * 
	//	 * @see org.openntf.domino.iterators.AbstractDominoIterator#getCollection()
	//	 */
	//	@Override
	//	public ViewEntryCollection getCollection() {
	//		ViewEntryCollection result = null;
	//		Base<?> collection = super.getCollection();
	//		if (collection instanceof ViewEntryCollection) {
	//			result = (ViewEntryCollection) collection;
	//		}
	//		return result;
	//	}

	/**
	 * Gets the current entry.
	 * 
	 * @return the current entry
	 */
	public ViewEntry getCurrentEntry() {
		return currentEntry_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		return currentIndex_ < count_;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#next()
	 */
	@Override
	@SuppressWarnings("deprecation")
	public ViewEntry next() {
		ViewEntry result = null;
		ViewEntry currentEntry = getCurrentEntry();
		try {
			result = ((currentEntry == null) ? collection_.getFirstEntry() : collection_.getNextEntry(currentEntry));
			currentIndex_++;
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		} finally {
			setCurrentEntry(result);
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
	 * Sets the current entry.
	 * 
	 * @param currentEntry
	 *            the new current entry
	 */
	public void setCurrentEntry(final ViewEntry currentEntry) {
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
	public void setDone(final boolean done) {
		done_ = done;
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
