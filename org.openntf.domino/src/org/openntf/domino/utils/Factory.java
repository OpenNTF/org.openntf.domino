package org.openntf.domino.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import org.openntf.domino.exceptions.UndefinedDelegateTypeException;

public enum Factory {
	;

	@SuppressWarnings( { "rawtypes", "unchecked" })
	public static <T> T fromLotus(lotus.domino.Base lotus, Class<? extends org.openntf.domino.Base> T) {
		if (lotus instanceof lotus.domino.Name) {
			return (T) new org.openntf.domino.impl.Name((lotus.domino.Name) lotus);
		} else if (lotus instanceof lotus.domino.Session) {
			return (T) new org.openntf.domino.impl.Session((lotus.domino.Session) lotus);
		} else if (lotus instanceof lotus.domino.Database) {
			return (T) new org.openntf.domino.impl.Database((lotus.domino.Database) lotus);
		} else if (lotus instanceof lotus.domino.DateTime) {
			return (T) new org.openntf.domino.impl.DateTime((lotus.domino.DateTime) lotus);
		} else if (lotus instanceof lotus.domino.DateRange) {
			return (T) new org.openntf.domino.impl.DateRange((lotus.domino.DateRange) lotus);
		} else if (lotus instanceof lotus.domino.ACL) {
			return (T) new org.openntf.domino.impl.ACL((lotus.domino.ACL) lotus);
		} else if (lotus instanceof lotus.domino.ACLEntry) {
			return (T) new org.openntf.domino.impl.ACLEntry((lotus.domino.ACLEntry) lotus);
		} else if (lotus instanceof lotus.domino.View) {
			return (T) new org.openntf.domino.impl.View((lotus.domino.View) lotus);
		} else if (lotus instanceof lotus.domino.Document) {
			return (T) new org.openntf.domino.impl.Document((lotus.domino.Document) lotus);
		} else if (lotus instanceof lotus.domino.DocumentCollection) {
			return (T) new org.openntf.domino.impl.DocumentCollection((lotus.domino.DocumentCollection) lotus);
		}
		throw new UndefinedDelegateTypeException();
	}

	@SuppressWarnings( { "unchecked", "rawtypes" })
	public static <T> Collection<T> fromLotus(Collection<lotus.domino.Base> lotusColl, Class<? extends org.openntf.domino.Base> T) {
		Collection<T> result = new ArrayList<T>(); // TODO anyone got a better implementation?
		if (!lotusColl.isEmpty()) {
			for (lotus.domino.Base lotus : lotusColl) {
				result.add((T) fromLotus(lotus, T));
			}
		}
		return result;

	}

	@SuppressWarnings( { "unchecked", "rawtypes" })
	public static <T> Vector<T> fromLotusAsVector(Collection<lotus.domino.Base> lotusColl, Class<? extends org.openntf.domino.Base> T) {
		Vector<T> result = new Vector<T>(); // TODO anyone got a better implementation?
		if (!lotusColl.isEmpty()) {
			for (lotus.domino.Base lotus : lotusColl) {
				result.add((T) fromLotus(lotus, T));
			}
		}
		return result;

	}

	// For passing arguments to delegates
	@SuppressWarnings("unchecked")
	public static <T> T toLotus(lotus.domino.Base obj, Class<? extends lotus.domino.Base> T) {
		if (obj instanceof org.openntf.domino.Base) {
			return (T) ((org.openntf.domino.Base) obj).getDelegate();
		}
		return (T) obj;
	}

	@SuppressWarnings("unchecked")
	public static lotus.domino.Base toLotus(lotus.domino.Base obj) {
		if (obj instanceof org.openntf.domino.Base) {
			return ((org.openntf.domino.Base) obj).getDelegate();
		}
		return obj;
	}

}
