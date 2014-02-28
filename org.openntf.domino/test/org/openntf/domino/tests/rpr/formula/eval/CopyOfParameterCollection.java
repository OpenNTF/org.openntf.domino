package org.openntf.domino.tests.rpr.formula.eval;

import java.lang.reflect.Array;
import java.util.AbstractCollection;
import java.util.Iterator;

public class ParameterCollectionInt extends AbstractCollection<int[]> {

	private ValueHolder[] params;
	private Class<T> clazz;
	private boolean permutative;
	private int size;

	public ParameterCollection(final ValueHolder[] params, final Class<T> clazz, final boolean permutative) {
		// TODO Auto-generated constructor stub
		this.params = params;
		this.clazz = clazz;
		this.permutative = permutative;
		this.size = params[0].size();
		if (permutative) {
			for (int i = 1; i < params.length; i++) {
				this.size *= params[0].size();
			}
		} else {
			for (int i = 1; i < params.length; i++) {
				this.size = Math.max(this.size, params[0].size());
			}
		}
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Iterator<T[]> iterator() {
		return new ParameterIterator();
	}

	class ParameterIterator implements Iterator<T[]> {
		// Due performance reasons, the "ret" value is reused!
		// this does not matter
		T[] ret;
		int idx = 0;

		ParameterIterator() {
			ret = (T[]) Array.newInstance(clazz, size);
		}

		public boolean hasNext() {
			// TODO Auto-generated method stub
			return idx < size;
		}

		public T[] next() {
			// TODO Auto-generated method stub
			if (permutative) {
				throw new UnsupportedOperationException();
			} else {
				if (Integer.class.isAssignableFrom(clazz)) {
					for (int i = 0; i < ret.length; i++) {
						ret[i] = (T) params[i].getInt(i);
					}
				} else {
				}
				idx++;
			}
			return ret;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
