/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
