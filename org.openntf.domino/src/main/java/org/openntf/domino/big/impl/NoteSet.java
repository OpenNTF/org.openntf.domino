package org.openntf.domino.big.impl;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import javolution.util.FastSet;

public class NoteSet implements Set<NoteCoordinate>, Externalizable {
	protected FastSet<NoteCoordinate> delegate_;
	@SuppressWarnings("unused")
	protected DbCache localCache_ = null;

	public NoteSet() {
		delegate_ = new FastSet<NoteCoordinate>();
	}

	public NoteSet(final boolean concurrent) {
		delegate_ = new FastSet<NoteCoordinate>().atomic();
	}

	public NoteSet(final DbCache cache) {
		localCache_ = cache;
		delegate_ = new FastSet<NoteCoordinate>();
	}

	@Override
	public boolean add(final NoteCoordinate e) {
		return delegate_.add(e);
	}

	@Override
	public boolean addAll(final Collection<? extends NoteCoordinate> c) {
		return delegate_.addAll(c);
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
	public boolean isEmpty() {
		return delegate_.isEmpty();
	}

	@Override
	public Iterator<NoteCoordinate> iterator() {
		return delegate_.iterator();
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
	public int size() {
		return delegate_.size();
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
