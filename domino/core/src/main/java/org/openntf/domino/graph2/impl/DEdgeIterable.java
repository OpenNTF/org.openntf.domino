package org.openntf.domino.graph2.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.openntf.domino.big.NoteCoordinate;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;

public class DEdgeIterable implements org.openntf.domino.graph2.DEdgeIterable {
	private org.openntf.domino.graph2.impl.DElementIterable delegate_;

	public static class DEdgeIterator implements org.openntf.domino.graph2.DEdgeIterable.DEdgeIterator {
		private Iterator<Element> delegate_;

		public DEdgeIterator(final Iterator<Element> delegate) {
			delegate_ = delegate;
		}

		@Override
		public DEdge next() {
			DEdge result = null;
			Element chk = delegate_.next();
			if (chk instanceof DEdge) {
				result = (DEdge) chk;
			} else {
				throw new IllegalStateException("Next element returned is a " + chk.getClass().getName() + " not a Vertex");
			}
			return result;
		}

		@Override
		public boolean hasNext() {
			return delegate_.hasNext();
		}

		@Override
		public void remove() {
			delegate_.remove();
		}
	}

	public DEdgeIterable(final DElementStore store, final List<NoteCoordinate> index) {
		delegate_ = new org.openntf.domino.graph2.impl.DElementIterable(store, index);
	}

	@Override
	public Iterator<Edge> iterator() {
		return new DEdgeIterator(delegate_.iterator());
	}

	@Override
	public boolean add(final Edge e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void add(final int index, final Edge element) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean addAll(final Collection<? extends Edge> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(final int index, final Collection<? extends Edge> c) {
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
	public Edge get(final int index) {
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
	public ListIterator<Edge> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListIterator<Edge> listIterator(final int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Edge remove(final int index) {
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
	public Edge set(final int index, final Edge element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Edge> subList(final int fromIndex, final int toIndex) {
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
