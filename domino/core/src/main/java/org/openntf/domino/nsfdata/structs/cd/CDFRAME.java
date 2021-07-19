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
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * Frame Name String and Target Name string follow the fixed portion of this CD Record. The strings are not null terminated. (fsods.h)
 * 
 * @since Lotus Notes/Domino 5.0.1
 *
 */
@SuppressWarnings("nls")
public class CDFRAME extends CDRecord {

	public final WSIG Header = inner(new WSIG());
	// TODO make enum
	public final Unsigned32 Flags = new Unsigned32();
	// TODO make enum
	public final Unsigned16 DataFlags = new Unsigned16();
	public final Bool BorderEnable = new Bool();
	public final Bool NoResize = new Bool();
	// TODO make enum
	public final Unsigned16 ScrollBarStyle = new Unsigned16();
	public final Unsigned16 MarginWidth = new Unsigned16();
	public final Unsigned16 MarginHeight = new Unsigned16();
	public final Unsigned32 dwReserved = new Unsigned32();
	public final Unsigned16 FrameNameLength = new Unsigned16();
	public final Unsigned16 Reserved1 = new Unsigned16();
	public final Unsigned16 FrameTargetLength = new Unsigned16();
	public final COLOR_VALUE FrameBorderColor = inner(new COLOR_VALUE());
	public final Unsigned16 wReserved = new Unsigned16();

	static {
		addVariableString("FrameName", "FrameNameLength");
		addVariableString("FrameTarget", "FrameTargetLength");

		// TODO add DataFlags extra data
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	public String getFrameName() {
		return (String) getVariableElement("FrameName");
	}

	public String getFrameTarget() {
		return (String) getVariableElement("FrameTarget");
	}

	//	@Override
	//	public String toString() {
	//		return "[" + getClass().getSimpleName() + ": Flags=" + getFlags() + ", DataFlags=" + getDataFlags() + ", BorderEnable="
	//				+ getBorderEnable() + ", NoResize=" + getNoResize() + ", ScrollBarStyle=" + getScrollBarStyle() + ", MarginWidth="
	//				+ getMarginWidth() + ", MarginHeight=" + getMarginHeight() + ", FrameBorderColor=" + getFrameBorderColor() + ", FrameName="
	//				+ getFrameName() + ", FrameTarget=" + getFrameTarget() + "]";
	//	}
}
