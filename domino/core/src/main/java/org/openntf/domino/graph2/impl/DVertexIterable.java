package org.openntf.domino.graph2.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.openntf.domino.big.NoteCoordinate;

import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Vertex;

public class DVertexIterable implements org.openntf.domino.graph2.DVertexIterable {
	private org.openntf.domino.graph2.impl.DElementIterable delegate_;

	public static class DVertexIterator implements org.openntf.domino.graph2.DVertexIterable.DVertexIterator {
		private Iterator<Element> delegate_;

		public DVertexIterator(final Iterator<Element> delegate) {
			delegate_ = delegate;
		}

		@Override
		public DVertex next() {
			DVertex result = null;
			Element chk = delegate_.next();
			if (chk instanceof DVertex) {
				result = (DVertex) chk;
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

	public DVertexIterable(final DElementStore store, final List<NoteCoordinate> index) {
		delegate_ = new org.openntf.domino.graph2.impl.DElementIterable(store, index);
	}

	@Override
	public Iterator<Vertex> iterator() {
		return new DVertexIterator(delegate_.iterator());
	}

	@Override
	public boolean add(final Vertex e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void add(final int index, final Vertex element) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean addAll(final Collection<? extends Vertex> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(final int index, final Collection<? extends Vertex> c) {
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
	public Vertex get(final int index) {
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
	public ListIterator<Vertex> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListIterator<Vertex> listIterator(final int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vertex remove(final int index) {
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
	public Vertex set(final int index, final Vertex element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Vertex> subList(final int fromIndex, final int toIndex) {
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
