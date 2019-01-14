package org.openntf.domino.impl;

public enum ImplUtils {
	;

	@SuppressWarnings("unchecked")
	public static <D extends lotus.domino.Base> D getLotus(final org.openntf.domino.Base<?> object) {
		return ((org.openntf.domino.impl.Base<?, D, ?>) object).getDelegate();
	}

	public static void recycle(final Object object) {
		Base.s_recycle(object);
	}

}
