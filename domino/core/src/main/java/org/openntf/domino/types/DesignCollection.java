/**
 * 
 */
package org.openntf.domino.types;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

import org.openntf.domino.NoteCollection;

/**
 * @author jgallagher
 * 
 */
public class DesignCollection<T> extends AbstractCollection<T> {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(DesignCollection.class.getName());
	private final NoteCollection collection_;

	public DesignCollection(final NoteCollection collection) {
		collection_ = collection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#add(java.lang.Object)
	 */
	@Override
	public boolean add(final T arg0) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(final Collection<? extends T> c) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#clear()
	 */
	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		// TODO Implement this
		return null;
	}

	@Override
	public int size() {
		return collection_.getCount();
	}
}
