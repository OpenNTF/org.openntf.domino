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

import org.openntf.domino.nsfdata.structs.BSIG;
import org.openntf.domino.nsfdata.structs.ELEMENTHEADER;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * A text element in a layout region of a form is defined by a CDLAYOUTTEXT record. This record must be between a CDLAYOUT record and a
 * CDLAYOUTEND record. This record is usually followed by other CD records identifying text, graphical, or action elements associated with
 * the element. (editods.h)
 * 
 * @since Lotus Notes 4.1
 *
 */
public class CDLAYOUTTEXT extends CDRecord {
	/**
	 * These flags are set in the "Flags" field of a CDLAYOUTTEXT record, and control operation of the field in the layout region.
	 * 
	 * @since Lotus Notes 4.1
	 *
	 */
	public static enum Flag {
		/**
		 * Display with a transparent background.
		 */
		TRANS(0x10000000),
		/**
		 * Left-justify text.
		 */
		LEFT(0x00000000),
		/**
		 * Center text.
		 */
		CENTER(0x20000000),
		/**
		 * Right-justify text.
		 */
		RIGHT(0x40000000),
		/**
		 * Mask used to obtain only the text alignment bits.
		 */
		ALIGN_MASK(0x60000000),
		/**
		 * Center field contents vertically.
		 */
		VCENTER(0x80000000),
		/**
		 * Left to right order text.
		 */
		LTR(0x01000000),
		/**
		 * Right to left order text.
		 */
		RTL(0x02000000);

		/**
		 * Read only mask.
		 */
		public static final int RO_MASK = 0x03000000;
		/**
		 * Mask used to obtain only the valid text layout flag bits.
		 */
		public static final int FLAGS_MASK = 0xF0000000;

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

	public final BSIG Header = inner(new BSIG());
	public final ELEMENTHEADER ElementHeader = inner(new ELEMENTHEADER());
	/**
	 * Use getFlags for access.
	 */
	@Deprecated
	public final Unsigned32 Flags = new Unsigned32();
	public final Unsigned8[] Reserved = array(new Unsigned8[16]);

	static {
		addVariableString("Text", "getTextSize");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf((int) Flags.get());
	}

	public int getTextSize() {
		return (int) (Header.getRecordLength() - Header.size() - ElementHeader.getStructSize() - 20);
	}

	/**
	 * This record may be followed by 8-bit text data, if the record was created by an early Test Build of Notes Release 4.0. Normally,
	 * other CD records will follow containing the text and graphical elements associated with this text element.
	 * 
	 * @since Lotus Notes 4.0 Beta Build 134
	 * @deprecated Only applies to records created by early Test Builds of Lotus Notes 4.0
	 */
	@Deprecated
	public String getText() {
		return (String) getVariableElement("Text");
	}

	//	@Override
	//	public String toString() {
	//		return "[" + getClass().getSimpleName() + ": ElementHeader=" + getElementHeader() + ", Flags=" + getFlags() + ", Text=" + getText()
	//				+ "]";
	//	}
}
