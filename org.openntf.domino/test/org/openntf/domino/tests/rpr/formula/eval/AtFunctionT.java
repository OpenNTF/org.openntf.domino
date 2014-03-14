package org.openntf.domino.tests.rpr.formula.eval;

import java.text.ParseException;
import java.util.Collection;

import org.openntf.domino.DateTime;
import org.openntf.domino.Session;
import org.openntf.domino.formula.impl.ParameterCollectionDouble;
import org.openntf.domino.utils.Factory;

import com.ibm.commons.util.NotImplementedException;

public class AtFunctionT implements AtFunctionFactory, AtFunction {
	public static enum Op {
		TEMPLATE_VERSION("@TemplateVersion", 0, 0),	// TODO: Not yet implemented
		TEXT("@Text", 1, 2),						// TODO: Formatter not implemented
		TEXT_TO_NUMBER("@TextToNumber", 1, 1),		// OK
		TEXT_TO_TIME("@TextToTime", 1, 1),			// OK
		THIS_NAME("@ThisName", 0, 0),				// TODO: Not yet implemented
		THIS_VALUE("@ThisValue", 0, 0),				// TODO: Not yet implemented
		TIME("@Time", 1, 6)							// OK
		;

		String value;
		int minParam;
		int maxParam;
		AtFunctionT instance; // cache

		Op(final String v, final int i1, final int i2) {
			value = v;
			minParam = i1;
			maxParam = i2;
		}
	}

	public AtFunctionT() {
	}

	public AtFunction getFunction(final String funcName) {
		if (funcName.charAt(0) == '@') {
			if (funcName.charAt(1) == 't' || funcName.charAt(1) == 'T') {
				for (Op op : Op.values()) {
					if (op.value.equalsIgnoreCase(funcName)) {
						if (op.instance == null) {
							op.instance = new AtFunctionT(op);
						}
						return op.instance;
					}
				}
			}
		}
		return null;
	}

	// ------------- This is for evaluation
	protected Op operation;

	public AtFunctionT(final Op op) {
		operation = op;
	}

	public String getImage() {
		return operation.value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[operation=" + operation + "]";
	}

	public ValueHolder evaluate(final FormulaContext ctx, final ValueHolder[] params) throws ParseException {
		ValueHolder ret = new ValueHolder();
		Formatter fmt;

		switch (operation) {
		case TEMPLATE_VERSION:
			throw new NotImplementedException();

		case TEXT:
			return atText(ctx, params);

		case TEXT_TO_NUMBER:
			return atTextToNumber(ctx, params);

		case TEXT_TO_TIME:
			return atTextToTime(ctx, params);

		case THIS_NAME:
			throw new NotImplementedException();

		case THIS_VALUE:
			throw new NotImplementedException();

		case TIME:
			if (params.length == 1) {
				return atTime1(ctx, params);
			} else if (params.length == 6) {
				return atTime6(ctx, params);
			} else if (params.length >= 3) {
				return atTime3(ctx, params);
			}

		}

		throw new UnsupportedOperationException(operation + " not supported");
	}

	private ValueHolder atTime1(final FormulaContext ctx, final ValueHolder[] params) {
		ValueHolder ret = new ValueHolder();
		ret.grow(params[0].size());

		for (Object el : params[0]) {
			DateTime dtc = ((DateTime) el).clone();
			dtc.setAnyDate();
			ret.add(dtc);
		}
		return ret;
	}

	private ValueHolder atTime3(final FormulaContext ctx, final ValueHolder[] params) {
		ValueHolder ret = new ValueHolder();
		Collection<double[]> values = new ParameterCollectionDouble(params, false);
		ret.grow(values.size());
		Session s = Factory.getSession();
		for (double[] v : values) {
			DateTime dtc = s.createDateTime(2000, 6, 1, (int) v[0], (int) v[1], (int) v[2]);
			System.out.println("Vorher: " + dtc);
			dtc.setAnyDate();
			System.out.println("Nachher: " + dtc);
			ret.add(dtc);
		}
		return ret;
	}

	private ValueHolder atTime6(final FormulaContext ctx, final ValueHolder[] params) {
		ValueHolder ret = new ValueHolder();
		Collection<double[]> values = new ParameterCollectionDouble(params, false);
		ret.grow(values.size());
		Session s = Factory.getSession();
		for (double[] v : values) {
			DateTime dtc = s.createDateTime((int) v[0], (int) v[1], (int) v[2], (int) v[3], (int) v[4], (int) v[5]);
			ret.add(dtc);
		}
		return ret;
	}

	private ValueHolder atTextToTime(final FormulaContext ctx, final ValueHolder[] params) throws ParseException {
		ValueHolder ret = new ValueHolder();
		ret.grow(params[0].size());
		Formatter fmt = ctx.getFormatter();
		for (Object el : params[0]) {
			ret.add(fmt.parseDate((String) el));
		}
		return ret;
	}

	private ValueHolder atTextToNumber(final FormulaContext ctx, final ValueHolder[] params) throws ParseException {
		ValueHolder ret = new ValueHolder();
		ret.grow(params[0].size());
		Formatter fmt = ctx.getFormatter();
		for (Object el : params[0]) {
			ret.add(fmt.parseNumber((String) el));
		}
		return ret;
	}

	private ValueHolder atText(final FormulaContext ctx, final ValueHolder[] params) {
		ValueHolder ret = new ValueHolder();
		ret.grow(params[0].size());
		if (params.length == 1) {
			for (Object el : params[0]) {
				ret.add(el.toString());
			}
		} else {
			for (Object el : params[0]) {
				ret.add(toText(el, params[1]));
			}
		}
		return ret;
	}

	/**
	 * This method does the text conversion according to the Notes-help
	 * 
	 * @param el
	 * @param valueHolder
	 * @return
	 */
	private String toText(final Object el, final ValueHolder valueHolder) {
		// 		@Text with time­date components

		//		D0	Month, day and year
		//		D1	Month and day, year if it is not the current year
		//		D2	Month and day
		//		D3	Month and year
		//		T0	Hour, minute, and second
		//		T1	Hour and minute
		//		Z0	Always convert time to this zone
		//		Z1	Display zone only when it is not this zone
		//		Z2	Display zone always
		//		S0	Date only
		//		S1	Time only
		//		S2	Date and time
		//		S3	Date, time, Today, or Yesterday
		//		Sx 	Use when you cannot predict the exact format of the value being passed, but you know that it is either a time, a date, or both.

		// @Text with number values
		//		G	General format (significant digits only)
		//		F	Fixed format (set number of decimal places)
		//		S	Scientific format (E notation)
		//		C	Currency format (two decimal places)
		//		,	Punctuated at thousands (using U.S. format)
		//		%	Percentage format
		//		()	Parentheses around negative numbers
		//		number	Number of digits of precision

		String s = "";
		for (Object o : valueHolder) {
			s = s + (String) o;
		}

		throw new NotImplementedException();	// TODO 
		//return null;
	}

}
