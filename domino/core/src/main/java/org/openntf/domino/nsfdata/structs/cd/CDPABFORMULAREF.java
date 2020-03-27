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

import org.openntf.domino.nsfdata.structs.BSIG;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * Starting in Release 4.0 of Notes, a paragraph may have an associated formula that determines when the paragraph is to be hidden. The
 * CDPABFORMULAREF is similar to the CDPABREFERENCE record, with an additional field that identifies the CDPABDEFINITION record containing
 * the CDPABHIDE record for the "Hide When" formula. (editods.h)
 * 
 * @since Lotus Notes 4.0
 *
 */
public class CDPABFORMULAREF extends CDRecord {

	public final BSIG Header = inner(new BSIG());
	public final Unsigned16 SourcePABID = new Unsigned16();
	public final Unsigned16 DestPABID = new Unsigned16();

	@Override
	public SIG getHeader() {
		return Header;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": SourcePabId=" + SourcePABID.get() + ", DestPabId=" + DestPABID.get() + "]";
	}
}
