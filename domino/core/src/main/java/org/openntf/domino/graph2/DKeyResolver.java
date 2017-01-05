package org.openntf.domino.graph2;

import java.util.Collection;

import org.openntf.domino.big.NoteCoordinate;

import com.tinkerpop.blueprints.Element;

public interface DKeyResolver {
	public NoteCoordinate resolveKey(Class<?> type, Object key);

	public Collection<Class<?>> getTypes();

	public Element handleMissingKey(Class<?> type, Object key);
}
