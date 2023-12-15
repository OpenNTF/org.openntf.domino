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

import org.openntf.domino.nsfdata.structs.BSIG;
import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure stores the style name for a Paragraph Attributes Block (PAB). (editods.h)
 */
public class CDSTYLENAME extends CDRecord {

	public static final int MAX_STYLE_NAME = 35;

	public final BSIG Header = inner(new BSIG());
	// TODO make enum
	public final Unsigned32 Flags = new Unsigned32();
	public final Unsigned16 PABID = new Unsigned16();
	public final Unsigned8[] StyleName = array(new Unsigned8[MAX_STYLE_NAME + 1]);

	static {
		// TODO add oddball variable elements
		//		/* If STYLE_FLAG_FONTID, a FONTID follows this structure... */
		//		/* If STYLE_FLAG_PERMANENT, the structure is followed by:	*/
		//		/*		WORD	nameLen			Length of user name	*/
		//		/*		BYTE	userName [nameLen]	User name			*/
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	public String getStyleName() {
		return ODSUtils.fromAscii(StyleName);
	}
}
