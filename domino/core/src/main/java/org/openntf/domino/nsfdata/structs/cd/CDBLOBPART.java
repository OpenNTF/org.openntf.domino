/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
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
 * This CD record is used in conjunction with CD record CDEVENT. If a CDEVENT record has an ActionType of ACTION_TYPE_JAVASCRIPT then
 * CDBLOBPART contains the JavaScript code. There may be more then one CDBLOBPART record for each CDEVENT. Therefore it may be necessary to
 * loop thorough all of the CDBLOBPART records to read in the complete JavaScript code. (editods.h)
 *
 */
@SuppressWarnings("nls")
public class CDBLOBPART extends CDRecord {
	public final WSIG Header = inner(new WSIG());
	public final Unsigned16 OwnerSig = new Unsigned16();
	public final Unsigned16 Length = new Unsigned16();
	public final Unsigned16 BlobMax = new Unsigned16();
	public final Unsigned8[] Reserved = array(new Unsigned8[8]);

	static {
		addVariableData("BlobData", "Length");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	public byte[] getBlobData() {
		return (byte[]) getVariableElement("BlobData");
	}
}
