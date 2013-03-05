package org.openntf.domino.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import org.openntf.domino.exceptions.UndefinedDelegateTypeException;

public enum Factory {
	;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T fromLotus(lotus.domino.Base lotus, Class<? extends org.openntf.domino.Base> T) {
		if (lotus instanceof lotus.domino.Name) {
			return (T) new org.openntf.domino.impl.Name((lotus.domino.Name) lotus);
		}
		throw new UndefinedDelegateTypeException();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> Collection<T> fromLotus(Collection<lotus.domino.Base> lotusColl, Class<? extends org.openntf.domino.Base> T) {
		Collection<T> result = new ArrayList<T>(); // TODO anyone got a better implementation?
		if (!lotusColl.isEmpty()) {
			for (lotus.domino.Base lotus : lotusColl) {
				result.add((T) fromLotus(lotus, T));
			}
		}
		return result;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> Vector<T> fromLotusAsVector(Collection<lotus.domino.Base> lotusColl, Class<? extends org.openntf.domino.Base> T) {
		Vector<T> result = new Vector<T>(); // TODO anyone got a better implementation?
		if (!lotusColl.isEmpty()) {
			for (lotus.domino.Base lotus : lotusColl) {
				result.add((T) fromLotus(lotus, T));
			}
		}
		return result;

	}

}
