package org.openntf.domino.xsp.msg;

import javax.faces.application.Application;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;

import com.ibm.xsp.binding.BindingFactory;
import com.ibm.xsp.util.ValueBindingUtil;

public class MsgBindingFactory implements BindingFactory {

	/** the prefix for the engine (= <code>"msg"</code>) */
	public static final String MSG = "msg";

	/**
	 * Create a method binding for the specified formula
	 * 
	 * @param app
	 *            not used here
	 * @param expr
	 *            the msg expression (with #{msg:...})
	 * @param paramClasses
	 *            not used here
	 * @return a {@link MsgMethodBinding}
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public MethodBinding createMethodBinding(final Application app, final String expr, final Class[] paramClasses) {
		String str = ValueBindingUtil.parseSimpleExpression(expr);
		return new MsgMethodBinding(str, paramClasses);
	}

	/**
	 * Create a new value binding for the specified formula
	 * 
	 * @param app
	 *            not used here
	 * @param expr
	 *            the formula expression (with #{formula:...})
	 * @return a {@link MsgMethodBinding}
	 */
	@Override
	public ValueBinding createValueBinding(final Application app, final String expr) {
		String str = ValueBindingUtil.parseSimpleExpression(expr);
		return new MsgValueBinding(str);
	}

	@Override
	public String getPrefix() {
		return MSG;
	}

}
