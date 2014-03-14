package org.openntf.domino.formula.impl;

import java.lang.reflect.Method;

import org.openntf.domino.formula.FormulaContext;
import org.openntf.domino.formula.ValueHolder;

public class AtFunctionGeneric extends AtFunction {
	// How this works:
	// 
	// You define static methods in your class, starting with "at". There are two types of allowed return types:
	// 
	// 1) Methods returning a ValueHolder. You must do multi value handling inside your function
	// 		you may specify a "FormulaContext ctx" as first parameter (optional) and a "ValueHolder[] params"
	// 2) Methods returning something else. MultiValueHandling is done automatically.
	// 		you may specify a "FormulaContext ctx" as first parameter (optional) and the remaining parameter that the function needs
	// 
	protected Method method;
	protected int paramCount;
	protected boolean useContext;
	protected Class<?> varArgClass;
	protected int minArgs = -1;
	protected int maxArgs = -1;

	public AtFunctionGeneric(final String image, final Method method) {
		super(image);
		this.method = method;
		Class<?>[] pt = method.getParameterTypes();
		paramCount = pt.length;
		if (method.isVarArgs()) {
			varArgClass = pt[paramCount - 1].getComponentType();
		}
		if (paramCount >= 1) {
			if (FormulaContext.class.isAssignableFrom(pt[0])) {
				useContext = true;
				paramCount--;
			}
		}

		ParamCount pc = method.getAnnotation(ParamCount.class);
		if (pc != null) {
			minArgs = pc.value()[0];
			maxArgs = pc.value()[pc.value().length - 1];
		}
	}

	public ValueHolder evaluate(final FormulaContext ctx, final ValueHolder[] params) throws Exception {
		switch (paramCount) {
		case 0:
			if (useContext) {
				return (ValueHolder) method.invoke(null, new Object[] { ctx });
			} else {
				return (ValueHolder) method.invoke(null, (Object[]) null);
			}
		case 1:
			if (useContext) {
				return (ValueHolder) method.invoke(null, new Object[] { ctx, params });
			} else {
				return (ValueHolder) method.invoke(null, new Object[] { params });
			}
		default:
			throw new IllegalArgumentException("Illegal parameter count: " + paramCount);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.formula.impl.AtFunction#getPrefix()
	 */
	@Override
	protected String getPrefix() {
		return method.getDeclaringClass().getSimpleName();
	}

	public boolean checkParamCount(final int i) {
		if (minArgs != -1 && i < minArgs)
			return false;
		if (maxArgs != -1 && i > maxArgs)
			return false;
		return true;
	}

}
