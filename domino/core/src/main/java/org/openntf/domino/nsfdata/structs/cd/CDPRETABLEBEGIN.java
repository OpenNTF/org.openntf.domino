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

import java.awt.Color;

import org.openntf.domino.nsfdata.structs.COLOR_VALUE;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This CD record provides additional table properties, expanding the information provided in CDTABLEBEGIN. It will only be recognized in
 * Domino versions 5.0 and greater. This record will be ignored in pre 5.0 versions. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDPRETABLEBEGIN extends CDRecord {
	public static enum Viewer {
		UNUSED0, ONCLICK, ONLOADTIMER, ONLOADCYCLEONCE, TABS, FILEDRIVEN, CYCLEONCE, CAPTIONS, LAST
	}

	public final WSIG Header = inner(new WSIG());
	// TODO make enum
	public final Unsigned32 Flags = new Unsigned32();
	public final Unsigned8 Rows = new Unsigned8();
	public final Unsigned8 Columns = new Unsigned8();
	public final Unsigned32 ColumnSizingBits1 = new Unsigned32();
	public final Unsigned32 ColumnSizingBits2 = new Unsigned32();
	public final Enum8<Viewer> ViewerType = new Enum8<Viewer>(Viewer.values());
	public final Unsigned8 Spare = new Unsigned8();
	public final Unsigned16 MinRowHeight = new Unsigned16();
	public final Unsigned16[] Spares = array(new Unsigned16[1]);
	/**
	 * Use getStyleColor1 for access.
	 */
	@Deprecated
	public final Unsigned32 StyleColor1 = new Unsigned32();
	/**
	 * Use getStyleColor2 for access.
	 */
	@Deprecated
	public final Unsigned32 StyleColor2 = new Unsigned32();
	public final COLOR_VALUE InnerBorderColor = inner(new COLOR_VALUE());
	public final Unsigned16 NameLength = new Unsigned16();
	public final Unsigned16 ImagePacketLength = new Unsigned16();
	public final Unsigned16 RowLabelDataLength = new Unsigned16();

	static {
		addVariableString("Name", "NameLength");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	public Color getStyleColor1() {
		int red = getData().get(getData().position() + 20) & 0xFF;
		int green = getData().get(getData().position() + 21) & 0xFF;
		int blue = getData().get(getData().position() + 22) & 0xFF;
		return new Color(red, green, blue);
	}

	public Color getStyleColor2() {
		int red = getData().get(getData().position() + 24) & 0xFF;
		int green = getData().get(getData().position() + 25) & 0xFF;
		int blue = getData().get(getData().position() + 26) & 0xFF;
		return new Color(red, green, blue);
	}

	public String getName() {
		return (String) getVariableElement("Name");
	}
}
