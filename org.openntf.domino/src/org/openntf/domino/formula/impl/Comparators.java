/*
 * © Copyright FOCONIS AG, 2014
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 * 
 */
package org.openntf.domino.formula.impl;

import java.util.Collection;

import org.openntf.domino.DateTime;
import org.openntf.domino.formula.AtFunctionFactory;
import org.openntf.domino.formula.FormulaContext;
import org.openntf.domino.formula.ValueHolder;

/**
 * This class implements the default arithmetic, boolean and compare operators.
 * 
 * As Operations are used at most, this is implemented to be (or to try to be) as fast as possible
 * 
 * @author Roland Praml, Foconis AG
 * 
 */
public class Comparators extends OperatorsAbstract {

	/**
	 * The Factory that returns a set of operators
	 */
	public static class Factory extends AtFunctionFactory {

		public Factory() {
			super();

			ComparatorImpl cmpEq = new ComparatorImpl() {
				@Override
				public boolean match(final int delta) {
					return delta == 0;
				}
			};

			ComparatorImpl cmpNe = new ComparatorImpl() {
				@Override
				public boolean match(final int delta) {
					return delta != 0;
				}
			};

			ComparatorImpl cmpLt = new ComparatorImpl() {
				@Override
				public boolean match(final int delta) {
					return delta < 0;
				}
			};
			ComparatorImpl cmpGt = new ComparatorImpl() {
				@Override
				public boolean match(final int delta) {
					return delta > 0;
				}
			};
			ComparatorImpl cmpLte = new ComparatorImpl() {
				@Override
				public boolean match(final int delta) {
					return delta <= 0;
				}
			};
			ComparatorImpl cmpGte = new ComparatorImpl() {
				@Override
				public boolean match(final int delta) {
					return delta >= 0;
				}
			};

			init(new Comparators(cmpEq, "="), 	//
					new Comparators(cmpEq, "*="), 	//
					new Comparators(cmpNe, "<>"), 	//
					new Comparators(cmpNe, "*<>"), 	//
					new Comparators(cmpLt, "<"), 	//
					new Comparators(cmpLt, "*<"), 	//
					new Comparators(cmpGt, ">"), 	//
					new Comparators(cmpGt, "*>"), 	//
					new Comparators(cmpLte, "<="), 	//
					new Comparators(cmpLte, "*<="),	//
					new Comparators(cmpGte, ">="), 	//
					new Comparators(cmpGte, "*>="));
		}
	}

	private ComparatorImpl computer;

	/**
	 * The constructor. Operators shoud be constructed via Operator.Factory
	 * 
	 * @param operation
	 * @param image
	 */
	private Comparators(final ComparatorImpl computer, final String image) {
		super(image);
		this.computer = computer;
		// Autodetect if the operation is permutative
		this.isPermutative = (image.charAt(0) == '*' && image.length() > 1);
	}

	// ----------- Strings
	@Override
	protected ValueHolder evaluateString(final FormulaContext ctx, final ValueHolder[] params) {
		Collection<String[]> values = new ParameterCollectionObject<String>(params, String.class, isPermutative);
		for (String[] value : values) {
			int delta = value[0].compareToIgnoreCase(value[1]);
			if (computer.match(delta)) {
				return ctx.TRUE;
			}
		}
		return ctx.FALSE;

	}

	@Override
	protected ValueHolder evaluateString(final FormulaContext ctx, final String s1, final String s2) {
		int delta = s1.compareToIgnoreCase(s2);
		if (computer.match(delta)) {
			return ctx.TRUE;
		}
		return ctx.FALSE;
	}

	// ----------- Numbers
	@Override
	protected ValueHolder evaluateNumber(final FormulaContext ctx, final ValueHolder[] params) {
		ValueHolder ret = new ValueHolder();
		Collection<double[]> values = new ParameterCollectionDouble(params, isPermutative);

		ret.grow(values.size());
		for (double[] value : values) {
			int delta = Double.compare(value[0], value[1]);
			if (computer.match(delta)) {
				return ctx.TRUE;
			}
		}
		return ctx.FALSE;
	}

	@Override
	protected ValueHolder evaluateNumber(final FormulaContext ctx, final double d1, final double d2) {
		int delta = Double.compare(d1, d2);
		if (computer.match(delta)) {
			return ctx.TRUE;
		}
		return ctx.FALSE;
	}

	// ----------- Integers
	@Override
	protected ValueHolder evaluateInt(final FormulaContext ctx, final ValueHolder[] params) {
		ValueHolder ret = new ValueHolder();
		Collection<int[]> values = new ParameterCollectionInt(params, isPermutative);

		ret.grow(values.size());
		for (int[] value : values) {
			int delta = value[0] - value[1];
			if (computer.match(delta)) {
				return ctx.TRUE;
			}
		}
		return ctx.FALSE;
	}

	@Override
	protected ValueHolder evaluateInt(final FormulaContext ctx, final int i1, final int i2) {
		int delta = i1 - i2;
		if (computer.match(delta)) {
			return ctx.TRUE;
		}
		return ctx.FALSE;
	}

	// ----------- DateTimes
	@Override
	protected ValueHolder evaluateDateTime(final FormulaContext ctx, final ValueHolder[] params) {
		ValueHolder ret = new ValueHolder();
		Collection<DateTime[]> values = new ParameterCollectionObject<DateTime>(params, DateTime.class, isPermutative);
		for (DateTime[] value : values) {
			int delta = value[0].compareTo(value[1]);
			if (computer.match(delta)) {
				return ctx.TRUE;
			}
		}
		return ctx.FALSE;
	}

	@Override
	protected ValueHolder evaluateDateTime(final FormulaContext ctx, final DateTime dt1, final DateTime dt2) {
		int delta = dt1.compareTo(dt2);
		if (computer.match(delta)) {
			return ctx.TRUE;
		}
		return ctx.FALSE;
	}

}