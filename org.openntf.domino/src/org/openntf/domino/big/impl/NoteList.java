package org.openntf.domino.big.impl;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javolution.util.FastSortedTable;
import javolution.util.FastTable;
import javolution.util.function.Equality;

public class NoteList implements List<NoteCoordinate>, Externalizable {
	private FastTable<NoteCoordinate> delegate_;
	@SuppressWarnings("unused")
	private DbCache localCache_ = null;

	protected static class NoteComparator implements Equality<NoteCoordinate> {
		private static final long serialVersionUID = 1L;
		private String comparisonKey_;

		NoteComparator(final String key) {
			comparisonKey_ = key;
		}

		@SuppressWarnings("unchecked")
		@Override
		public int compare(final NoteCoordinate arg0, final NoteCoordinate arg1) {
			Object obj0 = arg0.get(comparisonKey_);
			Object obj1 = arg1.get(comparisonKey_);
			return ((Comparable<Object>) obj0).compareTo(obj1);
		}

		@Override
		public int hashOf(final NoteCoordinate object) {
			return object.hashCode();
		}

		@Override
		public boolean equal(final NoteCoordinate left, final NoteCoordinate right) {
			return left.equals(right);
		}

	}

	public static NoteComparator getComparator(final String key) {
		return new NoteComparator(key);
	}

	public NoteList() {
		delegate_ = new FastTable<NoteCoordinate>();
	}

	public NoteList(final boolean concurrent) {
		delegate_ = new FastTable<NoteCoordinate>().atomic();
	}

	public NoteList(final DbCache cache) {
		localCache_ = cache;
		delegate_ = new FastTable<NoteCoordinate>();
	}

	public NoteList(final DbCache cache, final Equality<NoteCoordinate> compare) {
		localCache_ = cache;	//NTF this doesn't do anything yet
		delegate_ = new FastSortedTable<NoteCoordinate>(compare);
	}

	public void sortBy(final String key) {
		NoteComparator comp = new NoteComparator(key);
		FastSortedTable<NoteCoordinate> newDel = new FastSortedTable<NoteCoordinate>(comp);
		newDel.addAll(delegate_);
		delegate_ = newDel;
	}

	@Override
	public boolean add(final NoteCoordinate e) {
		return delegate_.add(e);
	}

	@Override
	public void add(final int index, final NoteCoordinate element) {
		delegate_.add(index, element);
	}

	@Override
	public boolean addAll(final Collection<? extends NoteCoordinate> c) {
		return delegate_.addAll(c);
	}

	@Override
	public boolean addAll(final int index, final Collection<? extends NoteCoordinate> c) {
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
	public NoteCoordinate get(final int index) {
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
	public Iterator<NoteCoordinate> iterator() {
		return delegate_.iterator();
	}

	@Override
	public int lastIndexOf(final Object o) {
		return delegate_.lastIndexOf(o);
	}

	@Override
	public ListIterator<NoteCoordinate> listIterator() {
		return delegate_.listIterator();
	}

	@Override
	public ListIterator<NoteCoordinate> listIterator(final int index) {
		return delegate_.listIterator(index);
	}

	@Override
	public NoteCoordinate remove(final int index) {
		return delegate_.remove(index);
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
	public NoteCoordinate set(final int index, final NoteCoordinate element) {
		return delegate_.set(index, element);
	}

	@Override
	public int size() {
		return delegate_.size();
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<NoteCoordinate> subList(final int fromIndex, final int toIndex) {
		return delegate_.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return delegate_.toArray();
	}

	@Override
	public <T> T[] toArray(final T[] a) {
		return delegate_.toArray(a);
	}

	public byte[] toByteArray() {
		byte[] result = new byte[size() * 24];
		int pos = 0;
		for (NoteCoordinate nc : this) {
			pos = nc.insertToByteArray(result, pos);
		}
		return result;
	}

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
		for (NoteCoordinate coord : this) {
			arg0.write(coord.toByteArray());
		}
	}

}
