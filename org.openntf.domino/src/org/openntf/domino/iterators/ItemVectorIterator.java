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

import org.openntf.domino.Document;
import org.openntf.domino.Item;
import org.openntf.domino.impl.ItemVector;

// TODO: Auto-generated Javadoc
/**
 * The Class VectorIterator.
 * 
 */
public class ItemVectorIterator extends AbstractDominoListIterator<Item> {

	/** The Constant log_. */
	private static final Logger log_ = Logger.getLogger(ItemVectorIterator.class.getName());
	/** The index_. */
	private int index_ = 0;

	/** The id array_. */
	private String[] names_;

	/** The current_. */
	private transient Item current_;

	/**
	 * Instantiates a new document iterator.
	 * 
	 * @param document
	 *            the document
	 * @param itemVector
	 *            the itemVector
	 */
	public ItemVectorIterator(final Document document, final ItemVector itemVector) {
		super(document);
		setNames(itemVector.getNames());
	}

	/**
	 * Gets the id array.
	 * 
	 * @return the id array
	 */
	protected String[] getNames() {
		return names_;
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
		return !((getIndex() + 1) > getNames().length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#next()
	 */
	@Override
	public Item next() {
		Item result = null;
		if (hasNext()) {
			String name = names_[getIndex()];
			setIndex(getIndex() + 1);
			current_ = ((Document) getCollection()).getFirstItem(name);
			result = current_;

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
	protected void setNames(final String[] names) {
		names_ = names;
		if (log_.isLoggable(Level.FINE)) {
			log_.log(Level.FINE, "Created an iterator from " + names.length + " itemnames.");
		}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.iterators.AbstractDominoListIterator#hasPrevious()
	 */
	@Override
	public boolean hasPrevious() {
		return !((getIndex() - 1) < getNames().length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.iterators.AbstractDominoListIterator#nextIndex()
	 */
	@Override
	public int nextIndex() {
		return getIndex() + 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.iterators.AbstractDominoListIterator#previous()
	 */
	@Override
	public Item previous() {
		Item result = null;
		if (hasPrevious()) {
			int pi = previousIndex();
			String name = names_[pi];
			setIndex(pi);
			current_ = ((Document) getCollection()).getFirstItem(name);
			result = current_;

		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.iterators.AbstractDominoListIterator#previousIndex()
	 */
	@Override
	public int previousIndex() {
		return getIndex() - 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.iterators.AbstractDominoListIterator#set(java.lang.Object)
	 */
	@Override
	public void set(final Item arg0) {
		throw new UnsupportedOperationException();
	}

}
