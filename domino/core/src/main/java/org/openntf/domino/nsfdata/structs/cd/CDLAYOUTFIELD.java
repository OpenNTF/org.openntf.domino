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

import org.openntf.domino.nsfdata.structs.BSIG;
import org.openntf.domino.nsfdata.structs.ELEMENTHEADER;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * A field in a layout region of a form is defined by a CDLAYOUTFIELD record. This record must be between a CDLAYOUT record and a
 * CDLAYOUTEND record. This record is usually followed by other CD records identifying text, graphical, or action elements associated with
 * the field. (editods.h)
 * 
 * @since Lotus Notes 4.1
 *
 */
public class CDLAYOUTFIELD extends CDRecord {
	/**
	 * These flags are set in the "Flags" field of a CDLAYOUTFIELD record, and control options for operation of the field in the layout
	 * region. (editods.h)
	 * 
	 * @since Lotus Notes 4.1
	 *
	 */
	public static enum Flag {
		/**
		 * Draw the border with a single line.
		 */
		SINGLELINE(0x00000001),
		/**
		 * Allow vertical scrolling in the field.
		 */
		VSCROLL(0x00000002),
		/**
		 * Allow multiple selections within the field if the field is a listbox or checkbox.
		 * 
		 * Note: This must not be sampled by any design mode code. It is, in effect, "write only" for design elements. Play mode elements,
		 * on the other hand, can rely on its value.
		 */
		MULTISEL(0x00000004),
		/**
		 * Field content is static (cannot be edited).
		 */
		STATIC(0x00000008),
		/**
		 * Do not draw a border.
		 */
		NOBORDER(0x00000010),
		/**
		 * Field is displayed as an image, not as text.
		 */
		IMAGE(0x00000020),
		/**
		 * Text Left to Right
		 */
		LTR(0x01000000),
		/**
		 * Text Right to Left
		 */
		RTL(0x02000000),
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
		 * Center field contents vertically.
		 */
		VCENTER(0x80000000);

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

	public static enum FieldType {
		TEXT, CHECK, RADIO, LIST, COMBO;
	}

	public final BSIG Header = inner(new BSIG());
	public final ELEMENTHEADER ElementHeader = inner(new ELEMENTHEADER());
	/**
	 * Use getFlags for access.
	 */
	@Deprecated
	public final Unsigned32 Flags = new Unsigned32();
	// TODO figure out why some values are way out of range - unless isPacked should be false
	//	public final Enum8<FieldType> bFieldType = new Enum8<FieldType>(FieldType.values());
	public final Unsigned8 bFieldType = new Unsigned8();
	public final Unsigned8[] Reserved = array(new Unsigned8[15]);

	@Override
	public SIG getHeader() {
		return Header;
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf((int) Flags.get());
	}

}
