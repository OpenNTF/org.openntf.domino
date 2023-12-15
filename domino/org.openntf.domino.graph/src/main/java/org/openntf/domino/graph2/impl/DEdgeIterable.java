/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.graph2.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.openntf.domino.big.NoteCoordinate;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;

@SuppressWarnings("nls")
public class DEdgeIterable implements org.openntf.domino.graph2.DEdgeIterable {
	private org.openntf.domino.graph2.impl.DElementIterable delegate_;

	public static class DEdgeIterator implements ListIterator<Edge> {
		private ListIterator<Element> delegate_;

		public DEdgeIterator(final ListIterator<Element> delegate) {
			delegate_ = delegate;
		}

		@Override
		public DEdge next() {
			DEdge result = null;
			Element chk = delegate_.next();
			if (chk instanceof DEdge) {
				result = (DEdge) chk;
			} else {
				throw new IllegalStateException("Next element returned is a " + chk.getClass().getName() + " not an Edge");
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

		@Override
		public void add(final Edge arg0) {
			delegate_.add(arg0);
		}

		@Override
		public boolean hasPrevious() {
			return delegate_.hasPrevious();
		}

		@Override
		public int nextIndex() {
			return delegate_.nextIndex();
		}

		@Override
		public int previousIndex() {
			return delegate_.previousIndex();
		}

		@Override
		public void set(final Edge arg0) {
			delegate_.set(arg0);
		}

		@Override
		public DEdge previous() {
			DEdge result = null;
			Element chk = delegate_.previous();
			if (chk instanceof DEdge) {
				result = (DEdge) chk;
			} else {
				throw new IllegalStateException("Previous element returned is a " + chk.getClass().getName() + " not an Edge");
			}
			return result;
		}
	}

	public DEdgeIterable(final DElementStore store, final List<NoteCoordinate> index) {
		delegate_ = new org.openntf.domino.graph2.impl.DElementIterable(store, index);
	}

	private DEdgeIterable(final org.openntf.domino.graph2.impl.DElementIterable delegate) {
		delegate_ = delegate;
	}

	@Override
	public Iterator<Edge> iterator() {
		return new DEdgeIterator(delegate_.listIterator());
	}

	@Override
	public boolean add(final Edge e) {
		return delegate_.add(e);
	}

	@Override
	public void add(final int index, final Edge element) {
		delegate_.add(index, element);
	}

	@Override
	public boolean addAll(final Collection<? extends Edge> c) {
		return delegate_.addAll(c);
	}

	@Override
	public boolean addAll(final int index, final Collection<? extends Edge> c) {
		return delegate_.addAll(index, c);
	}

	@Override
	public void clear() {
		delegate_.clear();
	}

	@Override
	public boolean contains(final Object o) {
		return delegate_.contains(o);
	}

	@Override
	public boolean containsAll(final Collection<?> c) {
		return delegate_.containsAll(c);
	}

	@Override
	public Edge get(final int index) {
		return (Edge) delegate_.get(index);
	}

	@Override
	public int indexOf(final Object o) {
		return delegate_.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return delegate_.isEmpty();
	}

	@Override
	public int lastIndexOf(final Object o) {
		return delegate_.lastIndexOf(o);
	}

	@Override
	public ListIterator<Edge> listIterator() {
		return new DEdgeIterator(delegate_.listIterator());
	}

	@Override
	public ListIterator<Edge> listIterator(final int index) {
		return new DEdgeIterator(delegate_.listIterator(index));
	}

	@Override
	public Edge remove(final int index) {
		return (Edge) delegate_.remove(index);
	}

	@Override
	public boolean remove(final Object o) {
		return delegate_.remove(o);
	}

	@Override
	public boolean removeAll(final Collection<?> c) {
		return delegate_.removeAll(c);
	}

	@Override
	public boolean retainAll(final Collection<?> c) {
		return delegate_.retainAll(c);
	}

	@Override
	public Edge set(final int index, final Edge element) {
		return (Edge) delegate_.set(index, element);
	}

	@Override
	public int size() {
		return delegate_.size();
	}

	@Override
	public List<Edge> subList(final int fromIndex, final int toIndex) {
		return new DEdgeIterable((DElementIterable) delegate_.subList(fromIndex, toIndex));
	}

	@Override
	public Object[] toArray() {
		return delegate_.toArray();
	}

	@Override
	public <T> T[] toArray(final T[] a) {
		return delegate_.toArray(a);
	}
}
