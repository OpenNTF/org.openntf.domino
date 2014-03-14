package org.openntf.domino.formula.impl;

import java.util.AbstractCollection;
import java.util.Iterator;

import org.openntf.domino.formula.ValueHolder;

public abstract class ParameterCollectionAbstract<T> extends AbstractCollection<T> {

	protected ValueHolder[] params;
	protected boolean permutative;
	protected int size;

	public ParameterCollectionAbstract(final ValueHolder[] params, final boolean permutative) {
		this.params = params;
		this.permutative = permutative;
		this.size = params[0].size();
		if (permutative) {
			for (int i = 1; i < params.length; i++) {
				this.size *= params[i].size();
			}
		} else {
			for (int i = 1; i < params.length; i++) {
				this.size = Math.max(this.size, params[i].size());
			}
		}
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public abstract Iterator<T> iterator();

	protected abstract class ParameterIteratorAbstract implements Iterator<T> {
		// Due performance reasons, the "ret" value is reused!
		// this does not matter
		int idx = 0;

		public boolean hasNext() {
			return idx < size;
		}

		public T next() {
			T ret;
			ret = getNext();
			idx++;
			return ret;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		protected int getIndex(final int pos) {
			if (permutative) {
				int currIdx = idx;
				int mod = params[pos].size();
				for (int i = pos + 1; i < params.length; i++) {
					currIdx /= params[i].size();
				}
				return currIdx % mod;
			} else {
				return idx;
			}
		}

		protected abstract T getNext();
	}
}
