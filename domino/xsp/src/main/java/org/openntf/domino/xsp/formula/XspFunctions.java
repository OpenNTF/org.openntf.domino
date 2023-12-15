/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;

import org.openntf.formula.Function;
import org.openntf.formula.FunctionFactory;
import org.openntf.formula.FunctionSet;
import org.openntf.formula.ValueHolder;
import org.openntf.formula.annotation.ParamCount;

/**
 * Here some "proof of concept" XSP-functions are implemented
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */

@Deprecated
// until we have some useful implementations
public enum XspFunctions {
	;

	/** helper class that initializes all functions in XspFunction.class */
	public static class Functions extends FunctionSet {
		private static final Map<String, Function> functionSet = FunctionFactory.getFunctions(XspFunctions.class);

		@Override
		public Map<String, Function> getFunctions() {
			return functionSet;
		}

		/**
		 * We must have higer priority than "evaluateNative"
		 * 
		 * @return 30
		 */
		@Override
		public int getPriority() {
			return 30;
		}

	}

	/**
	 * Returns the clientID of the current XPage-component
	 * 
	 * @param ctx
	 *            the FormulaContext
	 * @return the clientID
	 */
	@ParamCount(0)
	public static ValueHolder atThisName(final FormulaContextXsp ctx) {
		UIComponent comp = ctx.getComponent();
		while (comp != null) {
			if (comp instanceof UIOutput) {
				return ValueHolder.valueOf(comp.getClientId(FacesContext.getCurrentInstance()));
				// return ValueHolder.valueOf(((UIOutput) comp).getId());
			}
			comp = comp.getParent();
		}
		return ValueHolder.valueDefault();
	}

	/**
	 * Returns the value of the current UIComponent
	 * 
	 */
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
