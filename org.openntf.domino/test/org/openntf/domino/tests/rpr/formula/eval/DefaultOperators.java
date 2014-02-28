package org.openntf.domino.tests.rpr.formula.eval;

import java.util.Collection;

public class DefaultOperators implements AtFunctionFactory, AtFunction {
	public static enum Op {
		// Additions
		ADD("_add", false, "+"), ADD_P("_addP", true, "*+"), SUB("_sub", false, "-"), SUB_P("_subP", true, "*-"),

		// Multiplications
		MUL("_mul", false, "*"), MUL_P("_mulP", true, "**"), DIV("_div", false, "/"), DIV_P("_divP", true, "*/"),

		// Unary ops
		NEGATIVE("_negative", false, " -"), NOT("_not", false, " !");

		String value;
		String image;
		boolean isPermutative;
		DefaultOperators instance; // cache

		Op(final String v, final boolean isPerm, final String img) {
			value = v;
			isPermutative = isPerm;
			image = img;
		}
	}

	public DefaultOperators() {
	}

	public AtFunction getFunction(final String funcName) {
		// TODO Auto-generated method stub
		if (funcName.charAt(0) == '_') {
			for (Op op : Op.values()) {
				if (op.value.equalsIgnoreCase(funcName)) {
					if (op.instance == null) {
						op.instance = new DefaultOperators(op);
					}
					return op.instance;
				}
			}
		}
		return null;
	}

	// ------------- This is for evaluation
	protected Op operation;

	public DefaultOperators(final Op op) {
		operation = op;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DefaultOperators [operation=" + operation + "]";
	}

	@SuppressWarnings("null")
	public ValueHolder evaluate(final FormulaContext ctx, final ValueHolder[] params) {

		Object o;
		o = params[0].get(0); // Fetch first element to determine operation

		if (o instanceof String) {
			return evaluateString(ctx, params);
		} else if (o instanceof Number) {
			return evaluateNumber(ctx, params);
		} else {
			return evaluateDateTime(ctx, params);
		}
	}

	private ValueHolder evaluateDateTime(final FormulaContext ctx, final ValueHolder[] params) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException(operation + " not supported for DateTimes");
	}

	private ValueHolder evaluateNumber(final FormulaContext ctx, final ValueHolder[] params) {
		ValueHolder ret = new ValueHolder();

		Collection<double[]> values = new ParameterCollectionDouble(params, operation.isPermutative);
		ret.grow(values.size());

		switch (operation) {
		case ADD:
		case ADD_P:
			for (double[] value : values) {
				ret.add(value[0] + value[1]);
			}
			return ret;
		case DIV:
		case DIV_P:
			for (double[] value : values) {
				ret.add(value[0] / value[1]);
			}
			return ret;
		case MUL:
		case MUL_P:
			for (double[] value : values) {
				ret.add(value[0] * value[1]);
			}
			return ret;
		case SUB:
		case SUB_P:

			for (double[] value : values) {
				ret.add(value[0] - value[1]);
			}
			return ret;

		case NEGATIVE:

			for (double[] value : values) {
				ret.add(-value[0]);
			}
			return ret;
		case NOT:

			for (double[] value : values) {
				ret.add(((int) value[0]) == 0);
			}
			return ret;

		}

		throw new UnsupportedOperationException(operation + " not supported for Numbers");
	}

	private ValueHolder evaluateString(final FormulaContext ctx, final ValueHolder[] params) {
		ValueHolder ret = new ValueHolder();

		switch (operation) {
		case ADD:
		case ADD_P:
			Collection<String[]> values = new ParameterCollectionObject<String>(params, String.class, operation.isPermutative);
			ret.grow(values.size());
			for (String[] value : values) {
				ret.add(value[0].concat(value[1]));
			}
			return ret;
		case DIV:
		case DIV_P:
		case MUL:
		case MUL_P:
		case SUB:
		case SUB_P:
			break;
		}

		throw new UnsupportedOperationException(operation + " not supported for Strings");
	}

	public String getImage() {
		return operation.image;
	}
}
