package org.openntf.domino.graph2.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.openntf.domino.big.NoteCoordinate;

import com.tinkerpop.blueprints.Element;

public class DElementIterable implements org.openntf.domino.graph2.DElementIterable, List<Element> {
	public static class DElementIterator implements org.openntf.domino.graph2.DElementIterable.DElementIterator, Iterator<Element> {
		protected final DElementStore elementStore_;
		protected final Iterable<NoteCoordinate> index_;
		protected Iterator<NoteCoordinate> iterator_;

		public DElementIterator(final DElementStore store, final Iterable<NoteCoordinate> index) {
			elementStore_ = store;
			index_ = index;
		}

		private Iterator<NoteCoordinate> getIterator() {
			if (iterator_ == null) {
				iterator_ = index_.iterator();
			}
			return iterator_;
		}

		@Override
		public boolean hasNext() {
			return getIterator().hasNext();
		}

		@Override
		public Element next() {
			Element result = null;
			NoteCoordinate nc = getIterator().next();
			if (nc != null) {
				result = elementStore_.getElement(nc);
			}
			return result;
		}

		@Override
		public void remove() {
			getIterator().remove();
		}
	}

	protected final List<NoteCoordinate> index_;
	protected final DElementStore store_;

	public DElementIterable(final DElementStore store, final List<NoteCoordinate> index) {
		store_ = store;
		index_ = index;
	}

	@Override
	public Iterator<Element> iterator() {
		return new DElementIterator(store_, index_);
	}

	@Override
	public boolean add(final Element e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void add(final int index, final Element element) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean addAll(final Collection<? extends Element> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(final int index, final Collection<? extends Element> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean contains(final Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsAll(final Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Element get(final int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int indexOf(final Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int lastIndexOf(final Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ListIterator<Element> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListIterator<Element> listIterator(final int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Element remove(final int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(final Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(final Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(final Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Element set(final int index, final Element element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Element> subList(final int fromIndex, final int toIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T[] toArray(final T[] a) {
		// TODO Auto-generated method stub
		return null;
	}

}
