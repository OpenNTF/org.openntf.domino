/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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
package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.NSFCompiledFormula;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This record contains the "Hide When" formula for a paragraph attributes block. (editods.h)
 * 
 * @since Lotus Notes 4.0
 *
 */
@SuppressWarnings("nls")
public class CDPABHIDE extends CDRecord {

	public final WSIG Header = inner(new WSIG());
	public final Unsigned16 PABID = new Unsigned16();
	public final Unsigned8[] Reserved = array(new Unsigned8[8]);

	static {
		addVariableData("Formula", "getFormulaLength");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	public int getFormulaLength() {
		return (int) (Header.getRecordLength() - size());
	}

	public NSFCompiledFormula getFormula() {
		return new NSFCompiledFormula((byte[]) getVariableElement("Formula"));
	}
}
