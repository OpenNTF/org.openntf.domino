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

import java.lang.reflect.Method;
import java.util.Collection;

import org.openntf.domino.formula.FormulaContext;
import org.openntf.domino.formula.ValueHolder;

/**
 * This class does multi value handling for you
 * 
 * @author Roland Praml, Foconis AG
 * 
 */
public class AtFunctionSimple extends AtFunctionGeneric {

	public AtFunctionSimple(final String image, final Method method) {
		super(image, method);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public ValueHolder evaluate(final FormulaContext ctx, final ValueHolder[] params) throws Exception {
		ValueHolder ret = null;
		Object result = null;
		if (varArgClass != null) {

			Collection<Object[]> values = new ParameterCollectionObject<Object>(params, (Class<Object>) varArgClass, false);

			// Our last parameter is a "varArg" this means, the LAST parameter is an array[]
			Object[] tmpParams = new Object[paramCount];
			for (Object[] value : values) {
				int i = 0;
				if (useContext) {
					tmpParams[i++] = ctx;
				}
				if (i == paramCount) {
					// that's not possible when useContext is true unless you specify FormulaContext... ctx
				} else if (i == paramCount - 1) {
					// exactly one parameter left. this is our vararg
					tmpParams[i++] = value;
				}
				result = method.invoke(null, tmpParams);
				if (result != null) {
					if (ret == null) {
						ret = ValueHolder.createValueHolder(result.getClass(), values.size());
					}
					ret.add(result);
				}
			}
		} else {
			Collection<Object[]> values = new ParameterCollectionObject<Object>(params, Object.class, false);

			for (Object[] value : values) {

				if (useContext) {
					Object[] tmpParams;
					if (value == null) {
						tmpParams = new Object[1];
					} else {
						tmpParams = new Object[value.length + 1];
						System.arraycopy(value, 0, tmpParams, 1, value.length);
					}
					tmpParams[0] = ctx;
					result = method.invoke(null, tmpParams);
				} else {
					result = method.invoke(null, value);
				}
				if (result != null) {
					if (ret == null) {
						ret = ValueHolder.createValueHolder(result.getClass(), values.size());
					}
					ret.add(result);
				}
			}
		}

		return ret;

	}

	@Override
	protected String getPrefix() {
		return method.getDeclaringClass().getSimpleName() + " [simple]";
	}
}
