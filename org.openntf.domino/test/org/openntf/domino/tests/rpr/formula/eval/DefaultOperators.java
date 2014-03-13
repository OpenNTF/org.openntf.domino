package org.openntf.domino.tests.rpr.formula.eval;

import java.util.Collection;

public class DefaultOperators implements AtFunctionFactory, AtFunction {
	public static enum Op {
		// Additions
		ADD("_add", false, "+"), ADD_P("_addP", true, "*+"), SUB("_sub", false, "-"), SUB_P("_subP", true, "*-"),

		// Multiplications
		MUL("_mul", false, "*"), MUL_P("_mulP", true, "**"), DIV("_div", false, "/"), DIV_P("_divP", true, "*/"),

		// Unary ops
		NEGATIVE("_negative", false, " -"), NOT("_not", false, " !"),

		// Equal/Not equal
		CMP_EQ("_cmpEq", false, "="), CMP_EQ_P("_cmpEqP", true, "*="), CMP_NE("_cmpNe", false, "!="), CMP_NE_P("_cmpNeP", true, "*<>"),

		// Comparators
		CMP_LT("_cmpLt", false, "<"), CMP_LT_P("_cmpLtP", true, "*<"), CMP_GT("_cmpGt", false, ">"), CMP_GT_P("_cmpGtP", true, "*>"),

		// and more comparators
		CMP_LTE("_cmpLte", false, "<="), CMP_LTE_P("_cmpLteP", true, "*<="), CMP_GTE("_cmpGte", false, ">="), CMP_GTE_P("_cmpGteP", true,
				"*>=");

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

		switch (operation) {
		case ADD:
		case ADD_P:
			ret.grow(values.size());
			for (double[] value : values) {
				ret.add(value[0] + value[1]);
			}
			return ret;
		case DIV:
		case DIV_P:
			ret.grow(values.size());
			for (double[] value : values) {
				ret.add(value[0] / value[1]);
			}
			return ret;
		case MUL:
		case MUL_P:
			ret.grow(values.size());
			for (double[] value : values) {
				ret.add(value[0] * value[1]);
			}
			return ret;
		case SUB:
		case SUB_P:
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
				ret.add(((int) value[0]) == 0);
			}
			return ret;
		case CMP_EQ:
		case CMP_EQ_P:
			return new ValueHolder(compare(values, 0));

		case CMP_GT:
		case CMP_GT_P:
			return new ValueHolder(compare(values, 1));

		case CMP_GTE:
		case CMP_GTE_P:
			return new ValueHolder(compare(values, 1) == 1 ? 1 : compare(values, 0));

		case CMP_LT:
		case CMP_LT_P:
			return new ValueHolder(compare(values, -1));

		case CMP_LTE:
		case CMP_LTE_P:
			return new ValueHolder(compare(values, -1) == 1 ? 1 : compare(values, 0));

		case CMP_NE:
		case CMP_NE_P:
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
