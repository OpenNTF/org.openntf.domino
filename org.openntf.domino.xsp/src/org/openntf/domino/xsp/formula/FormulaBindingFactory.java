package org.openntf.domino.xsp.formula;

import javax.faces.application.Application;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;

import com.ibm.xsp.binding.BindingFactory;
import com.ibm.xsp.util.ValueBindingUtil;

public class FormulaBindingFactory implements BindingFactory {

	public static final String FORMULA = "formula";

	@Override
	public MethodBinding createMethodBinding(final Application arg0, final String expr, final Class[] paramClasses) {
		String str = ValueBindingUtil.parseSimpleExpression(expr);
		return new FormulaMethodBinding(str, paramClasses);
	}

	@Override
	public ValueBinding createValueBinding(final Application arg0, final String expr) {
		String str = ValueBindingUtil.parseSimpleExpression(expr);
		return new FormulaValueBinding(str);
	}

	@Override
	public String getPrefix() {
		return FORMULA;
	}

}
