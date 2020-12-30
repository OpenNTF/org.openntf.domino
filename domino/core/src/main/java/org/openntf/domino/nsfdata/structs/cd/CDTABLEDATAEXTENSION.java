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

import org.openntf.domino.nsfdata.structs.COLOR_VALUE;
import org.openntf.domino.nsfdata.structs.FONTID;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This record was added because the Pre Table Begin Record can not be expanded and R6 required more data to be stored. (editods.h)
 * 
 * @since Lotus Notes/Domino 6.0
 *
 */
@SuppressWarnings("nls")
public class CDTABLEDATAEXTENSION extends CDRecord {

	public final WSIG Header = inner(new WSIG());
	public final Unsigned32 dwColumnSizeToFitBits1 = new Unsigned32();
	public final Unsigned32 dwColumnSizeToFitBits2 = new Unsigned32();
	public final Unsigned16 wEqualSizeTabsWidthX = new Unsigned16();
	public final Unsigned16 wTabsIndentWidthX = new Unsigned16();
	public final Unsigned16 wAvailable3 = new Unsigned16();
	public final Unsigned16 wAvailable4 = new Unsigned16();
	public final Unsigned32 dwAvailable5 = new Unsigned32();
	public final Unsigned32 dwAvailable6 = new Unsigned32();
	public final Unsigned32 dwAvailable7 = new Unsigned32();
	public final Unsigned32 dwAvailable8 = new Unsigned32();
	public final Unsigned32 dwAvailable9 = new Unsigned32();
	public final Unsigned16 wcTabLabelFont = new Unsigned16();
	public final Unsigned16 wAvailableLength11 = new Unsigned16();
	public final Unsigned16 wAvailableLength12 = new Unsigned16();
	public final Unsigned16 wExtension2Length = new Unsigned16();
	public final FONTID FontID = inner(new FONTID());
	public final Unsigned32 FontSpare = new Unsigned32();

	static {
		// The COLOR_VALUE is missing from data created by some R6 beta releases
		addVariableArray("FontColor", "getFontColorCount", COLOR_VALUE.class);

		addVariableData("Available11", "wAvailableLength11");
		addVariableData("Available12", "wAvailableLength12");
		addVariableData("Extension2", "wExtension2Length");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	public int getFontColorCount() {
		return wcTabLabelFont.get() > 8 ? 1 : 0;
	}

	public COLOR_VALUE getFontColor() {
		COLOR_VALUE[] fontColor = (COLOR_VALUE[]) getVariableElement("FontColor");
		return fontColor.length > 0 ? fontColor[0] : null;
	}

	public byte[] getAvailable11() {
		return (byte[]) getVariableElement("Available11");
	}

	public byte[] getAvailable12() {
		return (byte[]) getVariableElement("Available12");
	}

	public byte[] getExtension2() {
		return (byte[]) getVariableElement("Extension2");
	}
}
