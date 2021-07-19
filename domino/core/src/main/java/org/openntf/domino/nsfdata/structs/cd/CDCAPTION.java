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

import org.openntf.domino.nsfdata.structs.COLOR_VALUE;
import org.openntf.domino.nsfdata.structs.FONTID;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This CD record defines the properties of a caption for a grapic [sic] record. The actual caption text follows the fixed part of the
 * record.
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
@SuppressWarnings("nls")
public class CDCAPTION extends CDRecord {
	public static enum CaptionPosition {
		BELOW_CENTER, MIDDLE_CENTER
	}

	public final WSIG Header = inner(new WSIG());
	public final Unsigned16 wLength = new Unsigned16();
	public final Enum8<CaptionPosition> Position = new Enum8<CaptionPosition>(CaptionPosition.values());
	public final FONTID FontID = inner(new FONTID());
	public final COLOR_VALUE FontColor = inner(new COLOR_VALUE());
	public final Unsigned8[] Reserved = array(new Unsigned8[11]);

	static {
		addVariableAsciiString("Caption", "wLength");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	public String getCaption() {
		return (String) getVariableElement("Caption");
	}
}
