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
import org.openntf.formula.impl.IntegerOverflowException;
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
public class Operators extends OperatorsAbstract {

	public static abstract class Computer {
		private String image;

		public Computer(final String im) {
			image = im;
		}

		public double compute(final double v1, final double v2) {
			throw new UnsupportedOperationException("'" + image + "' is not supported for DOUBLE");
		}

		public boolean compute(final boolean b1, final boolean b2) {
			throw new UnsupportedOperationException("'" + image + "' is not supported for BOOLEAN");
		}

		public int compute(final int v1, final int v2) throws IntegerOverflowException {
			throw new UnsupportedOperationException("'" + image + "' is not supported for INTEGER");
		}

		public String compute(final String v1, final String v2) {
			throw new UnsupportedOperationException("'" + image + "' is not supported for STRING");
		}

		public DateTime compute(final DateTime d1, final DateTime d2) {
			throw new UnsupportedOperationException("'" + image + "' is not supported for DATETIME");
		}

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

			// Define the computers
			Computer add = new Computer("+") {

				@Override
				public int compute(final int v1, final int v2) throws IntegerOverflowException {
					long res = ((long) v1 + (long) v2);
					if (res < Integer.MIN_VALUE || Integer.MAX_VALUE < res) {
						throw new IntegerOverflowException();
					}
					return (int) res;
				}

				@Override
				public double compute(final double v1, final double v2) {
					return v1 + v2;
				}

				@Override
				public String compute(final String v1, final String v2) {
					return v1.concat(v2);
				}
			};

			Computer sub = new Computer("-") {

				@Override
				public int compute(final int v1, final int v2) throws IntegerOverflowException {
					long res = ((long) v1 - (long) v2);
					if (res < Integer.MIN_VALUE || Integer.MAX_VALUE < res) {
						throw new IntegerOverflowException();
					}
					return (int) res;
				}

				@Override
				public double compute(final double v1, final double v2) {
					return v1 - v2;
				}
			};

			Computer mul = new Computer("*") {

				@Override
				public int compute(final int v1, final int v2) throws IntegerOverflowException {
					long res = ((long) v1 * (long) v2);
					if (res < Integer.MIN_VALUE || Integer.MAX_VALUE < res) {
						throw new IntegerOverflowException();
					}
					return (int) res;
				}

				@Override
				public double compute(final double v1, final double v2) {
					return v1 * v2;
				}

			};

			Computer div = new Computer("/") {

				@Override
				public int compute(final int v1, final int v2) throws IntegerOverflowException {
					if (v1 % v2 != 0)
						throw new IntegerOverflowException();
					return v1 / v2;
				}

				@Override
				public double compute(final double v1, final double v2) {
					return v1 / v2;
				}

			};

			add(new Operators(add, "+"));
			add(new Operators(add, "*+"));
			add(new Operators(sub, "-"));
			add(new Operators(sub, "*-"));
			add(new Operators(mul, "*"));
			add(new Operators(mul, "**"));
			add(new Operators(div, "/"));
			add(new Operators(div, "*/"));
		}
	}

	private Computer computer;

	/**
	 * The constructor. Operators shoud be constructed via Operator.Factory
	 * 
	 * @param operation
	 * @param image
	 */
	private Operators(final Computer computer, final String image) {
		super(image);
		this.computer = computer;
		// Autodetect if the operation is permutative
		this.isPermutative = (image.charAt(0) == '*' && image.length() > 1);
	}

	// ----------- Strings
	@Override
	protected ValueHolder evaluateString(final FormulaContext ctx, final ValueHolder[] params) {
		Collection<String[]> values = new ParameterCollectionObject<String>(params, String.class, isPermutative);
		ValueHolder ret = ValueHolder.createValueHolder(String.class, values.size());
		for (String[] value : values) {
			ret.add(computer.compute(value[0], value[1]));
		}
		return ret;

	}

	@Override
	protected ValueHolder evaluateString(final FormulaContext ctx, final String s1, final String s2) {
		return ValueHolder.valueOf(computer.compute(s1, s2));
	}

	// ----------- Numbers
	@Override
	protected ValueHolder evaluateNumber(final FormulaContext ctx, final ValueHolder[] params) {

		Collection<double[]> values = new ParameterCollectionDouble(params, isPermutative);
		ValueHolder ret = ValueHolder.createValueHolder(double.class, values.size());

		for (double[] value : values) {
			ret.add(computer.compute(value[0], value[1]));
		}

		return ret;
	}

	@Override
	protected ValueHolder evaluateNumber(final FormulaContext ctx, final double d1, final double d2) {
		return ValueHolder.valueOf(computer.compute(d1, d2));
	}

	// ----------- Integers
	@Override
	protected ValueHolder evaluateInt(final FormulaContext ctx, final ValueHolder[] params) {

		Collection<int[]> values = new ParameterCollectionInt(params, isPermutative);
		ValueHolder ret = ValueHolder.createValueHolder(int.class, values.size());

		for (int[] value : values) {
			try {
				ret.add(computer.compute(value[0], value[1]));
			} catch (IntegerOverflowException e) {
				ret.add(computer.compute((double) value[0], (double) value[1]));
			}
		}
		return ret;
	}

	@Override
	protected ValueHolder evaluateInt(final FormulaContext ctx, final int i1, final int i2) {
		try {
			return ValueHolder.valueOf(computer.compute(i1, i2));
		} catch (IntegerOverflowException e) {
			return ValueHolder.valueOf(computer.compute((double) i1, (double) i2));
		}
	}

	// ----------- DateTimes
	@Override
	protected ValueHolder evaluateDateTime(final FormulaContext ctx, final ValueHolder[] params) {
		Collection<DateTime[]> values = new ParameterCollectionObject<DateTime>(params, DateTime.class, isPermutative);
		ValueHolder ret = ValueHolder.createValueHolder(DateTime.class, values.size());

		for (DateTime[] value : values) {
			ret.add(computer.compute(value[0], value[1]));
		}
		return ret;
	}

	@Override
	protected ValueHolder evaluateDateTime(final FormulaContext ctx, final DateTime dt1, final DateTime dt2) {
		return ValueHolder.valueOf(computer.compute(dt1, dt2));
	}

	// ----------- Numbers
	@Override
	protected ValueHolder evaluateBoolean(final FormulaContext ctx, final ValueHolder[] params) {

		Collection<boolean[]> values = new ParameterCollectionBoolean(params, isPermutative);
		ValueHolder ret = ValueHolder.createValueHolder(boolean.class, values.size());

		for (boolean[] value : values) {
			ret.add(computer.compute(value[0], value[1]));
		}

		return ret;
	}

	@Override
	protected ValueHolder evaluateBoolean(final FormulaContext ctx, final boolean b1, final boolean b2) {
		return ValueHolder.valueOf(computer.compute(b1, b2));
	}

}