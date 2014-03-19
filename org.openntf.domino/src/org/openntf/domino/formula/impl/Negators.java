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

import org.openntf.domino.formula.AtFunctionFactory;
import org.openntf.domino.formula.FormulaContext;
import org.openntf.domino.formula.ValueHolder;
import org.openntf.domino.formula.ValueHolder.DataType;

/**
 * This class implements the default arithmetic, boolean and compare operators.
 * 
 * As Operations are used at most, this is implemented to be (or to try to be) as fast as possible
 * 
 * @author Roland Praml, Foconis AG
 * 
 */
public abstract class Negators extends AtFunction {

	/**
	 * The Factory that returns a set of operators
	 */
	public static class Factory extends AtFunctionFactory {

		public Factory() {
			super();
			init(new Negators(" -") {

				@Override
				protected Object compute(final FormulaContext ctx, final int i) {
					// TODO Auto-generated method stub
					return -i;
				}

				@Override
				protected Object compute(final FormulaContext ctx, final double d) {
					// TODO Auto-generated method stub
					return -d;
				} // the space is to distinguish to Op.SUB
			}, new Negators(" !") {

				@Override
				protected Object compute(final FormulaContext ctx, final int i) {
					// TODO Auto-generated method stub
					return i == 0 ? ctx.TRUE : ctx.FALSE;
				}

				@Override
				protected Object compute(final FormulaContext ctx, final double d) {
					return (int) d == 0 ? ctx.TRUE : ctx.FALSE;
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
		ValueHolder ret = new ValueHolder();

		if (v1.dataType == DataType.ERROR) {
			return v1;
		}

		switch (v1.dataType) {

		case INTEGER:
			ret.grow(v1.size);
			for (int i = 0; i < v1.size; i++) {
				ret.add(compute(ctx, v1.getInt(i)));
			}
			return ret;
		case DOUBLE:
		case NUMBER:
			ret.grow(v1.size());
			for (int i = 0; i < v1.size; i++) {
				ret.add(compute(ctx, v1.getDouble(i)));
			}
			return ret;
		default:
			break;

		}

		throw new IllegalArgumentException("Operation '" + getImage() + " " + v1.dataType + "' is not supported.");

	}

	protected abstract Object compute(FormulaContext ctx, int i);

	protected abstract Object compute(FormulaContext ctx, double d);

	/* -------------------------------------------------- */
	public boolean checkParamCount(final int i) {
		// ensured by parser
		return true;
	}
}