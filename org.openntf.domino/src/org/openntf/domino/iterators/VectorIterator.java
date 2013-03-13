package org.openntf.domino.iterators;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import org.openntf.domino.impl.Vector;

public class VectorIterator<T> implements Iterator<T> {
	private static final Logger log_ = Logger.getLogger(VectorIterator.class.getName());
	private Vector<T> v;

	public VectorIterator(Vector<T> v, int modCount) {
		this.v = v;
		this.expectedModCount = modCount;
	}

	int pos = -1;
	int expectedModCount;
	int lastPosition = -1;
	T lastitem = null;

	public boolean hasNext() {
		return (this.pos + 1 < v.size());
	}

	private void recycle(T b) {
		org.openntf.domino.impl.Base.recycle(b);
	}

	public T next() {
		if (lastitem != null) {
			recycle(lastitem);
		}
		if (this.expectedModCount == v.getModCount()) {
			try {
				T localObject = v.get(this.pos + 1);
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

		if (this.expectedModCount != v.getModCount()) {
			throw new ConcurrentModificationException();
		}
		try {
			v.remove(this.lastPosition);
		} catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
			throw new ConcurrentModificationException();
		}

		this.expectedModCount = v.getModCount();
		if (this.pos == this.lastPosition) {
			this.pos -= 1;
		}
		this.lastPosition = -1;
	}
}
