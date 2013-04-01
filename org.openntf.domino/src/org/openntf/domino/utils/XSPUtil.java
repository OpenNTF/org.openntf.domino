package org.openntf.domino.utils;

import java.lang.reflect.Method;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;

public enum XSPUtil {
	INSTANCE;

	public static Document wrap(lotus.domino.Document doc) {
		try {
			lotus.domino.Database db = doc.getParentDatabase();
			Session session = Factory.fromLotus(db.getParent(), Session.class, null);
			Database wrappedDB = Factory.fromLotus(db, Database.class, session);
			return Factory.fromLotus(doc, Document.class, wrappedDB);
		} catch (lotus.domino.NotesException ne) {
			return null;
		}
	}

	public static Database getCurrentDatabase() throws Exception {
		try {
			lotus.domino.Database db = (lotus.domino.Database) resolveVariable("database");
			Session session = Factory.fromLotus(db.getParent(), Session.class, null);
			return Factory.fromLotus(db, Database.class, session);
		} catch (lotus.domino.NotesException ne) {
			return null;
		}
	}

	public static Session getCurrentSession() throws Exception {
		return Factory.fromLotus((lotus.domino.Session) resolveVariable("session"), Session.class, null);
	}

	public static Session getCurrentSessionAsSigner() throws Exception {
		return Factory.fromLotus((lotus.domino.Session) resolveVariable("sessionAsSigner"), Session.class, null);
	}

	public static Session getCurrentSessionAsSignerWithFullAccess() throws Exception {
		return Factory.fromLotus((lotus.domino.Session) resolveVariable("sessionAsSignerWithFullAccess"), Session.class, null);
	}

	public static Object resolveVariable(String varName) throws Exception {
		Class<?> facesContextClass = Class.forName("javax.faces.context.FacesContext");
		Method getCurrentInstance = facesContextClass.getMethod("getCurrentInstance");
		Method getApplication = facesContextClass.getMethod("getApplication");
		Class<?> applicationClass = Class.forName("javax.faces.application.Application");
		Method getVariableResolver = applicationClass.getMethod("getVariableResolver");
		Class<?> variableResolverClass = Class.forName("javax.faces.el.VariableResolver");
		Method resolveVariable = variableResolverClass.getMethod("resolveVariable", facesContextClass, String.class);

		Object facesContext = getCurrentInstance.invoke(null);
		Object application = getApplication.invoke(facesContext);
		Object variableResolver = getVariableResolver.invoke(application);
		return resolveVariable.invoke(variableResolver, facesContext, varName);
	}
}
