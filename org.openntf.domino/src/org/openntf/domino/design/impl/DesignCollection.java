/**
 * 
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
	private static final long serialVersionUID = 1L;

	private final NoteCollection collection_;
	private final Class<? extends AbstractDesignBase> clazz_;

	public DesignCollection(final NoteCollection collection, final Class<? extends AbstractDesignBase> clazz) {
		collection_ = collection;
		clazz_ = clazz;
	}

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
		@Override
		public T next() {
			String noteId = iterator_.next();
			Document doc = collection_.getAncestorDatabase().getDocumentByID(noteId);
			return DesignFactory.fromDocument(doc, clazz_);
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
