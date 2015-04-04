package org.openntf.domino.xsp;

import org.openntf.domino.xsp.formula.FormulaBindingFactory;
import org.openntf.domino.xsp.msg.MsgBindingFactory;

import com.ibm.xsp.library.XspContributor;

public class OpenntfDominoXspContributor extends XspContributor {

	@Override
	public Object[][] getFactories() {
		Object[][] result = new Object[][] {
				// That may not work reliable, as it depends on the sorting of a hashmap
				// So that object factory will only create open* objects, the GodMode-Factory will create the other objects
				{ "org.openntf.domino.xsp.helpers.DOMINO_IMPLICIT_OBJECT_FACTORY",
						org.openntf.domino.xsp.helpers.OpenntfDominoImplicitObjectFactory.class },
				{ FormulaBindingFactory.FORMULA, FormulaBindingFactory.class }, { MsgBindingFactory.MSG, MsgBindingFactory.class } };

		return result;
	}
}
