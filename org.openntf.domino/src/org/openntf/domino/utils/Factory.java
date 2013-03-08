package org.openntf.domino.utils;

import java.util.ArrayList;
import java.util.Collection;

import org.openntf.domino.exceptions.UndefinedDelegateTypeException;

public enum Factory {
	;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T fromLotus(lotus.domino.Base lotus, Class<? extends org.openntf.domino.Base> T) {
		if (lotus instanceof lotus.domino.Name) {
			return (T) new org.openntf.domino.impl.Name((lotus.domino.Name) lotus);
		} else if (lotus instanceof lotus.domino.Session) {
			return (T) new org.openntf.domino.impl.Session((lotus.domino.Session) lotus);
		} else if (lotus instanceof lotus.domino.Database) {
			return (T) new org.openntf.domino.impl.Database((lotus.domino.Database) lotus);
		} else if (lotus instanceof lotus.domino.Document) {
			return (T) new org.openntf.domino.impl.Document((lotus.domino.Document) lotus);
		} else if (lotus instanceof lotus.domino.Form) {
			return (T) new org.openntf.domino.impl.Form((lotus.domino.Form) lotus);
		} else if (lotus instanceof lotus.domino.DateTime) {
			return (T) new org.openntf.domino.impl.DateTime((lotus.domino.DateTime) lotus);
		} else if (lotus instanceof lotus.domino.DateRange) {
			return (T) new org.openntf.domino.impl.DateRange((lotus.domino.DateRange) lotus);
		}
		throw new UndefinedDelegateTypeException();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> Collection<T> fromLotus(Collection<?> lotusColl, Class<? extends org.openntf.domino.Base> T) {
		Collection<T> result = new ArrayList<T>(); // TODO anyone got a better implementation?
		if (!lotusColl.isEmpty()) {
			for (Object lotus : lotusColl) {
				if (lotus instanceof lotus.domino.Base) {
					result.add((T) fromLotus((lotus.domino.Base) lotus, T));
				}
			}
		}
		return result;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> org.openntf.domino.impl.Vector<T> fromLotusAsVector(Collection<?> lotusColl,
			Class<? extends org.openntf.domino.Base> T) {
		org.openntf.domino.impl.Vector<T> result = new org.openntf.domino.impl.Vector<T>(); // TODO anyone got a better implementation?
		System.out.println("START creation new Vector from lotus objects...");
		if (!lotusColl.isEmpty()) {
			for (Object lotus : lotusColl) {
				if (lotus instanceof lotus.domino.local.NotesBase) {
					result.add((T) fromLotus((lotus.domino.Base) lotus, T));
				}
			}
		}
		System.out.println("END creation new Vector from lotus objects...");
		return result;

	}

	public static org.openntf.domino.Session getSession() {
		try {
			lotus.domino.Session s = lotus.domino.NotesFactory.createSession();
			return fromLotus(s, org.openntf.domino.Session.class);
		} catch (lotus.domino.NotesException ne) {
			DominoUtils.handleException(ne);
		}
		return null;
	}

}
