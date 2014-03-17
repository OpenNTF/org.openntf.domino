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

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.openntf.domino.DateTime;
import org.openntf.domino.formula.AtFunctionFactory;
import org.openntf.domino.formula.FormulaContext;
import org.openntf.domino.formula.ValueHolder;

/**
 * This class implements the default arithmetic, boolean and compare operators
 * 
 * @author Roland Praml, Foconis AG
 * 
 */
public class Operator extends AtFunction {
	/**
	 * Enum for the factory to make some things easier
	 */
	public enum Op {
		// Additions
		ADD, SUB, MUL, DIV,

		// Unary ops
		NEGATIVE, NOT,

		// Equal/Not equal
		CMP_EQ, CMP_NE, CMP_LT, CMP_GT, CMP_LTE, CMP_GTE,

	}

	/**
	 * The Factory that returns a set of operators
	 */
	public static class Factory extends AtFunctionFactory {

		public Factory() {
			super();
			init(new Operator(Op.ADD, "+"), 		// 
					new Operator(Op.ADD, "*+"), 	//
					new Operator(Op.SUB, "-"), 		//
					new Operator(Op.SUB, "*-"), 	//
					new Operator(Op.MUL, "*"), 		//
					new Operator(Op.MUL, "**"), 	//
					new Operator(Op.DIV, "/"), 		//
					new Operator(Op.DIV, "*/"), 	//
					new Operator(Op.NEGATIVE, " -"),// the space is to distinguish to Op.SUB
					new Operator(Op.NOT, " !"), 	//

					new Operator(Op.CMP_EQ, "="), 	//
					new Operator(Op.CMP_EQ, "*="), 	//
					new Operator(Op.CMP_NE, "<>"), 	//
					new Operator(Op.CMP_NE, "*<>"), //
					new Operator(Op.CMP_LT, "<"), 	//
					new Operator(Op.CMP_LT, "*<"), 	//
					new Operator(Op.CMP_GT, ">"), 	//
					new Operator(Op.CMP_GT, "*>"), 	//
					new Operator(Op.CMP_LTE, "<="), //
					new Operator(Op.CMP_LTE, "*<="),//
					new Operator(Op.CMP_GTE, ">="), //
					new Operator(Op.CMP_GTE, "*>="));
		}
	}

	/** is this operation permutative? */
	private boolean isPermutative = false;

	/** the operation itself */
	private Op operation;

	/**
	 * The constructor. Operators shoud be constructed via Operator.Factory
	 * 
	 * @param operation
	 * @param image
	 */
	private Operator(final Op operation, final String image) {
		super(image);
		this.operation = operation;
		// Autodetect if the operation is permutative
		this.isPermutative = (image.charAt(0) == '*' && image.length() > 1);
	}

	/**
	 * Evaluates the operator
	 */
	public ValueHolder evaluate(final FormulaContext ctx, final ValueHolder[] params) {
		Object o;
		o = params[0].get(0); // Fetch first element to determine operation
		// TODO: Check Null + Exception!

		if (o instanceof String) {
			return evaluateString(ctx, params);
		} else if (o instanceof Number) {
			return evaluateNumber(ctx, params);
		} else if (o instanceof DateTime) {
			return evaluateDateTime(ctx, params);
		} else if (o instanceof Date) {
			return evaluateDate(ctx, params);

		} else if (o instanceof Calendar) {
			return evaluateCalendar(ctx, params);
		} else {
			throw new IllegalArgumentException(o.getClass().getName() + " is not a supported datatype.");
		}
	}

	// DateTime/Date/Calendar computation is completely missing... doing this later
	private ValueHolder evaluateDateTime(final FormulaContext ctx, final ValueHolder[] params) {
		// TODO
		throw new UnsupportedOperationException(operation + " not supported for DateTimes");
	}

	private ValueHolder evaluateDate(final FormulaContext ctx, final ValueHolder[] params) {
		// TODO
		throw new UnsupportedOperationException(operation + " not supported for Date");
	}

