package org.openntf.domino.tests.rpr.formula.eval;

import java.util.Iterator;

public class ParameterCollectionInt extends ParameterCollectionAbstract<int[]> {

	public ParameterCollectionInt(final ValueHolder[] params, final boolean permutative) {
		super(params, permutative);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Iterator<int[]> iterator() {
		return new ParameterIteratorInt();
	}

	protected class ParameterIteratorInt extends ParameterIteratorAbstract {
		int[] ret = new int[params.length];

		@Override
		protected int[] getNext() {
			// TODO Auto-generated method stub
			for (int i = 0; i < ret.length; i++) {
				ret[i] = params[i].getInt(getIndex(i));
			}
			return ret;
		}
	}

}
