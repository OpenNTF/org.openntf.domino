package org.openntf.domino.tests.rpr.formula.eval;


public class StringOperators implements AtFunctionFactory, AtFunction {
	public static enum Op {
		// Additions
		TEXT("@Text", 1, 2);

		String value;
		int minParam;
		int maxParam;
		StringOperators instance; // cache

		Op(final String v, final int i1, final int i2) {
			value = v;
			minParam = i1;
			maxParam = i2;
		}
	}

	public StringOperators() {
	}

	public AtFunction getFunction(final String funcName) {

		if (funcName.charAt(0) == '@') {
			for (Op op : Op.values()) {
				if (op.value.equalsIgnoreCase(funcName)) {
					if (op.instance == null) {
						op.instance = new StringOperators(op);
					}
					return op.instance;
				}
			}
		}
		return null;
	}

	// ------------- This is for evaluation
	protected Op operation;

	public StringOperators(final Op op) {
		operation = op;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StringOperators [operation=" + operation + "]";
	}

	@SuppressWarnings("null")
	public ValueHolder evaluate(final FormulaContext ctx, final ValueHolder[] params) {
		ValueHolder ret = new ValueHolder();

		switch (operation) {
		case TEXT:
			ret.grow(params[0].size());

			for (Object el : params[0]) {
				ret.add(el.toString());
			}
			return ret;
		}

		throw new UnsupportedOperationException(operation + " not supported");
	}

	public String getImage() {
		return operation.value;
	}
}
