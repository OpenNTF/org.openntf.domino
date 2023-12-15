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

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * The CDTARGET structure specifies the target (ie: the frame) where a resource link hotspot is to be displayed. It is followed by variable
 * length data whose length is specified in the TargetLength member. This variable length data specifies the target frame. The format of the
 * variable length data is specified by the Flags member of the CDTARGET structure. If no flags are specified, then the data following the
 * CDTARGET record is a character string containing the name of the target frame. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
@SuppressWarnings("nls")
public class CDTARGET extends CDRecord {

	public final WSIG Header = inner(new WSIG());
	public final Unsigned16 TargetLength = new Unsigned16();
	// TODO make enum
	public final Unsigned16 Flags = new Unsigned16();
	public final Unsigned32 Reserved = new Unsigned32();

	static {
		addVariableString("Target", "TargetLength");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	public String getTarget() {
		return (String) getVariableElement("Target");
	}
}
