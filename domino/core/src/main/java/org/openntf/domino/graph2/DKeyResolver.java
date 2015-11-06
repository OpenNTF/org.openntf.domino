package org.openntf.domino.graph2;

import java.util.Collection;

import org.openntf.domino.big.NoteCoordinate;

public interface DKeyResolver {
	public NoteCoordinate resolveKey(Class<?> type, Object key);

	public Collection<Class<?>> getTypes();
}
