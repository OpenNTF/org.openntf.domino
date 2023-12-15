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

import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.COLOR_VALUE;
import org.openntf.domino.nsfdata.structs.FONTID;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * A CDPLACEHOLDER record stores additional information about various embedded type CD records, such as CDEMBEDDEDCTL, CDEMBEDDEDOUTLINE and
 * other embedded CD record types defined in HOTSPOTREC_TYPE_xxx. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDPLACEHOLDER extends CDRecord {
	public static enum Flag {
		/** Fit to window */
		FITTOWINDOW(0x00000001),
		/** Draw background */
		DRAWBACKGROUND(0x00000002),
		/** Use percentage */
		USEPERCENTAGE(0x00000004),
		/** Scrollbars */
		SCROLLBARS(0x00000008),
		/** Contents only */
		CONTENTSONLY(0x00000010),
		/** Align center */
		ALIGNCENTER(0x00000020),
		/** Align right */
		ALIGNRIGHT(0x00000040),
		/** Fit to window height */
		FITTOWINDOWHEIGHT(0x00000080),
		/** Tile image */
		TILEIMAGE(0x00000100),
		/** Display horizontally */
		DISPLAYHORZ(0x00000200),
		/** Don't expand selections */
		DONTEXPANDSELECTIONS(0x00000400),
		/** Expand current */
		EXPANDCURRENT(0x00000800),
		/** Fit contents width */
		FITCONTENTSWIDTH(0x00001000),
		/** Fixed width */
		FIXEDWIDTH(0x00002000),
		/** Fixed height */
		FIXEDHEIGHT(0x00004000),
		/** Fit contents */
		FITCONTENTS(0x00008000),
		/** Proportional width */
		PROP_WIDTH(0x00010000),
		/** Proportional width and height */
		PROP_BOTH(0x00020000),
		/** Scrollers */
		SCROLLERS(0x00040000);

		private final int value_;

		private Flag(final int value) {
			value_ = value;
		}

		public int getValue() {
			return value_;
		}

		public static Set<Flag> valuesOf(final int flags) {
			Set<Flag> result = EnumSet.noneOf(Flag.class);
			for (Flag flag : values()) {
				if ((flag.getValue() & flags) > 0) {
					result.add(flag);
				}
			}
			return result;
		}
	}

	/**
	 * @since Lotus Notes/Domino 5.0
	 */
	public static enum Alignment {
		LEFT, CENTER, RIGHT
	}

	public final WSIG Header = inner(new WSIG());
	public final Enum16<CDHOTSPOTBEGIN.HotspotType> Type = new Enum16<CDHOTSPOTBEGIN.HotspotType>(CDHOTSPOTBEGIN.HotspotType.values());
	/**
	 * Use getFlags for access.
	 */
	@Deprecated
	public final Unsigned32 Flags = new Unsigned32();
	public final Unsigned16 Width = new Unsigned16();
	public final Unsigned16 Height = new Unsigned16();
	public final FONTID FontID = inner(new FONTID());
	public final Unsigned16 Characters = new Unsigned16();
	public final Unsigned16 SpaceBetween = new Unsigned16();
	public final Enum16<Alignment> TextAlignment = new Enum16<Alignment>(Alignment.values());
	public final Unsigned16 SpaceWord = new Unsigned16();
	public final FONTID[] SubFontID = array(new FONTID[2]);
	public final Unsigned16 DataLength = new Unsigned16();
	public final COLOR_VALUE BackgroundColor = inner(new COLOR_VALUE());
	public final COLOR_VALUE ColorRGB = inner(new COLOR_VALUE());
	public final Unsigned16 SpareWord = new Unsigned16();
	public final Unsigned32[] Spare = array(new Unsigned32[3]);

	@Override
	public SIG getHeader() {
		return Header;
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf((int) Flags.get());
	}

}
