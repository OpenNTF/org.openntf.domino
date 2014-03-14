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

	@Override
	public ValueHolder evaluate(final FormulaContext ctx, final ValueHolder[] params) throws Exception {
		ValueHolder ret = new ValueHolder();

		if (varArgClass != null) {
			System.out.println(varArgClass);
			Collection<Object[]> values = new ParameterCollectionObject<Object>(params, (Class<Object>) varArgClass, false);
			ret.grow(values.size());

			// this means, the LAST parameter is an array[]
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
				ret.add(method.invoke(null, tmpParams));
			}
		} else {
			Collection<Object[]> values = new ParameterCollectionObject<Object>(params, Object.class, false);

			ret.grow(values.size());
			for (Object[] value : values) {
				if (useContext) {
					Object[] tmpParams = new Object[value.length + 1];
					tmpParams[0] = ctx;
					System.arraycopy(value, 0, tmpParams, 1, value.length);
					ret.add(method.invoke(null, tmpParams));
				} else {
					ret.add(method.invoke(null, value));
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
