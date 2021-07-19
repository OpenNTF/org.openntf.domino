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

import org.openntf.domino.nsfdata.structs.FONTID;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This structure defines the appearance of a button in a rich text field. (editods.h)
 *
 */
@SuppressWarnings("nls")
public class CDBUTTON extends CDRecord {
	/**
	 * Possible values for the Flags member of the CDBUTTON structure.
	 * 
	 * @since Lotus Notes/Domino 5.0.3
	 *
	 */
	public static enum Flag {
		/**
		 * Button is unused.
		 */
		UNUSED((short) 0x0000),
		/**
		 * Script is attached to this button.
		 */
		RUNFLAG_SCRIPT((short) 0x0001),
		/**
		 * Works with width flags BUTTON_RUNFLAG_FIXED, BUTTON_FUNFLAG_MINIMUM, BUTTON_RUNFLAG_CONTENT and BUTTON_RUNFLAG_WIDTHMASK.. If
		 * text doesn't "fit" in the specified width, determines if text should be wrapped (and button made taller) or not.
		 */
		RUNFLAG_NOWRAP((short) 0x0002),
		/**
		 * Button uses right-to-left reading order.
		 */
		RUNFLAG_RTL((short) 0x0100),
		/**
		 * Button has a fixed width of specified size.
		 */
		RUNFLAG_FIXED((short) 0x0200),
		/**
		 * Button has a fixed width of specified size UNLESS text is too wide to fit in which case it's made wider to accommodate the text.
		 */
		RUNFLAG_MINIMUM((short) 0x0400),
		/**
		 * Button is as wide as is necessary to accommodate the text.
		 */
		RUNFLAG_CONTENT((short) 0x0800),
		/**
		 * Button width is a fixed number of characters.
		 */
		RUNFLAG_PROPORTIONAL((short) 0x4000),
		/**
		 * Button has focus.
		 */
		FOCUS_ON((short) 0x8000),
		/**
		 * Button edges are rounded.
		 */
		EDGE_ROUNDED((short) 0x1000),
		/**
		 * Button edges are square.
		 */
		EDGE_SQUARE((short) 0x2000);

		/**
		 * Determines which of the flag bits are written to disk as opposed to just being used while the button "is running" to record the
		 * current state of the button (e.g., if the button has been pressed and is currently executing script bits). In other words, it's
		 * the AND mask for all the bits that are going to be written to disk.
		 */
		public static final short ODS_MASK = 0x7F02;
		/**
		 * AND mask to see if BUTTON_RUNFLAG_FIXED, BUTTON_RUNFLAG_MINIMUM, BUTTON_RUNFLAG_CONTENT, BUTTON_RUNFLAG_NOWRAP and
		 * BUTTON_RUNFLAG_PROPORTIONAL flags are set. If none of these flags are set, the button is "MAXIMUM" size. "Maximum" means the
		 * button is as wide as is necessary to accommodate the text up-to the specified maximum width. If the text doesn't fit at the
		 * maximum width, it's wrapped onto a second (third, fourth, ...) line and the button is made correspondingly taller. Minimum
		 * allowed value is "2.00".
		 */
		public static final short RUNFLAG_WIDTH_MASK = (short) (RUNFLAG_FIXED.getValue() | RUNFLAG_MINIMUM.getValue()
				| RUNFLAG_CONTENT.getValue() | RUNFLAG_PROPORTIONAL.getValue());

		private final short value_;

		private Flag(final short value) {
			value_ = value;
		}

		public short getValue() {
			return value_;
		}

		public static Set<Flag> valuesOf(final short flags) {
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
	/**
	 * Use getFlags for access.
	 */
	@Deprecated
	public final Unsigned16 Flags = new Unsigned16();
	public final Unsigned16 Width = new Unsigned16();
	public final Unsigned16 Height = new Unsigned16();
	public final Unsigned16 Lines = new Unsigned16();
	public final FONTID FontID = inner(new FONTID());

	static {
		addVariableString("Text", "getTextLen");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf((short) Flags.get());
	}

	public int getTextLen() {
		return (int) (Header.getRecordLength() - 8 - FontID.size());
	}

	public String getText() {
		return (String) getVariableElement("Text");
	}
}
