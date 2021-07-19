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
 * The 64K limit on paragraphs has been removed in Notes/Domino 6. To ensure backward compatibility, "large" paragraphs are broken into
 * smaller paragraphs which are bracketed by a CDLARGEPARAGRAPH record with its Flags member set to CDLARGEPARAGRAPH_BEGIN and a
 * CDLARGEPARAGRAPH record with its Flags member set to CDLARGEPARAGRAPH_END. (editods.h)
 * 
 * @since Lotus Notes/Domino 6.0
 */
public class CDLARGEPARAGRAPH extends CDRecord {

	public static final short CDLARGEPARAGRAPH_BEGIN = 0x0001;
	public static final short CDLARGEPARAGRAPH_END = 0x0002;

	public final WSIG Header = inner(new WSIG());
	// TODO make enum
	public final Unsigned16 Version = new Unsigned16();
	// TODO make enum
	public final Unsigned16 Flags = new Unsigned16();
	public final Unsigned32[] Spare = array(new Unsigned32[2]);

	@Override
	public SIG getHeader() {
		return Header;
	}
}
