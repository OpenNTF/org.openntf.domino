package org.openntf.domino.impl;

public enum ImplUtils {
	;

	public static lotus.domino.Base getLotus(final org.openntf.domino.Base object) {
		return ((org.openntf.domino.impl.Base) object).getDelegate();
	}

	public static void recycle(final Object object) {
		Base.s_recycle(object);
	}

}
