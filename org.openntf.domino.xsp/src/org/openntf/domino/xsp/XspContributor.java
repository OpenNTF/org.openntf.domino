package org.openntf.domino.xsp;

import org.openntf.domino.xsp.helpers.ImplicitObjectFactory;

public class XspContributor extends com.ibm.xsp.library.XspContributor {
	public static final String HELPER_IMPLICITOBJECTS_FACTORY = "org.openntf.domino.xsp.helpers.IMPLICIT_OBJECT_FACTORY";

	@Override
	public Object[][] getFactories() {
		return new Object[][] { { HELPER_IMPLICITOBJECTS_FACTORY, ImplicitObjectFactory.class } };
	}
}