	private ValueHolder evaluateCalendar(final FormulaContext ctx, final ValueHolder[] params) {
		// TODO
		throw new UnsupportedOperationException(operation + " not supported for Calendar");
	}

	/**
	 * Does a numeric computation
	 */
	private ValueHolder evaluateNumber(final FormulaContext ctx, final ValueHolder[] params) {
		ValueHolder ret = new ValueHolder();
		Collection<double[]> values = new ParameterCollectionDouble(params, isPermutative);

		switch (operation) {
		case ADD:
			ret.grow(values.size());
			for (double[] value : values) {
				ret.add(value[0] + value[1]);
			}
			return ret;
		case DIV:
			ret.grow(values.size());
			for (double[] value : values) {
				ret.add(value[0] / value[1]);
			}
			return ret;
		case MUL:
			ret.grow(values.size());
			for (double[] value : values) {
				ret.add(value[0] * value[1]);
			}
			return ret;
		case SUB:
			ret.grow(values.size());
			for (double[] value : values) {
				ret.add(value[0] - value[1]);
			}
			return ret;

		case NEGATIVE:
			ret.grow(values.size());
			for (double[] value : values) {
				ret.add(-value[0]);
			}
			return ret;
		case NOT:
			ret.grow(values.size());
			for (double[] value : values) {
				if (Double.compare(value[0], 0) == 0) {
					ret.add(1);
				} else {
					ret.add(0);
				}
			}
			return ret;
		case CMP_EQ:
			return new ValueHolder(compare(values, 0));

		case CMP_GT:
			return new ValueHolder(compare(values, 1));

		case CMP_GTE:
			return new ValueHolder(compare(values, 1) == 1 ? 1 : compare(values, 0));

		case CMP_LT:
			return new ValueHolder(compare(values, -1));

		case CMP_LTE:
			return new ValueHolder(compare(values, -1) == 1 ? 1 : compare(values, 0));

		case CMP_NE:
			return new ValueHolder(compare(values, -1) == 1 ? 1 : compare(values, 1));

		}

		throw new UnsupportedOperationException(operation + " not supported for Numbers");
	}

	private int compare(final Collection<double[]> values, final int ref) {
		// TODO: Check this for numeric stability!
		for (double[] value : values) {
			if (Double.compare(value[0], value[1]) == ref) {
				return 1;
			}
		}
		return 0;
	}

	private int compareString(final Collection<String[]> values, final int ref) {
		// TODO: Check this for numeric stability!
		for (String[] value : values) {
			if (Math.signum(value[0].compareTo(value[1])) == ref) {
				return 1;
			}
		}
		return 0;
	}

	private ValueHolder evaluateString(final FormulaContext ctx, final ValueHolder[] params) {
		ValueHolder ret = new ValueHolder();
		Collection<String[]> values = new ParameterCollectionObject<String>(params, String.class, isPermutative);

		switch (operation) {
		case ADD:
			ret.grow(values.size());
			for (String[] value : values) {
				ret.add(value[0].concat(value[1]));
			}
			return ret;

		case CMP_EQ:
			return new ValueHolder(compareString(values, 0));

		case CMP_GT:
			return new ValueHolder(compareString(values, 1));

		case CMP_GTE:
			return new ValueHolder(compareString(values, 1) == 1 ? 1 : compareString(values, 0));

		case CMP_LT:
			return new ValueHolder(compareString(values, -1));

		case CMP_LTE:
			return new ValueHolder(compareString(values, -1) == 1 ? 1 : compareString(values, 0));

		case CMP_NE:
			return new ValueHolder(compareString(values, -1) == 1 ? 1 : compareString(values, 1));

			// these are not supported on string
		case DIV:
		case MUL:
		case SUB:
		case NEGATIVE:
		case NOT:
			break;
		}

		throw new UnsupportedOperationException(operation + " not supported for Strings");
	}

	public boolean checkParamCount(final int i) {
		// ensured by parser
		return true;
	}

}