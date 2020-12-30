/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.xsp.formula;

import javax.faces.application.Application;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;

import com.ibm.xsp.binding.BindingFactory;
import com.ibm.xsp.util.ValueBindingUtil;

/**
 * This factory is for expressions like <code>#{formula:...}</code> in XPages.
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public class FormulaBindingFactory implements BindingFactory {

	/** the prefix for the engine (= <code>"formula"</code>) */
	public static final String FORMULA = "formula"; //$NON-NLS-1$

	/**
	 * Create a method binding for the specified formula
	 * 
	 * @param app
	 *            not used here
	 * @param expr
	 *            the formula expression (with #{formula:...})
	 * @param paramClasses
	 *            not used here
	 * @return a {@link FormulaMethodBinding}
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public MethodBinding createMethodBinding(final Application app, final String expr, final Class[] paramClasses) {
		String str = ValueBindingUtil.parseSimpleExpression(expr);
		return new FormulaMethodBinding(str, paramClasses);
	}

	/**
	 * Create a new value binding for the specified formula
	 * 
	 * @param app
	 *            not used here
	 * @param expr
	 *            the formula expression (with #{formula:...})
	 * @return a {@link FormulaMethodBinding}
	 */
	@Override
	public ValueBinding createValueBinding(final Application app, final String expr) {
		String str = ValueBindingUtil.parseSimpleExpression(expr);
		return new FormulaValueBinding(str);
	}

	/**
	 * Returns the prefix
	 * 
	 * @return {@link #FORMULA}
	 */
	@Override
	public String getPrefix() {
		return FORMULA;
	}

}
