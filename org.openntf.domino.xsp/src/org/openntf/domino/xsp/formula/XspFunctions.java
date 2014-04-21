package org.openntf.domino.xsp.formula;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

import org.openntf.formula.Function;
import org.openntf.formula.FunctionFactory;
import org.openntf.formula.FunctionSet;
import org.openntf.formula.ValueHolder;
import org.openntf.formula.annotation.ParamCount;

public enum XspFunctions {
	;

	public static class Functions extends FunctionSet {
		private static final Map<String, Function> functionSet = FunctionFactory.getFunctions(XspFunctions.class);

		@Override
		public Map<String, Function> getFunctions() {
			return functionSet;
		}
	}

	@ParamCount(0)
	public static ValueHolder atThisName(final FormulaContextXsp ctx) {
		UIComponent comp = ctx.getComponent();
		while (comp != null) {
			if (comp instanceof UIOutput) {
				return ValueHolder.valueOf(((UIOutput) comp).getId());
			}
			comp = comp.getParent();
		}
		return ValueHolder.valueDefault();
	}

	@ParamCount(0)
	public static ValueHolder atThisValue(final FormulaContextXsp ctx) {
		UIComponent comp = ctx.getComponent();
		while (comp != null) {
			if (comp instanceof UIOutput) {
				return ValueHolder.valueOf(((UIOutput) comp).getValue());
			}
			comp = comp.getParent();
		}
		return ValueHolder.valueDefault();
	}
}
