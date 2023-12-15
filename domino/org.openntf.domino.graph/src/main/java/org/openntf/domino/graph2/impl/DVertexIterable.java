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

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.openntf.domino.big.NoteCoordinate;
import org.openntf.domino.exceptions.UnimplementedException;
import org.openntf.domino.graph2.DGraphUtils;

import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings("nls")
public class DVertexIterable implements org.openntf.domino.graph2.DVertexIterable {
	private org.openntf.domino.graph2.impl.DElementIterable delegate_;

	public static class DVertexIterator implements ListIterator<Vertex> {
		private ListIterator<Element> delegate_;

		public DVertexIterator(final ListIterator<Element> delegate) {
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

		@Override
		public void add(final Vertex arg0) {
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
		public void set(final Vertex arg0) {
			delegate_.set(arg0);
		}

		@Override
		public Vertex previous() {
			DVertex result = null;
			Element chk = delegate_.previous();
			if (chk instanceof DVertex) {
				result = (DVertex) chk;
			} else {
				throw new IllegalStateException("Previous element returned is a " + chk.getClass().getName() + " not a Vertex");
			}
			return result;
		}
	}

	public DVertexIterable(final DElementStore store, final List<NoteCoordinate> index) {
		delegate_ = new org.openntf.domino.graph2.impl.DElementIterable(store, index);
	}

	private DVertexIterable(final org.openntf.domino.graph2.impl.DElementIterable delegate) {
		delegate_ = delegate;
	}

	@Override
	public Iterator<Vertex> iterator() {
		return new DVertexIterator(delegate_.listIterator());
	}

	@Override
	public boolean add(final Vertex e) {
		return delegate_.add(e);
	}

	@Override
	public void add(final int index, final Vertex element) {
		delegate_.add(index, element);
	}

	@Override
	public boolean addAll(final Collection<? extends Vertex> c) {
		return delegate_.addAll(c);
	}

	@Override
	public boolean addAll(final int index, final Collection<? extends Vertex> c) {
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
	public Vertex get(final int index) {
		return (Vertex) delegate_.get(index);
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
	public ListIterator<Vertex> listIterator() {
		return new DVertexIterator(delegate_.listIterator());
	}

	@Override
	public ListIterator<Vertex> listIterator(final int index) {
		return new DVertexIterator(delegate_.listIterator(index));
	}

	@Override
	public Vertex remove(final int index) {
		return (Vertex) delegate_.remove(index);
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
	public Vertex set(final int index, final Vertex element) {
		return (Vertex) delegate_.set(index, element);
	}

	@Override
	public int size() {
		return delegate_.size();
	}

	@Override
	public List<Vertex> subList(final int fromIndex, final int toIndex) {
		return new DVertexIterable((DElementIterable) delegate_.subList(fromIndex, toIndex));
	}

	@Override
	public Object[] toArray() {
		return delegate_.toArray();
	}

	@Override
	public <T> T[] toArray(final T[] a) {
		return delegate_.toArray(a);
	}

	private static final Vertex[] VA = new Vertex[1];

	public DVertexIterable sortBy(final List<CharSequence> keys, final boolean desc) {
		Vertex[] array = toArray(VA);
		Arrays.sort(array, new DGraphUtils.VertexComparator(keys, desc));
		throw new UnimplementedException("sortBy not yet completed for DVertexIterable");
		//		return new DVertexIterable(framedGraph, sourceVertex_, convertToEdges(array), kind);
	}

}
