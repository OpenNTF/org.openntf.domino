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

import org.openntf.formula.DateTime;
import org.openntf.formula.FormulaContext;
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
public abstract class OperatorsAbstract extends AtFunction {

	protected boolean isPermutative;

	/**
	 * The constructor. Operators shoud be constructed via Operator.Factory
	 * 
	 */
	public OperatorsAbstract(final String image) {
		super(image);
		// Autodetect if the operation is permutative
		this.isPermutative = (image.charAt(0) == '*' && image.length() > 1);
	}

	/**
	 * Evaluates the operator
	 */
	public ValueHolder evaluate(final FormulaContext ctx, final ValueHolder[] params) {
		ValueHolder v1 = params[0]; // Fetch first element to determine operation
		ValueHolder v2 = params[1]; // Fetch first element to determine operation

		if (v1.dataType == DataType.ERROR) {
			return v1;
		}
		if (v2.dataType == DataType.ERROR) {
			return v2;
		}

		boolean multi = v1.size > 1 ? true : false;
		if (v2.size > 1)
			multi = true;

		switch (v1.dataType) {
		case DATETIME:
			if (v2.dataType == DataType.DATETIME) {
				if (multi)
					return evaluateDateTime(ctx, params);
				return evaluateDateTime(ctx, v1.getDateTime(0), v2.getDateTime(0));
			}
			break;

		case INTEGER:

			if (v2.dataType == DataType.INTEGER) {
				if (multi)
					return evaluateInt(ctx, params);
				return evaluateInt(ctx, v1.getInt(0), v2.getInt(0));
			}
			//do not break - fall through. It may be integer & double
		case DOUBLE:
			if (v2.dataType.numeric) {
				if (multi)
					return evaluateNumber(ctx, params);
				return evaluateNumber(ctx, v1.getDouble(0), v2.getDouble(0));
			}
			break;

		case STRING:
			if (v2.dataType == DataType.STRING) {
				if (multi)
					return evaluateString(ctx, params);
				return evaluateString(ctx, v1.getString(0), v2.getString(0));
			}
			break;

		case BOOLEAN:
			if (v2.dataType == DataType.BOOLEAN) {
				if (multi)
					return evaluateBoolean(ctx, params);
				return evaluateBoolean(ctx, v1.getBoolean(0), v2.getBoolean(0));
			}
			break;

		case _UNSET:
			break;
		case ERROR:
			break;
		default:
			break;

		}

		throw new IllegalArgumentException("Operation '" + v1.dataType + " " + getImage() + " " + v2.dataType + "' is not supported.");

	}

	protected abstract ValueHolder evaluateString(final FormulaContext ctx, final ValueHolder[] params);

	protected abstract ValueHolder evaluateString(final FormulaContext ctx, final String s1, final String s2);

	protected abstract ValueHolder evaluateNumber(final FormulaContext ctx, final ValueHolder[] params);

	protected abstract ValueHolder evaluateNumber(final FormulaContext ctx, final double d1, final double d2);

	protected abstract ValueHolder evaluateInt(final FormulaContext ctx, final ValueHolder[] params);

	protected abstract ValueHolder evaluateInt(final FormulaContext ctx, final int i1, final int i2);

	protected abstract ValueHolder evaluateDateTime(final FormulaContext ctx, final ValueHolder[] params);

	protected abstract ValueHolder evaluateDateTime(final FormulaContext ctx, final DateTime dt1, final DateTime dt2);

	protected abstract ValueHolder evaluateBoolean(final FormulaContext ctx, final ValueHolder[] params);

	protected abstract ValueHolder evaluateBoolean(final FormulaContext ctx, final boolean b1, final boolean b2);

	/* -------------------------------------------------- */
	public boolean checkParamCount(final int i) {
		// ensured by parser
		return true;
	}
}