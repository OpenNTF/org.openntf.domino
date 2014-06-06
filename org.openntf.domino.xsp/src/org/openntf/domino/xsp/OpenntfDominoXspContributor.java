package org.openntf.domino.xsp;

import org.openntf.domino.xsp.formula.FormulaBindingFactory;

import com.ibm.xsp.library.XspContributor;

public class OpenntfDominoXspContributor extends XspContributor {

	@Override
	public Object[][] getFactories() {
		Object[][] result = new Object[][] {
				{ "org.openntf.domino.xsp.helpers.DOMINO_IMPLICIT_OBJECT_FACTORY",
						org.openntf.domino.xsp.helpers.OpenntfDominoImplicitObjectFactory2.class },
				{ FormulaBindingFactory.FORMULA, FormulaBindingFactory.class } };

		return result;
	}
}
