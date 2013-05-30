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

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import org.openntf.domino.impl.Vector;

// TODO: Auto-generated Javadoc
/**
 * The Class VectorIterator.
 * 
 * @param <T>
 *            the generic type
 */
public class VectorIterator<T> implements Iterator<T> {

	/** The Constant log_. */
	private static final Logger log_ = Logger.getLogger(VectorIterator.class.getName());

	/** The v. */
	private Vector<T> v;

	/**
	 * Instantiates a new vector iterator.
	 * 
	 * @param v
	 *            the v
	 * @param modCount
	 *            the mod count
	 */
	public VectorIterator(Vector<T> v, int modCount) {
		this.v = v;
		this.expectedModCount = modCount;
	}

	/** The pos. */
	int pos = -1;

	/** The expected mod count. */
	int expectedModCount;

	/** The last position. */
	int lastPosition = -1;

	/** The lastitem. */
	T lastitem = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return (this.pos + 1 < v.size());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#next()
	 */
	public T next() {
		// if (lastitem != null) {
		// recycle(lastitem);
		// }
		if (this.expectedModCount == v.getModCount()) {
			try {
				T localObject = v.get(this.pos + 1);
				this.lastPosition = (++this.pos);
				// lastitem = localObject;
				return localObject;
			} catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
				throw new NoSuchElementException();
			}
		}
		throw new ConcurrentModificationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		if (this.lastPosition == -1) {
			throw new IllegalStateException();
		}

		if (this.expectedModCount != v.getModCount()) {
			throw new ConcurrentModificationException();
		}
		try {
			v.remove(this.lastPosition);
		} catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
			throw new ConcurrentModificationException();
		}

		this.expectedModCount = v.getModCount();
		if (this.pos == this.lastPosition) {
			this.pos -= 1;
		}
		this.lastPosition = -1;
	}
}
