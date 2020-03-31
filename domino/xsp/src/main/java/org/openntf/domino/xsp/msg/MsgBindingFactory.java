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
