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

import java.util.HashMap;
import java.util.Map;

import org.openntf.formula.FormulaContext;
import org.openntf.formula.Function;
import org.openntf.formula.FunctionSet;
import org.openntf.formula.ValueHolder;
import org.openntf.formula.ValueHolder.DataType;
import org.openntf.formula.impl.AtFunction;

/**
 * This class implements the default arithmetic, boolean and compare operators.
 * 
 * As Operations are used at most, this is implemented to be (or to try to be) as fast as possible
 * 
 * @author Roland Praml, Foconis AG
 * 
 */
@SuppressWarnings("nls")
public abstract class Negators extends AtFunction {

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
			add(new Negators(" -") { // the space is to distinguish to Op.SUB //$NON-NLS-1$

				@Override
				protected int compute(final FormulaContext ctx, final int i) {
					return -i;
				}

				@Override
				protected double compute(final FormulaContext ctx, final double d) {
					return -d;
				}

				@Override
				protected boolean compute(final FormulaContext ctx, final boolean b) {
					return !b;
				}
			});

			add(new Negators(" !") { //$NON-NLS-1$

				@Override
				protected int compute(final FormulaContext ctx, final int i) {
					return i == 0 ? 1 : 0;
				}

				@Override
				protected double compute(final FormulaContext ctx, final double d) {
					return (int) d == 0 ? 1.0D : 0.0D;
				}

				@Override
				protected boolean compute(final FormulaContext ctx, final boolean b) {
					return !b;
				}
			});
		}
	}

	/**
	 * The constructor. Operators shoud be constructed via Operator.Factory
	 * 
	 */
	public Negators(final String image) {
		super(image);
	}

	/**
	 * Evaluates the operator
	 */
	public ValueHolder evaluate(final FormulaContext ctx, final ValueHolder[] params) {
		ValueHolder v1 = params[0]; // Fetch first element to determine operation

		if (v1.dataType == DataType.ERROR) {
			return v1;
		}

		ValueHolder ret = v1.newInstance(v1.size);
		switch (v1.dataType) {

		case INTEGER:
			for (int i = 0; i < v1.size; i++) {
				ret.add(compute(ctx, v1.getInt(i)));
			}
			return ret;
		case BOOLEAN:
			for (int i = 0; i < v1.size; i++) {
				ret.add(compute(ctx, v1.getBoolean(i)));
			}
			return ret;

		case DOUBLE:
			for (int i = 0; i < v1.size; i++) {
				ret.add(compute(ctx, v1.getDouble(i)));
			}
			return ret;
		default:
			break;

		}

		throw new IllegalArgumentException("Operation '" + getImage() + " " + v1.dataType + "' is not supported.");

	}

	protected abstract boolean compute(FormulaContext ctx, boolean b);

	protected abstract int compute(FormulaContext ctx, int i);

	protected abstract double compute(FormulaContext ctx, double d);

	/* -------------------------------------------------- */
	public boolean checkParamCount(final int i) {
		// ensured by parser
		return true;
	}
}