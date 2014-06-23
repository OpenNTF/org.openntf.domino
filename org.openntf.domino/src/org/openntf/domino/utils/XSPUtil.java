package org.openntf.domino.utils;

import java.lang.reflect.Method;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewEntryCollection;
import org.openntf.domino.ViewNavigator;

// TODO: Auto-generated Javadoc
/**
 * The Enum XSPUtil.
 */
public enum XSPUtil {

	/** The instance. */
	INSTANCE;

	/**
	 * Wrap.
	 * 
	 * @param doc
	 *            the doc
	 * @return the document
	 */
	public static Document wrap(final lotus.domino.Document doc) {
		try {
			lotus.domino.Database db = doc.getParentDatabase();
			Session session = Factory.fromLotus(db.getParent(), Session.SCHEMA, null);
			Database wrappedDB = Factory.fromLotus(db, Database.SCHEMA, session);
			return Factory.fromLotus(doc, Document.SCHEMA, wrappedDB);
		} catch (lotus.domino.NotesException ne) {
			return null;
		}
	}

	/**
	 * Wrap.
	 * 
	 * @param entry
	 *            the entry
	 * @return the view entry
	 */
	public static ViewEntry wrap(final lotus.domino.ViewEntry entry) {
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

	/**
	 * Gets the current database.
	 * 
	 * @return the current database
	 */
	public static Database getCurrentDatabase() {
		try {
			lotus.domino.Database db = (lotus.domino.Database) resolveVariable("database");
			if (db instanceof org.openntf.domino.Database) {
				return (org.openntf.domino.Database) db;
			} else {
				return Factory.fromLotus(db, Database.SCHEMA, getCurrentSession());
			}
		} catch (Exception ne) {
			DominoUtils.handleException(ne);
			return null;
		}
	}

	/**
	 * Gets the current session.
	 * 
	 * @return the current session
	 */
	public static Session getCurrentSession() {
		try {
			lotus.domino.Session s = (lotus.domino.Session) resolveVariable("session");
			if (s instanceof org.openntf.domino.Session) {
				return (org.openntf.domino.Session) s;
			} else {
				return Factory.fromLotus(s, Session.class, null);
			}
		} catch (ClassNotFoundException nfe) {
			System.out
					.println("Class not found exception generally indicates that the OpenNTF API has not been initialized from XPages. Please see the installation instructions.");
			return null;
		} catch (Exception ne) {
			System.out.println("ALERT! Unable to find current session. Normal log handling not likely available.");
			ne.printStackTrace();
			return null;
		}
	}

	/**
	 * Gets the current session as signer.
	 * 
	 * @return the current session as signer
	 */
	public static Session getCurrentSessionAsSigner() {
		try {
			return Factory.fromLotus((lotus.domino.Session) resolveVariable("sessionAsSigner"), Session.SCHEMA, null);
		} catch (Exception ne) {
			DominoUtils.handleException(ne);
			return null;
		}
	}

	/**
	 * Gets the current session as signer with full access.
	 * 
	 * @return the current session as signer with full access
	 */
	public static Session getCurrentSessionAsSignerWithFullAccess() {
		try {
			return Factory.fromLotus((lotus.domino.Session) resolveVariable("sessionAsSignerWithFullAccess"), Session.class, null);
		} catch (Exception ne) {
			DominoUtils.handleException(ne);
			return null;
		}
	}

	/**
	 * Resolve variable.
	 * 
	 * @param varName
	 *            the var name
	 * @return the object
	 * @throws Exception
	 *             the exception
	 */
	public static Object resolveVariable(final String varName) throws Exception {
		// TODO RPr move to Xpage-Plugin
		Class<?> facesContextClass = Class.forName("javax.faces.context.FacesContext", true, Factory.getClassLoader());
		Method getCurrentInstance = facesContextClass.getMethod("getCurrentInstance");
		Method getApplication = facesContextClass.getMethod("getApplication");
		Class<?> applicationClass = Class.forName("javax.faces.application.Application", true, Factory.getClassLoader());
		Method getVariableResolver = applicationClass.getMethod("getVariableResolver");
		Class<?> variableResolverClass = Class.forName("javax.faces.el.VariableResolver", true, Factory.getClassLoader());
		Method resolveVariable = variableResolverClass.getMethod("resolveVariable", facesContextClass, String.class);

		Object facesContext = getCurrentInstance.invoke(null);
		Object application = getApplication.invoke(facesContext);
		Object variableResolver = getVariableResolver.invoke(application);
		return resolveVariable.invoke(variableResolver, facesContext, varName);
	}
}
