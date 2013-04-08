package org.openntf.domino.xsp.helpers;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;

import org.openntf.domino.xsp.Activator;

import com.ibm.xsp.util.Delegation;

public class VariableResolver extends javax.faces.el.VariableResolver {
	protected final javax.faces.el.VariableResolver _resolver;
	private final static boolean _debug = Activator.isDebug();

	public VariableResolver() throws FacesException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		this._resolver = ((javax.faces.el.VariableResolver) Delegation.getImplementation("variable-resolver"));
		// TODO
	}

	public VariableResolver(javax.faces.el.VariableResolver resolver) {
		// TODO
		_resolver = resolver;
	}

	@Override
	public Object resolveVariable(FacesContext paramFacesContext, String paramString) throws EvaluationException {
		// your code goes here
		return _resolver.resolveVariable(paramFacesContext, paramString);
	}
}
