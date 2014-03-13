package org.openntf.domino.tests.rpr.formula.eval;

import java.lang.reflect.Array;
import java.util.Iterator;

import org.openntf.domino.DateTime;

public class ParameterCollectionObject<T> extends ParameterCollectionAbstract<T[]> {
	protected Class<T> clazz;

	public ParameterCollectionObject(final ValueHolder[] params, final Class<T> clazz, final boolean permutative) {
		super(params, permutative);
		this.clazz = clazz;
	}

	@Override
	public Iterator<T[]> iterator() {
		return new ParameterIterator();
	}

	protected class ParameterIterator extends ParameterIteratorAbstract {
		@SuppressWarnings("unchecked")
		T[] ret = (T[]) Array.newInstance(clazz, params.length);

		@Override
		protected T[] getNext() {
			if (clazz.equals(String.class)) {
				for (int i = 0; i < ret.length; i++) {
					ret[i] = (T) params[i].getText(getIndex(i));
				}
			} else if (clazz.equals(DateTime.class)) {
				for (int i = 0; i < ret.length; i++) {
					ret[i] = (T) params[i].getDateTime(getIndex(i));
				}
			} else {
				for (int i = 0; i < ret.length; i++) {
					ret[i] = (T) params[i].get(getIndex(i));
				}
			}
			return ret;
		}
	}

}
