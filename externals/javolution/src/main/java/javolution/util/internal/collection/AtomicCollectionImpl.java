/*
 * Javolution - Java(TM) Solution for Real-Time and Embedded Systems
 * Copyright (C) 2012 - Javolution (http://javolution.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package javolution.util.internal.collection;

import java.util.Collection;
import java.util.Iterator;

import javolution.util.function.Consumer;
import javolution.util.function.Equality;
import javolution.util.service.CollectionService;

/**
 * An atomic view over a collection (copy-on-write).
 */
public class AtomicCollectionImpl<E> extends CollectionView<E> {

	/** Thread-Safe Iterator. */
	private class IteratorImpl implements Iterator<E> {
		private E current;
		private final Iterator<E> targetIterator;

		public IteratorImpl() {
			targetIterator = targetView().iterator();
		}

		@Override
		public boolean hasNext() {
			return targetIterator.hasNext();
		}

		@Override
		public E next() {
			current = targetIterator.next();
			return current;
		}

		@Override
		public void remove() {
			if (current == null)
				throw new IllegalStateException();
			AtomicCollectionImpl.this.remove(current);
			current = null;
		}
	}

	private static final long serialVersionUID = 0x600L; // Version.
	protected volatile CollectionService<E> immutable; // The copy used by readers.
	protected transient Thread updatingThread; // The thread executing an update.

	public AtomicCollectionImpl(final CollectionService<E> target) {
		super(target);
		this.immutable = cloneTarget();
	}

	@Override
	public synchronized boolean add(final E element) {
		boolean changed = target().add(element);
		if (changed && !updateInProgress())
			immutable = cloneTarget();
		return changed;
	}

	@Override
	public synchronized boolean addAll(final Collection<? extends E> c) {
		boolean changed = target().addAll(c);
		if (changed && !updateInProgress())
			immutable = cloneTarget();
		return changed;
	}

	@Override
	public synchronized void clear() {
		target().clear();
		if (!updateInProgress()) {
			immutable = cloneTarget();
		}
	}

	@Override
	public synchronized AtomicCollectionImpl<E> clone() { // Synchronized required since working with real target.
		AtomicCollectionImpl<E> copy = (AtomicCollectionImpl<E>) super.clone();
		copy.updatingThread = null;
		return copy;
	}

	@Override
	public Equality<? super E> comparator() {
		return immutable.comparator();
	}

	@Override
	public boolean contains(final Object o) {
		return targetView().contains(o);
	}

	@Override
	public boolean containsAll(final Collection<?> c) {
		return targetView().containsAll(c);
	}

	@Override
	public boolean equals(final Object o) {
		return targetView().equals(o);
	}

	@Override
	public int hashCode() {
		return targetView().hashCode();
	}

	@Override
	public boolean isEmpty() {
		return targetView().isEmpty();
	}

	@Override
	public Iterator<E> iterator() {
		return new IteratorImpl();
	}

	@Override
	public synchronized boolean remove(final Object o) {
		boolean changed = target().remove(o);
		if (changed && !updateInProgress())
			immutable = cloneTarget();
		return changed;
	}

	@Override
	public synchronized boolean removeAll(final Collection<?> c) {
		boolean changed = target().removeAll(c);
		if (changed && !updateInProgress())
			immutable = cloneTarget();
		return changed;
	}

	@Override
	public synchronized boolean retainAll(final Collection<?> c) {
		boolean changed = target().retainAll(c);
		if (changed && !updateInProgress())
			immutable = cloneTarget();
		return changed;
	}

	@Override
	public int size() {
		return targetView().size();
	}

	@Override
	public Object[] toArray() {
		return targetView().toArray();
	}

	@Override
	public <T> T[] toArray(final T[] a) {
		return targetView().toArray(a);
	}

	@Override
	public synchronized void update(final Consumer<CollectionService<E>> action, final CollectionService<E> view) {
		updatingThread = Thread.currentThread(); // Update in progress.
		try {
			target().update(action, view); // No copy performed.
		} finally {
			updatingThread = null;
			immutable = cloneTarget(); // One single copy !
		}
	}

	/** Returns a clone copy of target. */
	protected CollectionService<E> cloneTarget() {
		try {
			return target().clone();
		} catch (CloneNotSupportedException e) {
			throw new Error("Cannot happen since target is Cloneable.");
		}
	}

	/**
	 * Returns either the immutable target or the actual target if updating thread.
	 */
	protected CollectionService<E> targetView() {
		return ((updatingThread == null) || (updatingThread != Thread.currentThread())) ? immutable : target();
	}

	/** Indicates if the current thread is doing an atomic update. */
	protected final boolean updateInProgress() {
		return updatingThread == Thread.currentThread();

	}
}
