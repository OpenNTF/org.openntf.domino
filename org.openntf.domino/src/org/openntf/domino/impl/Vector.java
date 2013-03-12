package org.openntf.domino.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class Vector<E> extends java.util.Vector<E> implements Collection<E> {
	private final java.util.ArrayList<E> delegate_;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Vector() {
		delegate_ = new ArrayList<E>();
	}

	public Vector(Collection<E> source) {
		delegate_ = new ArrayList<E>(source);
	}

	@Override
	public boolean add(E arg0) {
		return delegate_.add(arg0);
	}

	@Override
	public void add(int arg0, E arg1) {
		delegate_.add(arg0, arg1);
	}

	@Override
	public boolean addAll(Collection<? extends E> arg0) {
		return delegate_.addAll(arg0);
	}

	@Override
	public boolean addAll(int arg0, Collection<? extends E> arg1) {
		return delegate_.addAll(arg0, arg1);
	}

	@Override
	@Deprecated
	public void addElement(E arg0) {
		delegate_.add(arg0);
	}

	@Override
	public int capacity() {
		return delegate_.size();
	}

	@Override
	public void clear() {
		delegate_.clear();
	}

	@Override
	public Object clone() {
		return delegate_.clone();
	}

	@Override
	public boolean contains(Object arg0) {
		return delegate_.contains(arg0);
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		return delegate_.containsAll(arg0);
	}

	@Override
	@Deprecated
	public void copyInto(Object[] arg0) {
		System.arraycopy(this.toArray(), 0, arg0, 0, this.size());
	}

	@Override
	@Deprecated
	public E elementAt(int arg0) {
		return delegate_.get(arg0);
	}

	@Override
	@Deprecated
	public Enumeration<E> elements() {
		return Collections.enumeration(delegate_);
	}

	@Override
	public void ensureCapacity(int arg0) {
		delegate_.ensureCapacity(arg0);
	}

	@Override
	public boolean equals(Object arg0) {
		return delegate_.equals(arg0);
	}

	@Override
	public E firstElement() {
		return delegate_.get(0);
	}

	@Override
	public E get(int arg0) {
		return delegate_.get(arg0);
	}

	@Override
	public int hashCode() {
		return delegate_.hashCode();
	}

	@Override
	@Deprecated
	public int indexOf(Object arg0, int arg1) {
		int i;
		if (arg0 != null) {
			for (i = arg1; i < this.size(); ++i) {
				if (arg0.equals(delegate_.toArray()[i]))
					return i;
			}
		} else {
			for (i = arg1; i < this.size(); ++i) {
				if (delegate_.toArray() == null) {
					return i;
				}
			}
		}
		return -1;

	}

	@Override
	public int indexOf(Object arg0) {
		return delegate_.indexOf(arg0);
	}

	@Override
	@Deprecated
	public void insertElementAt(E arg0, int arg1) {
		delegate_.add(arg1, arg0);
	}

	@Override
	public boolean isEmpty() {
		return delegate_.isEmpty();
	}

	@Override
	public Iterator<E> iterator() {
		return new RecyclingIterator();
	}

	private class RecyclingIterator implements Iterator<E> {
		int pos = -1;
		int expectedModCount;
		int lastPosition = -1;
		E lastitem = null;

		RecyclingIterator() {
			this.expectedModCount = Vector.this.modCount;
		}

		public boolean hasNext() {
			return (this.pos + 1 < Vector.this.size());
		}

		private void recycle(E b) {
			org.openntf.domino.impl.Base.recycle(b);
		}

		public E next() {
			if (lastitem != null) {
				recycle(lastitem);
			}
			if (this.expectedModCount == Vector.this.modCount) {
				try {
					E localObject = Vector.this.get(this.pos + 1);
					this.lastPosition = (++this.pos);
					lastitem = localObject;
					return localObject;
				} catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
					throw new NoSuchElementException();
				}
			}
			throw new ConcurrentModificationException();
		}

		public void remove() {
			if (this.lastPosition == -1) {
				throw new IllegalStateException();
			}

			if (this.expectedModCount != Vector.this.modCount) {
				throw new ConcurrentModificationException();
			}
			try {
				Vector.this.remove(this.lastPosition);
			} catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
				throw new ConcurrentModificationException();
			}

			this.expectedModCount = Vector.this.modCount;
			if (this.pos == this.lastPosition) {
				this.pos -= 1;
			}
			this.lastPosition = -1;
		}
	}

	@Override
	@Deprecated
	public E lastElement() {
		return delegate_.get(size() - 1);
		// return delegate_.lastElement();
	}

	@Override
	@Deprecated
	public int lastIndexOf(Object arg0, int arg1) {
		return delegate_.lastIndexOf(arg0);
		// return delegate_.lastIndexOf(arg0, arg1);
	}

	@Override
	public int lastIndexOf(Object arg0) {
		return delegate_.lastIndexOf(arg0);
	}

	@Override
	public ListIterator<E> listIterator() {
		return delegate_.listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int arg0) {
		return delegate_.listIterator(arg0);
	}

	@Override
	public E remove(int arg0) {
		return delegate_.remove(arg0);
	}

	@Override
	public boolean remove(Object arg0) {
		return delegate_.remove(arg0);
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		return delegate_.removeAll(arg0);
	}

	@Override
	public void removeAllElements() {
		delegate_.clear();
	}

	@Override
	public boolean removeElement(Object arg0) {
		return delegate_.remove(arg0);
	}

	@Override
	public void removeElementAt(int arg0) {
		delegate_.remove(arg0);
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		return delegate_.retainAll(arg0);
	}

	@Override
	public E set(int arg0, E arg1) {
		return delegate_.set(arg0, arg1);
	}

	@Override
	@Deprecated
	public void setElementAt(E arg0, int arg1) {
		// TODO make work, since it's a replace
		delegate_.remove(arg1);
		delegate_.add(arg1, arg0);
	}

	@Override
	@Deprecated
	public void setSize(int arg0) {
		// TODO NTF - absolutely certain that this implementation is wrong
		if (arg0 == size()) {
			return;
		}
		ensureCapacity(arg0);
		if (size() > arg0) {
			Arrays.fill(this.elementData, arg0, size(), null);
		}
		this.elementCount = arg0;
		this.modCount += 1;
	}

	@Override
	public int size() {
		return delegate_.size();
	}

	@Override
	public List<E> subList(int arg0, int arg1) {
		return delegate_.subList(arg0, arg1);
	}

	@Override
	public Object[] toArray() {
		return delegate_.toArray();
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		return delegate_.toArray(arg0);
	}

	@Override
	public String toString() {
		return delegate_.toString();
	}

	@Override
	public void trimToSize() {
		delegate_.trimToSize();
	}

}
