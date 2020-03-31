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
package org.openntf.domino.xsp.helpers;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;

import org.openntf.domino.xsp.ODAPlatform;

import com.ibm.xsp.util.Delegation;

/**
 * VariableResolver for the library
 */
public class VariableResolver extends javax.faces.el.VariableResolver {
	protected final javax.faces.el.VariableResolver _resolver;
	@SuppressWarnings("unused")
	private final static boolean _debug = ODAPlatform.isDebug();

	/**
	 * Constructor
	 * 
	 */
	public VariableResolver() throws FacesException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		this._resolver = ((javax.faces.el.VariableResolver) Delegation.getImplementation("variable-resolver"));
		// TODO
	}

	/**
	 * Constructor, loading in a specific VariableResolver
	 * 
	 * @param resolver
	 *            VariableResolver
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	public VariableResolver(final javax.faces.el.VariableResolver resolver) {
		// TODO
		_resolver = resolver;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.el.VariableResolver#resolveVariable(javax.faces.context.FacesContext, java.lang.String)
	 */
	@Override
	public Object resolveVariable(final FacesContext paramFacesContext, final String paramString) throws EvaluationException {
		// your code goes here
		return _resolver.resolveVariable(paramFacesContext, paramString);
	}
}
