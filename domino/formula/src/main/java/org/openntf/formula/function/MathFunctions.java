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

import java.util.Map;

import org.openntf.formula.Function;
import org.openntf.formula.FunctionFactory;
import org.openntf.formula.FunctionSet;
import org.openntf.formula.ValueHolder;
import org.openntf.formula.annotation.OpenNTF;
import org.openntf.formula.annotation.ParamCount;
import org.openntf.formula.impl.ParameterCollectionDouble;

public enum MathFunctions {
	;

	public static class Functions extends FunctionSet {
		private static final Map<String, Function> functionSet = FunctionFactory.getFunctions(MathFunctions.class);

		@Override
		public Map<String, Function> getFunctions() {
			return functionSet;
		}
	}

	@ParamCount(1)
	public static Number atAbs(final Number arg) {
		return Math.abs(arg.doubleValue());
	}

	@ParamCount(1)
	public static Number atACos(final Number arg) {
		return Math.acos(arg.doubleValue());
	}

	@ParamCount(1)
	public static Number atASin(final Number arg) {
		return Math.asin(arg.doubleValue());
	}

	@ParamCount(1)
	public static Number atATan(final Number arg) {
		return Math.atan(arg.doubleValue());
	}

	@ParamCount(2)
	public static Number atATan2(final Number arg1, final Number arg2) {
		// pay attention. Formula language expects the arguments in different order
		return Math.atan2(arg2.doubleValue(), arg1.doubleValue());
	}

	@ParamCount(1)
	public static Number atCos(final Number arg) {
		return Math.cos(arg.doubleValue());
	}

	@ParamCount(1)
	public static Number atExp(final Number arg) {
		return Math.exp(arg.doubleValue());
	}

	@ParamCount({ 2, 3 })
	public static Number atFloatEq(final Number... args) {
		double epsilon = (args.length == 3) ? args[2].doubleValue() : 0.0001;
		return Math.abs(args[0].doubleValue() - args[1].doubleValue()) < epsilon ? 1 : 0;
	}

	@ParamCount(1)
	public static Number atInteger(final Number arg) {
		return arg.intValue();
	}

	@ParamCount(1)
	public static Number atLog(final Number arg) {
		return Math.log10(arg.doubleValue());
	}

	@ParamCount(1)
	public static Number atLn(final Number arg) {
		return Math.log(arg.doubleValue());
	}

	// Max returns either the largest number in a single list, or the larger of two numbers or number lists.
	@ParamCount({ 1, 2 })
	public static ValueHolder atMax(final ValueHolder[] params) {

		if (params.length == 1) {
			ValueHolder vh = params[0];
			double ret = Double.MIN_VALUE;
			for (int i = 0; i < vh.size; i++) {
				ret = Math.max(ret, vh.getDouble(i));
			}
			return ValueHolder.valueOf(ret);
		} else if (ValueHolder.hasMultiValues(params)) {

			ParameterCollectionDouble values = new ParameterCollectionDouble(params, false);
			ValueHolder ret = ValueHolder.createValueHolder(double.class, values.size());
			for (double[] value : values) {
				ret.add(Math.max(value[0], value[1]));
			}
			return ret;

		} else {
			ValueHolder ret = ValueHolder.createValueHolder(double.class, 1);
			ret.add(Math.max(params[0].getDouble(0), params[1].getDouble(0)));
			return ret;

		}
	}

	//... the same for Min
	@ParamCount({ 1, 2 })
	public static ValueHolder atMin(final ValueHolder[] params) {

		if (params.length == 1) {
			double ret = Double.MAX_VALUE;
			ValueHolder vh = params[0];
			for (int i = 0; i < vh.size; i++) {
				ret = Math.min(ret, vh.getDouble(i));
			}
			return ValueHolder.valueOf(ret);
		} else if (ValueHolder.hasMultiValues(params)) {
			ParameterCollectionDouble values = new ParameterCollectionDouble(params, false);

			ValueHolder ret = ValueHolder.createValueHolder(double.class, values.size());
			for (double[] value : values) {
				ret.add(Math.min(value[0], value[1]));
			}
			return ret;

		} else {

			ValueHolder ret = ValueHolder.createValueHolder(double.class, 1);
			ret.add(Math.min(params[0].getDouble(0), params[1].getDouble(0)));
			return ret;
		}
	}

	@ParamCount(2)
	public static Number atModulo(final Number arg1, final Number arg2) {
		// how I would do it
		// return arg1.longValue() % arg2.longValue();

		// how notes does it
		double divres = arg1.doubleValue() / arg2.doubleValue();
		if (Double.isInfinite(divres))
			return divres;

		return (long) (arg1.doubleValue() - (long) divres * arg2.doubleValue());
	}

	private static ValueHolder PI = ValueHolder.valueOf(Math.PI);

	@ParamCount(0)
	public static ValueHolder atPi() {
		return PI;
	}

	@ParamCount(2)
	public static Number atPower(final Number arg1, final Number arg2) {
		return Math.pow(arg1.doubleValue(), arg2.doubleValue());
	}

	@ParamCount(0)
	public static ValueHolder atRandom() {
		return ValueHolder.valueOf(Math.random());
	}

	@ParamCount(1)
	public static Number atRound(final Number arg) {
		return Math.round(arg.doubleValue());
	}

	@ParamCount(1)
	public static Number atSin(final Number arg) {
		return Math.sin(arg.doubleValue());
	}

	@ParamCount(1)
	public static Number atSqrt(final Number arg) {
		return Math.sqrt(arg.doubleValue());
	}

	@ParamCount({ 1, Integer.MAX_VALUE })
	public static ValueHolder atSum(final ValueHolder[] params) {
		double ret = 0;

		for (ValueHolder valueHolder : params) {
			for (int i = 0; i < valueHolder.size; i++) {
				ret += valueHolder.getDouble(i);
			}
		}
		return ValueHolder.valueOf(ret);
	}

	@ParamCount(1)
	public static Number atTan(final Number arg) {
		return Math.tan(arg.doubleValue());
	}

	@ParamCount(1)
	public static Number atSign(final Number arg) {
		int i = (arg instanceof Integer) ? arg.intValue() : Double.compare(arg.doubleValue(), 0.0);
		return (i == 0) ? 0 : (i < 0) ? -1 : 1;
	}

	// ===========================================
	// These functions are NOT supported by Lotus:

	/* (non-Javadoc)
	 * @see java.lang.Math.cbrt
	 */
	@ParamCount(1)
	@OpenNTF
	public static Number atCbrt(final Number arg) {
		return Math.cbrt(arg.doubleValue());
	}

	@ParamCount(1)
	@OpenNTF
	public static Number atCeil(final Number arg) {
		return Math.ceil(arg.doubleValue());
	}

	@ParamCount(1)
	@OpenNTF
	public static Number atFloor(final Number arg) {
		return Math.floor(arg.doubleValue());
	}

	// TODO: Implement the complete Math Functions
}
