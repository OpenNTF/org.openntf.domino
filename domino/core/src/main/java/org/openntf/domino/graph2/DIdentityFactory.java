package org.openntf.domino.graph2;

public interface DIdentityFactory {

	public Object getId(DElementStore store, Class<?> type, Object context, Object... args);

}
