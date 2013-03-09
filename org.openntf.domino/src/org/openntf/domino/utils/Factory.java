package org.openntf.domino.utils;

import java.util.ArrayList;
import java.util.Collection;

import org.openntf.domino.exceptions.UndefinedDelegateTypeException;

public enum Factory {
	;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T fromLotus(lotus.domino.Base lotus, Class<? extends org.openntf.domino.Base> T, org.openntf.domino.Base parent) {
		if (T.isAssignableFrom(lotus.getClass())) {
			return (T) lotus;
		}
		if (lotus == null) {
			return null;
		} else if (lotus instanceof lotus.domino.Name) {
			return (T) new org.openntf.domino.impl.Name((lotus.domino.Name) lotus, parent);
		} else if (lotus instanceof lotus.domino.Session) {
			return (T) new org.openntf.domino.impl.Session((lotus.domino.Session) lotus, parent);
		} else if (lotus instanceof lotus.domino.Database) {
			return (T) new org.openntf.domino.impl.Database((lotus.domino.Database) lotus, parent);
		} else if (lotus instanceof lotus.domino.DocumentCollection) {
			return (T) new org.openntf.domino.impl.DocumentCollection((lotus.domino.DocumentCollection) lotus, parent);
		} else if (lotus instanceof lotus.domino.Document) {
			return (T) new org.openntf.domino.impl.Document((lotus.domino.Document) lotus, parent);
		} else if (lotus instanceof lotus.domino.Form) {
			return (T) new org.openntf.domino.impl.Form((lotus.domino.Form) lotus, parent);
		} else if (lotus instanceof lotus.domino.DateTime) {
			return (T) new org.openntf.domino.impl.DateTime((lotus.domino.DateTime) lotus, parent);
		} else if (lotus instanceof lotus.domino.DateRange) {
			return (T) new org.openntf.domino.impl.DateRange((lotus.domino.DateRange) lotus, parent);
		}
		throw new UndefinedDelegateTypeException();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> Collection<T> fromLotus(Collection<?> lotusColl, Class<? extends org.openntf.domino.Base> T,
			org.openntf.domino.Base<?> parent) {
		Collection<T> result = new ArrayList<T>(); // TODO anyone got a better implementation?
		if (!lotusColl.isEmpty()) {
			for (Object lotus : lotusColl) {
				if (lotus instanceof lotus.domino.Base) {
					result.add((T) fromLotus((lotus.domino.Base) lotus, T, parent));
				}
			}
		}
		return result;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> org.openntf.domino.impl.Vector<T> fromLotusAsVector(Collection<?> lotusColl,
			Class<? extends org.openntf.domino.Base> T, org.openntf.domino.Base<?> parent) {
		org.openntf.domino.impl.Vector<T> result = new org.openntf.domino.impl.Vector<T>(); // TODO anyone got a better implementation?
		System.out.println("START creation new Vector from lotus objects...");
		if (!lotusColl.isEmpty()) {
			for (Object lotus : lotusColl) {
				if (lotus instanceof lotus.domino.local.NotesBase) {
					result.add((T) fromLotus((lotus.domino.Base) lotus, T, parent));
				}
			}
		}
		System.out.println("END creation new Vector from lotus objects...");
		return result;

	}

	public static org.openntf.domino.Session getSession() {
		try {
			lotus.domino.Session s = lotus.domino.NotesFactory.createSession();
			return fromLotus(s, org.openntf.domino.Session.class, null);
		} catch (lotus.domino.NotesException ne) {
			DominoUtils.handleException(ne);
		}
		return null;
	}

	public static org.openntf.domino.Session getSession(org.openntf.domino.Base<?> base) {
		org.openntf.domino.Session result = null;
		if (base instanceof org.openntf.domino.Database) {
			result = ((org.openntf.domino.Database) base).getParent();
		} else if (base instanceof org.openntf.domino.Document) {
			result = ((org.openntf.domino.Document) base).getParentDatabase().getParent();
		} else if (base instanceof org.openntf.domino.DocumentCollection) {
			result = (org.openntf.domino.Session) ((org.openntf.domino.DocumentCollection) base).getParent().getParent();
		} else if (base instanceof org.openntf.domino.View) {
			result = (org.openntf.domino.Session) ((org.openntf.domino.View) base).getParent().getParent();
		} else if (base instanceof org.openntf.domino.DateTime) {
			result = ((org.openntf.domino.DateTime) base).getParent();
		} else if (base instanceof org.openntf.domino.DateRange) {
			result = ((org.openntf.domino.DateRange) base).getParent();
		} else if (base instanceof org.openntf.domino.Form) {
			result = ((org.openntf.domino.Form) base).getParent().getParent();
		} else if (base instanceof org.openntf.domino.Name) {
			result = ((org.openntf.domino.Name) base).getParent();
		} else {
			throw new UndefinedDelegateTypeException();
		}
		return result;
	}

}
