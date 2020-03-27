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

import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.COLOR_VALUE;
import org.openntf.domino.nsfdata.structs.FONTID;
import org.openntf.domino.nsfdata.structs.RepeatType;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This CD Record defines the attributes of an embedded outline. It is preceded by a CDHOTSPOTBEGIN and a CDPLACEHOLDER. The CD record,
 * CDPLACEHOLDER, further defines the CDEMBEDDEDOUTLINE. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDEMBEDDEDOUTLINE extends CDRecord {
	/**
	 * (editods.h)
	 * 
	 * @since Lotus Notes/Domino 5.0
	 *
	 */
	public static enum Flag {
		/** Embedded outline is displayed horizontally. */
		DISPLAYHORZ(0x00000001),
		/** Embedded outline is a background image. */
		HASIMAGELABEL(0x00000002),
		/** Embedded outline is a tile image. */
		TITLEIMAGE(0x00000004),
		/** Embedded outline use applet in browser. */
		USEAPPLET_INBROWSER(0x00000008),
		/** Embedded outline is a title. */
		TYPE_TITLE(0x00000010),
		/** Embedded outline show twistie. */
		SHOWTWISTIE(0x00000020),
		/** Embedded outline title is fixed. */
		TITLEFIXED(0x00000040),
		/** Embedded outline top level is fixed. */
		TOPLEVELFIXED(0x00000080),
		/** Embedded outline sub level is fixed. */
		SUBLEVELFIXED(0x00000100),
		/** Embedded outline is tree style. */
		TREE_STYLE(0x00000200),
		/** Embedded outline has name. */
		HASNAME(0x00000400),
		/** Embedded outline has target frame. */
		HASTARGETFRAME(0x00000800),
		/** Embedded outline is all the same. */
		ALLTHESAME(0x00001000),
		/** Embedded outline back is all the same. */
		BACK_ALLTHESAME(0x00002000),
		/** Embedded outline expand data. */
		EXPAND_DATA(0x00004000),
		/** Embedded outline expand all. */
		EXPAND_ALL(0x00008000),
		/** Embedded outline expand first. */
		EXPAND_FIRST(0x00010000),
		/** Embedded outline expand save. */
		EXPAND_SAVED(0x00020000),
		/** Embedded outline expand none. */
		EXPAND_NONE(0x00040000),
		/** Embedded outline has root name. */
		HASROOTNAME(0x00080000),
		/** Embedded outline has right to left reading order. */
		RTLREADING(0x00100000),
		/** Embedded outline has twistie image. */
		TWISTIEIMAGE(0x00200000),
		/** Embedded outline displays unread count in folder. */
		HANDLEFOLDERUNREAD(0x00400000),
		/** Embedded outline has an OS style twistie. */
		NEWSTYLE_TWISTIE(0x00800000),
		/** Embedded outline maintains the folder's unread marks. */
		MAINTAINFOLDERUNREAD(0x01000000),
		/** Use JavaScript (Dojo) control in the browser. */
		USEJSCTLINBROWSER(0x02000000),
		/** Use custom JavasScript control in the browser */
		USECUSTOMJSINBROWSER(0x04000000);

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

	public static enum ImageAlign {
		TOPLEFT, TOPCENTER, TOPRIGHT, MIDDLELEFT, MIDDLECENTER, MIDDLERIGHT, BOTTOMLEFT, BOTTOMCENTER, BOTTOMRIGHT
	}

	public static enum TitleStyle {
		HIDE, SIMPLE, HIERARCHICAL
	}

	public final WSIG Header = inner(new WSIG());
	/**
	 * Use getFlags for access.
	 */
	@Deprecated
	public final Unsigned32 Flags = new Unsigned32();
	public final Unsigned32[] Unused = array(new Unsigned32[3]);
	public final Unsigned16 Align = new Unsigned16();
	public final Unsigned16 SpaceBetweenEntries = new Unsigned16();
	public final Unsigned16 LabelLength = new Unsigned16();
	public final Enum16<TitleStyle> Style = new Enum16<TitleStyle>(TitleStyle.values());
	public final Unsigned16 Title_VOffset = new Unsigned16();
	public final Unsigned16 Title_HOffset = new Unsigned16();
	public final Unsigned16 Title_Height = new Unsigned16();
	public final Unsigned16 TopLevel_VOffset = new Unsigned16();
	public final Unsigned16 TopLevel_HOffset = new Unsigned16();
	public final Unsigned16 TopLevel_Height = new Unsigned16();
	public final Unsigned16 SubLevel_VOffset = new Unsigned16();
	public final Unsigned16 SubLevel_HOffset = new Unsigned16();
	public final Unsigned16 SubLevel_Height = new Unsigned16();
	public final Unsigned16 NameLength = new Unsigned16();
	public final Unsigned16 TargetFrameLength = new Unsigned16();
	public final FONTID[] SelectFontID = array(new FONTID[3]);
	public final FONTID[] MouseFontID = array(new FONTID[3]);
	public final Unsigned16[] Font_VOffset = array(new Unsigned16[3]);
	public final Unsigned16[] Font_HOffset = array(new Unsigned16[3]);
	public final Enum16<ImageAlign> Align1 = new Enum16<ImageAlign>(ImageAlign.values());
	public final Enum16<ImageAlign> Align2 = new Enum16<ImageAlign>(ImageAlign.values());
	public final Enum16<ImageAlign> Align3 = new Enum16<ImageAlign>(ImageAlign.values());
	public final COLOR_VALUE Control_BackColor = inner(new COLOR_VALUE());
	public final COLOR_VALUE[] BackColor = array(new COLOR_VALUE[9]);
	public final COLOR_VALUE[] SelectBackColor = array(new COLOR_VALUE[3]);
	public final Enum16<RepeatType> Repeat1 = new Enum16<RepeatType>(RepeatType.values());
	public final Enum16<RepeatType> Repeat2 = new Enum16<RepeatType>(RepeatType.values());
	public final Enum16<RepeatType> Repeat3 = new Enum16<RepeatType>(RepeatType.values());
	public final Enum16<RepeatType> Repeat4 = new Enum16<RepeatType>(RepeatType.values());
	public final Enum16<ImageAlign> Background_Align1 = new Enum16<ImageAlign>(ImageAlign.values());
	public final Enum16<ImageAlign> Background_Align2 = new Enum16<ImageAlign>(ImageAlign.values());
	public final Enum16<ImageAlign> Background_Align3 = new Enum16<ImageAlign>(ImageAlign.values());
	public final Enum16<ImageAlign> Background_Align4 = new Enum16<ImageAlign>(ImageAlign.values());
	public final Unsigned16[] Background_VOffset = array(new Unsigned16[4]);
	public final Unsigned16[] Background_HOffset = array(new Unsigned16[4]);
	public final Unsigned16[] wBackground_Image = array(new Unsigned16[4]);
	public final COLOR_VALUE[] NormalFontColor = array(new COLOR_VALUE[3]);
	public final COLOR_VALUE[] MouseFontColor = array(new COLOR_VALUE[3]);
	public final Unsigned16 RootLength = new Unsigned16();
	public final Unsigned16 TopLevel_PixelHeight = new Unsigned16();
	public final Unsigned16 wColWidth = new Unsigned16();
	public final Unsigned16 SpareWord = new Unsigned16();
	public final Unsigned32[] Spare = array(new Unsigned32[4]);

	@Override
	public SIG getHeader() {
		return Header;
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf((int) Flags.get());
	}
}
