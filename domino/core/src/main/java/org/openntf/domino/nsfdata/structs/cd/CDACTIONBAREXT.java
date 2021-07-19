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

import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.COLOR_VALUE;
import org.openntf.domino.nsfdata.structs.FONTID;
import org.openntf.domino.nsfdata.structs.LENGTH_VALUE;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This CD record defines the Action Bar attributes. It is an extension of the CDACTIONBAR record. It is found within a $V5ACTIONS item and
 * is preceded by a CDACTIONBAR record. (actods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 */
public class CDACTIONBAREXT extends CDRecord {
	public static enum BackgroundRepeat {
		DEFAULT,
		/**
		 * Image repeats once in upper left of action bar
		 */
		REPEATONCE,
		/**
		 * Image repeats vertically along left of action bar
		 */
		REPEATVERT,
		/**
		 * Image repeats horizontally along top of action bar
		 */
		REPEATHORIZ,
		/**
		 * Image "tiles" (repeats) to fit action bar
		 */
		TILE,
		/**
		 * Image is divided and "tiled" (repeated) to fit action bar
		 */
		CENTER_TILE,
		/**
		 * Image is sized to fit action bar
		 */
		REPEATSIZE,
		/**
		 * Image is centered in action bar
		 */
		REPEATCENTER
	}

	public static enum ButtonWidth {
		/**
		 * Width is calculated based on text length and image width
		 */
		DEFAULT,
		/**
		 * Width is at least button background image width or wider if needed to fit text and image
		 */
		BACKGROUND,
		/**
		 * Width is set to value in wBtnWidthAbsolute
		 */
		ABSOLUTE
	}

	public static enum Justify {
		LEFT, CENTER, RIGHT;
	}

	public static enum BorderDisplay {
		ONMOUSEOVER, ALWAYS, NEVER, NOTES
	}

	public static enum Flag {
		/**
		 * Width style is valid
		 */
		WIDTH_STYLE_VALID(0x00000001);

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

	public final WSIG Header = inner(new WSIG());
	public final COLOR_VALUE BackColor = inner(new COLOR_VALUE());
	public final COLOR_VALUE LineColor = inner(new COLOR_VALUE());
	public final COLOR_VALUE FontColor = inner(new COLOR_VALUE());
	public final COLOR_VALUE ButtonColor = inner(new COLOR_VALUE());
	// TODO figure out why some values are way out of range - unless isPacked should be false
	//	public final Enum16<BorderDisplay> BtnBorderDisplay = new Enum16<BorderDisplay>(BorderDisplay.values());
	public final Unsigned16 BtnBorderDisplay = new Unsigned16();
	public final Unsigned16 wAppletHeight = new Unsigned16();
	public final Enum16<BackgroundRepeat> wBarBackgroundRepeat = new Enum16<BackgroundRepeat>(BackgroundRepeat.values());
	// TODO figure out why some values are way out of range - unless isPacked should be false
	//	public final Enum8<ButtonWidth> BtnWidthStyle = new Enum8<ButtonWidth>(ButtonWidth.values());
	public final Unsigned8 BtnWidthStyle = new Unsigned8();
	public final Enum8<Justify> BtnTextJustify = new Enum8<Justify>(Justify.values());
	public final Unsigned16 wBtnWidthAbsolute = new Unsigned16();
	public final Unsigned16 wBtnInternalMargin = new Unsigned16();
	/**
	 * Use getFlags for access.
	 */
	@Deprecated
	public final Unsigned32 dwFlags = new Unsigned32();
	public final FONTID barFontID = inner(new FONTID());
	public final LENGTH_VALUE barHeight = inner(new LENGTH_VALUE());
	public final Unsigned32[] Spare = array(new Unsigned32[12]);

	/**
	 * @return See ACTIONBAREXT_xxx flags
	 */
	public Set<Flag> getFlags() {
		return Flag.valuesOf((int) dwFlags.get());
	}

	@Override
	public SIG getHeader() {
		return Header;
	}
}
