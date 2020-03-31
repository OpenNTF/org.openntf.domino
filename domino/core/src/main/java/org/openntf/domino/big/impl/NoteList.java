/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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
package org.openntf.domino.big.impl;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javolution.util.FastSortedTable;
import javolution.util.function.Equality;

public class NoteList implements org.openntf.domino.big.NoteList {
	protected List<org.openntf.domino.big.NoteCoordinate> delegate_;
	protected DbCache localCache_ = null;
	protected boolean isSynced_ = false;

	protected static class NoteComparator implements Equality<org.openntf.domino.big.NoteCoordinate> {
		private static final long serialVersionUID = 1L;
		private String comparisonKey_;

		NoteComparator(final String key) {
			comparisonKey_ = key;
		}

		@Override
		@SuppressWarnings("unchecked")
		public int compare(final org.openntf.domino.big.NoteCoordinate arg0, final org.openntf.domino.big.NoteCoordinate arg1) {
			Object obj0 = arg0.get(comparisonKey_);
			Object obj1 = arg1.get(comparisonKey_);
			return ((Comparable<Object>) obj0).compareTo(obj1);
		}

		@Override
		public int hashOf(final org.openntf.domino.big.NoteCoordinate object) {
			return object.hashCode();
		}

		@Override
		public boolean equal(final org.openntf.domino.big.NoteCoordinate left, final org.openntf.domino.big.NoteCoordinate right) {
			return left.equals(right);
		}

	}

	public static NoteComparator getComparator(final String key) {
		return new NoteComparator(key);
	}

	public NoteList() {
		delegate_ = new ArrayList<org.openntf.domino.big.NoteCoordinate>();
	}

	public NoteList(final boolean concurrent) {
		delegate_ = Collections.synchronizedList(new ArrayList<org.openntf.domino.big.NoteCoordinate>());
		isSynced_ = true;
	}

	@Override
	public void sortBy(final String key) {
		NoteComparator comp = new NoteComparator(key);
		FastSortedTable<org.openntf.domino.big.NoteCoordinate> newDel = new FastSortedTable<org.openntf.domino.big.NoteCoordinate>(comp);
		newDel.addAll(delegate_);
		delegate_ = newDel;
	}

	@Override
	public boolean add(final org.openntf.domino.big.NoteCoordinate e) {
		if (e == null) {
			return true;
		}
		return delegate_.add(e);
	}

	@Override
	public void add(final int index, final org.openntf.domino.big.NoteCoordinate element) {
		if (element != null) {
			delegate_.add(index, element);
		}
	}

	@Override
	public boolean addAll(final Collection<? extends org.openntf.domino.big.NoteCoordinate> c) {
		if (c == null) {
			return true;
		}
		return delegate_.addAll(c);
	}

	@Override
	public boolean addAll(final int index, final Collection<? extends org.openntf.domino.big.NoteCoordinate> c) {
		if (c == null) {
			return true;
		}
		return delegate_.addAll(index, c);
	}

	@Override
	public void clear() {
		delegate_.clear();
	}

	@Override
	public boolean contains(final Object o) {
		if (o == null) {
			return true;
		}
		return delegate_.contains(o);
	}

	@Override
	public boolean containsAll(final Collection<?> c) {
		if (c == null) {
			return true;
		}
		return delegate_.containsAll(c);
	}

	@Override
	public org.openntf.domino.big.NoteCoordinate get(final int index) {
		return delegate_.get(index);
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
	public Iterator<org.openntf.domino.big.NoteCoordinate> iterator() {
		return delegate_.iterator();
	}

	@Override
	public int lastIndexOf(final Object o) {
		return delegate_.lastIndexOf(o);
	}

	@Override
	public ListIterator<org.openntf.domino.big.NoteCoordinate> listIterator() {
		return delegate_.listIterator();
	}

	@Override
	public ListIterator<org.openntf.domino.big.NoteCoordinate> listIterator(final int index) {
		return delegate_.listIterator(index);
	}

	@Override
	public org.openntf.domino.big.NoteCoordinate remove(final int index) {
		return delegate_.remove(index);
	}

	@Override
	public boolean remove(final Object o) {
		//		System.out.println("TEMP DEBUG NoteList removing a " + o.getClass().getName() + " " + String.valueOf(o));
		boolean result = delegate_.remove(o);
		if (!result) {
			//			System.out.println("TEMP DEBUG Unable to remove element from NoteList!");
		}
		return result;
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
	public org.openntf.domino.big.NoteCoordinate set(final int index, final org.openntf.domino.big.NoteCoordinate element) {
		return delegate_.set(index, element);
	}

	@Override
	public int size() {
		return delegate_.size();
	}

	@Override
	public List<org.openntf.domino.big.NoteCoordinate> subList(final int fromIndex, final int toIndex) {
		NoteList result = new NoteList(isSynced_);
		result.addAll(delegate_.subList(fromIndex, toIndex));
		return result;
	}

	@Override
	public Object[] toArray() {
		return delegate_.toArray();
	}

	@Override
	public <T> T[] toArray(final T[] a) {
		return delegate_.toArray(a);
	}

	@Override
	public byte[] toByteArray() {
		byte[] result = new byte[size() * 24];
		int pos = 0;
		for (org.openntf.domino.big.NoteCoordinate nc : this) {
			pos = nc.insertToByteArray(result, pos);
		}
		return result;
	}

	@Override
	public void loadByteArray(final byte[] bytes) {
		int size = bytes.length / 24;
		byte[] buffer = new byte[24];
		for (int i = 0; i < size; i++) {
			System.arraycopy(bytes, i * 24, buffer, 0, 24);
			add(new NoteCoordinate(buffer));
		}
	}

	@Override
	public void readExternal(final ObjectInput arg0) throws IOException, ClassNotFoundException {
		int size = arg0.readInt();
		byte[] buffer = new byte[24];
		for (int i = 0; i < size; i++) {
			arg0.read(buffer);
			add(new NoteCoordinate(buffer));
		}
	}

	@Override
	public void writeExternal(final ObjectOutput arg0) throws IOException {
		arg0.writeInt(size());
		for (org.openntf.domino.big.NoteCoordinate coord : this) {
			arg0.write(coord.toByteArray());
		}
	}

	//	public NoteList intersect(final NoteList intersectingList) {
	//		delegate_.retainAll(intersectingList.delegate_);
	//		return this;
	//	}

}
