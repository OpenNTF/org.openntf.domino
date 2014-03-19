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

import java.util.Collection;

import org.openntf.domino.formula.ValueHolder;

public enum Arithmetic {
	;

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
		Collection<double[]> values = new ParameterCollectionDouble(params, false);

		if (params.length == 1) {
			double ret = Double.MIN_VALUE;
			for (double[] value : values) {
				ret = Math.max(ret, value[0]);
			}
			return new ValueHolder(ret);
		} else {
			ValueHolder ret = new ValueHolder();
			ret.grow(values.size());
			for (double[] value : values) {
				ret.add(Math.max(value[0], value[1]));
			}
			return ret;
		}
	}

	//... the same for Min
	@ParamCount({ 1, 2 })
	public static ValueHolder atMin(final ValueHolder[] params) {
		Collection<double[]> values = new ParameterCollectionDouble(params, false);

		if (params.length == 1) {
			double ret = Double.MAX_VALUE;
			for (double[] value : values) {
				ret = Math.min(ret, value[0]);
			}
			return new ValueHolder(ret);
		} else {
			ValueHolder ret = new ValueHolder();
			ret.grow(values.size());
			for (double[] value : values) {
				ret.add(Math.min(value[0], value[1]));
			}
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

	@ParamCount(0)
	public static ValueHolder atPi() {
		return new ValueHolder(Math.PI);
	}

	@ParamCount(2)
	public static Number atPower(final Number arg1, final Number arg2) {
		return Math.pow(arg1.doubleValue(), arg2.doubleValue());
	}

	@ParamCount(0)
	public static ValueHolder atRandom() {
		return new ValueHolder(Math.random());
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
			for (Object value : valueHolder) {
				ret += ((Number) value).doubleValue();
			}
		}
		return new ValueHolder(ret);
	}

	@ParamCount(1)
	public static Number atTan(final Number arg) {
		return Math.tan(arg.doubleValue());
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
