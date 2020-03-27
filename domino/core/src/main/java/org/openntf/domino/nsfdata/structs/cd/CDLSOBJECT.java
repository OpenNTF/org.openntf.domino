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
package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * The CD record contains Lotus Script object code. (editods.h)
 * 
 * @since Lotus Notes/Domino 4.6
 *
 */
public class CDLSOBJECT extends CDRecord {

	public final WSIG Header = inner(new WSIG());
	public final Unsigned32 CodeSize = new Unsigned32();
	public final Unsigned8[] Reserved = array(new Unsigned8[4]);

	static {
		addVariableData("ObjectCode", "CodeSize");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	public byte[] getObjectCode() {
		return (byte[]) getVariableElement("ObjectCode");
	}
}
