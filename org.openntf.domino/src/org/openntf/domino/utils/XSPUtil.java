package org.openntf.domino.utils;

import java.lang.reflect.Method;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewEntryCollection;
import org.openntf.domino.ViewNavigator;

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

	public static ViewEntry wrap(lotus.domino.ViewEntry entry) {
		try {
			Object parent = entry.getParent();
			lotus.domino.View view;
			if (parent instanceof lotus.domino.ViewEntryCollection) {
				view = ((lotus.domino.ViewEntryCollection) parent).getParent();
			} else if (parent instanceof lotus.domino.ViewNavigator) {
				view = ((lotus.domino.ViewNavigator) parent).getParentView();
			} else {
				view = (lotus.domino.View) parent;
			}
			lotus.domino.Database db = view.getParent();
			Session session = Factory.fromLotus(db.getParent(), Session.class, null);
			Database wrappedDB = Factory.fromLotus(db, Database.class, session);
			View wrappedView = Factory.fromLotus(view, View.class, wrappedDB);
			if (parent instanceof lotus.domino.ViewEntryCollection) {
				ViewEntryCollection vec = Factory.fromLotus((lotus.domino.Base) parent, ViewEntryCollection.class, wrappedView);
				return Factory.fromLotus(entry, ViewEntry.class, vec);
			} else if (parent instanceof lotus.domino.ViewNavigator) {
				ViewNavigator vnav = Factory.fromLotus((lotus.domino.Base) parent, ViewNavigator.class, wrappedView);
				return Factory.fromLotus(entry, ViewEntry.class, vnav);
			} else {
				return Factory.fromLotus(entry, ViewEntry.class, wrappedView);
			}
		} catch (lotus.domino.NotesException ne) {
			return null;
		}
	}

	public static Database getCurrentDatabase() {
		try {
			lotus.domino.Database db = (lotus.domino.Database) resolveVariable("database");
			Session session = Factory.fromLotus(db.getParent(), Session.class, null);
			return Factory.fromLotus(db, Database.class, session);
		} catch (Exception ne) {
			DominoUtils.handleException(ne);
			return null;
		}
	}

	public static Session getCurrentSession() {
		try {
			return Factory.fromLotus((lotus.domino.Session) resolveVariable("session"), Session.class, null);
		} catch (Exception ne) {
			DominoUtils.handleException(ne);
			return null;
		}
	}

	public static Session getCurrentSessionAsSigner() {
		try {
			return Factory.fromLotus((lotus.domino.Session) resolveVariable("sessionAsSigner"), Session.class, null);
		} catch (Exception ne) {
			DominoUtils.handleException(ne);
			return null;
		}
	}

	public static Session getCurrentSessionAsSignerWithFullAccess() {
		try {
			return Factory.fromLotus((lotus.domino.Session) resolveVariable("sessionAsSignerWithFullAccess"), Session.class, null);
		} catch (Exception ne) {
			DominoUtils.handleException(ne);
			return null;
		}
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
