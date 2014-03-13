package org.openntf.domino.tests.rpr.formula.eval;

import java.util.Iterator;

public class ParameterCollectionDouble extends ParameterCollectionAbstract<double[]> {

	public ParameterCollectionDouble(final ValueHolder[] params, final boolean permutative) {
		super(params, permutative);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Iterator<double[]> iterator() {
		return new ParameterIteratorDouble();
	}

	protected class ParameterIteratorDouble extends ParameterIteratorAbstract {
		double[] ret = new double[params.length];

		@Override
		protected double[] getNext() {
			for (int i = 0; i < ret.length; i++) {
				ret[i] = params[i].getDouble(getIndex(i));
			}
			return ret;
		}
	}

}
