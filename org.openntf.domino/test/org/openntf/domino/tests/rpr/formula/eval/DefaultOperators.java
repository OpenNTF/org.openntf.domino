package org.openntf.domino.tests.rpr.formula.eval;

import java.util.Date;

public class DefaultOperators implements AtFunctionFactory, AtFunction {
	public static enum Op {
		ADD("_add"), ADD_P("_addP"), SUB("_sub"), SUB_P("_subP");

		String value;
		DefaultOperators instance;

		Op(final String v) {
			value = v;
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
	public Value evaluate(final FormulaContext ctx, final Value[] params) {
		Value ret = new Value();

		// left value & right value
		Value lv = null;
		Value rv = null;
		int l = 0;
		int r = 0;
		if (params.length == 2) {
			lv = params[0];
			rv = params[1];
			l = lv.size();
			r = rv.size();
		} else if (params.length == 1) {
			lv = params[0];
			l = lv.size();
		}

		int ub = Math.max(l, r);
		Object o;

		switch (operation) {
		case ADD:
			o = lv.get(0);
			ret.incSize(ub);
			if (o instanceof String) {
				for (int i = 0; i < ub; i++) {
					ret.append(lv.getText(i).concat(rv.getText(i)));
				}
			} else if (o instanceof Number) {
				for (int i = 0; i < ub; i++) {
					ret.append(lv.getDouble(i) + rv.getDouble(i));
				}
			} else {
				throw new IllegalArgumentException();
			}
			break;

		case ADD_P:
			o = lv.get(0);
			ret.incSize(l * r);
			if (o instanceof String) {
				for (int i = 0; i < l; i++) {
					for (int j = 0; j < r; j++) {
						ret.append(lv.getText(i).concat(rv.getText(j)));
					}
				}
			} else if (o instanceof Number) {
				for (int i = 0; i < l; i++) {
					for (int j = 0; j < r; j++) {
						ret.append(lv.getDouble(i) + rv.getDouble(j));
					}
				}
			} else {
				throw new IllegalArgumentException();
			}
			break;
		case SUB:
			o = lv.get(0);
			ret.incSize(ub);
			if (o instanceof Date) {
				for (int i = 0; i < ub; i++) {
					// TODO: [31.3.2014 00:00]-[30.03.2014 00:00] vs. [31.3.2014]-[30.03.2014]
					ret.append((lv.getDate(i).getTime() - rv.getDate(i).getTime()) / 1000);
				}
			} else if (o instanceof Number) {
				for (int i = 0; i < ub; i++) {
					ret.append(lv.getDouble(i) + rv.getDouble(i));
				}
			} else {
				throw new IllegalArgumentException();
			}
			break;
		case SUB_P:
			break;
		default:
			break;

		}
		return ret;
	}

}
