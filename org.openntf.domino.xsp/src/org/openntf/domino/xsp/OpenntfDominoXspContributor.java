package org.openntf.domino.xsp;

import com.ibm.xsp.library.XspContributor;

public class OpenntfDominoXspContributor extends XspContributor {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.library.XspContributor#getFactories()
	 */
	@Override
	public Object[][] getFactories() {
		Object[][] result = new Object[][] { { "org.openntf.domino.xsp.helpers.DOMINO_IMPLICIT_OBJECT_FACTORY",
				org.openntf.domino.xsp.helpers.OpenntfDominoImplicitObjectFactory2.class } };
		return result;
	}
}
