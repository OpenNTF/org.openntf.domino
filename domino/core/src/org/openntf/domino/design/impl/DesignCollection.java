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

package org.openntf.domino.design.impl;

import java.util.Iterator;
import java.util.logging.Logger;

import org.openntf.domino.Document;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.design.DesignBase;

/**
 * @author jgallagher
 * 
 */
public class DesignCollection<E extends DesignBase> implements org.openntf.domino.design.DesignCollection<E> {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(DesignCollection.class.getName());

	private final NoteCollection collection_;
	private final Class<? extends DesignBase> clazz_;

	public DesignCollection(final NoteCollection collection, final Class<? extends DesignBase> clazz) {
		collection_ = collection;
		clazz_ = clazz;
	}

	@Override
	public int getCount() {
		return collection_.getCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<E> iterator() {
		return new DesignIterator<E>();
	}

	public class DesignIterator<T extends E> implements Iterator<T> {
		private final Iterator<String> iterator_;

		protected DesignIterator() {
			iterator_ = collection_.iterator();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			return iterator_.hasNext();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Iterator#next()
		 */
		@SuppressWarnings("unchecked")
		@Override
		public T next() {
			String noteId = iterator_.next();
			Document doc = collection_.getAncestorDatabase().getDocumentByID(noteId);
			DesignBase ret = DesignFactory.fromDocument(doc);
			if (clazz_ != null && !clazz_.isAssignableFrom(ret.getClass()))
				throw new ClassCastException("Cannot cast " + ret.getClass().getName() + " to " + clazz_.getName());
			return (T) ret;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}
}
