/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openntf.domino.NoteCollection;
import org.openntf.domino.utils.DominoUtils;

/**
 * The Class DocumentIterator.
 */
@SuppressWarnings("nls")
public class NoteIterator implements Iterator<String> {
	/** The Constant log_. */
	private static final Logger log_ = Logger.getLogger(NoteIterator.class.getName());

	/** The index_. */
	private int index_ = 0;

	/** The id array_. */
	private int[] idArray_;

	@SuppressWarnings("unused")
	private NoteCollection collection_;

	/**
	 * Instantiates a new document iterator.
	 * 
	 * @param collection
	 *            the collection
	 */
	public NoteIterator(final NoteCollection collection) {
		collection_ = collection;
		setIdArray(getCollectionIds(collection));
	}

	/**
	 * Gets the collection ids.
	 * 
	 * @param nc
	 *            the nc
	 * @return the collection ids
	 */
	protected int[] getCollectionIds(final NoteCollection nc) {
		int[] result = null;
		if (nc != null) {
			try {
				if (nc.getCount() > 0) {
					result = nc.getNoteIDs();
				} else {
					if (log_.isLoggable(Level.FINER)) {
						log_.log(Level.FINER, "Attempted to get id array of empty NoteCollection");
					}
				}
			} catch (Throwable t) {
				DominoUtils.handleException(t);
			}
		} else {
			if (log_.isLoggable(Level.WARNING)) {
				log_.log(Level.WARNING, "Attempted to iterate over null NoteCollection");
			}
		}
		return result;
	}

	/**
	 * Gets the id array.
	 * 
	 * @return the id array
	 */
	protected int[] getIdArray() {
		return idArray_;
	}

	/**
	 * Gets the index.
	 * 
	 * @return the index
	 */
	protected int getIndex() {
		return index_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		if (getIdArray() == null) {
			// Most commonly if no match found
			return false;
		}
		return !((getIndex() + 1) > getIdArray().length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#next()
	 */
	@Override
	public String next() {
		String result = null;
		if (hasNext()) {
			result = Integer.toHexString(getIdArray()[getIndex()]);
			setIndex(getIndex() + 1);
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
	 * Sets the id array.
	 * 
	 * @param idArray
	 *            the new id array
	 */
	protected void setIdArray(final int[] idArray) {
		idArray_ = idArray;
	}

	/**
	 * Sets the index.
	 * 
	 * @param index
	 *            the new index
	 */
	protected void setIndex(final int index) {
		index_ = index;
	}
}
