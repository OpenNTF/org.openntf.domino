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
package org.openntf.formula.function;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.openntf.formula.DateTime;
import org.openntf.formula.FormulaContext;
import org.openntf.formula.Function;
import org.openntf.formula.FunctionSet;
import org.openntf.formula.ValueHolder;
import org.openntf.formula.impl.ParameterCollectionBoolean;
import org.openntf.formula.impl.ParameterCollectionDouble;
import org.openntf.formula.impl.ParameterCollectionInt;
import org.openntf.formula.impl.ParameterCollectionObject;

/**
 * This class implements the default arithmetic, boolean and compare operators.
 * 
 * As Operations are used at most, this is implemented to be (or to try to be) as fast as possible
 * 
 * @author Roland Praml, Foconis AG
 * 
 */
public class Comparators extends OperatorsAbstract {

	public static abstract class Matcher {
		public abstract boolean match(int delta);
	}

	/**
	 * The Factory that returns a set of operators
	 */
	public static class Functions extends FunctionSet {
		private static final Map<String, Function> functionSet = new HashMap<String, Function>(16);

		private static void add(final Function f) {
			functionSet.put(f.getImage().toLowerCase(), f);
		}

		@Override
		public Map<String, Function> getFunctions() {
			return functionSet;
		}

		static {

			Matcher cmpEq = new Matcher() {
				@Override
				public boolean match(final int delta) {
					return delta == 0;
				}
			};
			add(new Comparators(cmpEq, "="));
			add(new Comparators(cmpEq, "*="));

			Matcher cmpNe = new Matcher() {
				@Override
				public boolean match(final int delta) {
					return delta != 0;
				}
			};
			add(new Comparators(cmpNe, "<>"));
			add(new Comparators(cmpNe, "*<>"));

			Matcher cmpLt = new Matcher() {
				@Override
				public boolean match(final int delta) {
					return delta < 0;
				}
			};
			add(new Comparators(cmpLt, "<"));
			add(new Comparators(cmpLt, "*<"));

			Matcher cmpGt = new Matcher() {
				@Override
				public boolean match(final int delta) {
					return delta > 0;
				}
			};
			add(new Comparators(cmpGt, ">"));
			add(new Comparators(cmpGt, "*>"));

			Matcher cmpLte = new Matcher() {
				@Override
				public boolean match(final int delta) {
					return delta <= 0;
				}
			};
			add(new Comparators(cmpLte, "<="));
			add(new Comparators(cmpLte, "*<="));

			Matcher cmpGte = new Matcher() {
				@Override
				public boolean match(final int delta) {
					return delta >= 0;
				}
			};
			add(new Comparators(cmpGte, ">="));
			add(new Comparators(cmpGte, "*>="));
		}

	}

	private Matcher matcher;

	/**
	 * The constructor. Operators shoud be constructed via Operator.Factory
	 * 
	 * @param operation
	 * @param image
	 */
	private Comparators(final Matcher matcher, final String image) {
		super(image);
		this.matcher = matcher;
		// Autodetect if the operation is permutative
		this.isPermutative = (image.charAt(0) == '*' && image.length() > 1);
	}

	// ----------- Strings
	@Override
	protected ValueHolder evaluateString(final FormulaContext ctx, final ValueHolder[] params) {
		Collection<String[]> values = new ParameterCollectionObject<String>(params, String.class, isPermutative);
		for (String[] value : values) {
			int delta = value[0].compareToIgnoreCase(value[1]);
			if (matcher.match(delta)) {
				return ctx.TRUE;
			}
		}
		return ctx.FALSE;

	}

	@Override
	protected ValueHolder evaluateString(final FormulaContext ctx, final String s1, final String s2) {
		int delta = s1.compareToIgnoreCase(s2);
		if (matcher.match(delta)) {
			return ctx.TRUE;
		}
		return ctx.FALSE;
	}

	// ----------- Numbers
	@Override
	protected ValueHolder evaluateNumber(final FormulaContext ctx, final ValueHolder[] params) {
		Collection<double[]> values = new ParameterCollectionDouble(params, isPermutative);

		for (double[] value : values) {
			int delta = Double.compare(value[0], value[1]);
			if (matcher.match(delta)) {
				return ctx.TRUE;
			}
		}
		return ctx.FALSE;
	}

	@Override
	protected ValueHolder evaluateNumber(final FormulaContext ctx, final double d1, final double d2) {
		int delta = Double.compare(d1, d2);
		if (matcher.match(delta)) {
			return ctx.TRUE;
		}
		return ctx.FALSE;
	}

	// ----------- Integers
	@Override
	protected ValueHolder evaluateInt(final FormulaContext ctx, final ValueHolder[] params) {
		Collection<int[]> values = new ParameterCollectionInt(params, isPermutative);

		for (int[] value : values) {
			int delta = value[0] - value[1];
			if (matcher.match(delta)) {
				return ctx.TRUE;
			}
		}
		return ctx.FALSE;
	}

	@Override
	protected ValueHolder evaluateInt(final FormulaContext ctx, final int i1, final int i2) {
		int delta = i1 - i2;
		if (matcher.match(delta)) {
			return ctx.TRUE;
		}
		return ctx.FALSE;
	}

	// ----------- DateTimes
	@Override
	protected ValueHolder evaluateDateTime(final FormulaContext ctx, final ValueHolder[] params) {
		Collection<DateTime[]> values = new ParameterCollectionObject<DateTime>(params, DateTime.class, isPermutative);

		for (DateTime[] value : values) {
			int delta = value[0].compare(value[0], value[1]);
			if (matcher.match(delta)) {
				return ctx.TRUE;
			}
		}
		return ctx.FALSE;
	}

	@Override
	protected ValueHolder evaluateDateTime(final FormulaContext ctx, final DateTime dt1, final DateTime dt2) {
		int delta = dt1.compare(dt1, dt2);
		if (matcher.match(delta)) {
			return ctx.TRUE;
		}
		return ctx.FALSE;
	}

	// ----------- Integers
	@Override
	protected ValueHolder evaluateBoolean(final FormulaContext ctx, final ValueHolder[] params) {
		Collection<boolean[]> values = new ParameterCollectionBoolean(params, isPermutative);

		for (boolean[] value : values) {
			int delta = (value[0] ? 1 : 0) - (value[1] ? 1 : 0);
			if (matcher.match(delta)) {
				return ctx.TRUE;
			}
		}
		return ctx.FALSE;
	}

	@Override
	protected ValueHolder evaluateBoolean(final FormulaContext ctx, final boolean b1, final boolean b2) {
		int delta = (b1 ? 1 : 0) - (b2 ? 1 : 0);
		if (matcher.match(delta)) {
			return ctx.TRUE;
		}
		return ctx.FALSE;
	}
}