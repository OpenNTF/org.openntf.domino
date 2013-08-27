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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.impl.Base;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class DocumentIterator.
 */
public class DocumentIterator extends AbstractDominoIterator<org.openntf.domino.Document> {
	/** The Constant log_. */
	private static final Logger log_ = Logger.getLogger(DocumentIterator.class.getName());
	/** The index_. */
	private int index_ = 0;

	/** The id array_. */
	private final int[] idArray_;

	// /** The current_. */
	// private transient Document current_;

	/**
	 * Instantiates a new document iterator.
	 * 
	 * @param collection
	 *            the collection
	 */
	public DocumentIterator(final DocumentCollection collection) {
		super(collection);
		idArray_ = getCollectionIds(collection);
	}

	/**
	 * Gets the collection ids.
	 * 
	 * @param collection
	 *            the collection
	 * @return the collection ids
	 */
	protected int[] getCollectionIds(final DocumentCollection collection) {
		int[] result = null;
		if (collection != null) {
			NoteCollection nc = null;
			try {
				Database db = collection.getParent();
				setDatabase(db);
				nc = org.openntf.domino.impl.DocumentCollection.toLotusNoteCollection(collection);
				if (nc.getCount() > 0) {
					result = nc.getNoteIDs();
				} else {
					if (log_.isLoggable(Level.FINER)) {
						log_.log(Level.FINER, "Attempted to get id array of empty DocumentCollection");
					}
				}
			} catch (Throwable t) {
				DominoUtils.handleException(t);
			} finally {
				Base.s_recycle(nc);
			}
		} else {
			if (log_.isLoggable(Level.WARNING)) {
				log_.log(Level.WARNING, "Attempted to iterate over null DocumentCollection");
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
	public Document next() {
		Document result = null;
		if (hasNext()) {
			String noteId = Integer.toHexString(getIdArray()[getIndex()]);
			setIndex(getIndex() + 1);
			// Base.recycle(current_);
			try {
				Database db = getDatabase();
				lotus.domino.Document doc = db.getDocumentByID(noteId);
				if (doc instanceof org.openntf.domino.Document) {
					result = (org.openntf.domino.Document) doc;
				} else {
					result = Factory.fromLotus(doc, Document.class, db);
				}
				// current_ = result;
			} catch (Throwable t) {
				DominoUtils.handleException(t);
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		// NOOP
	}

	// /**
	// * Sets the id array.
	// *
	// * @param idArray
	// * the new id array
	// */
	// protected void setIdArray(int[] idArray) {
	// idArray_ = idArray;
	// if (log_.isLoggable(Level.FINE)) {
	// log_.log(Level.FINE, "Created an iterator from " + idArray.length + " noteids.");
	// }
	// }

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
