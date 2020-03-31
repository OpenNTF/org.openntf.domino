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
package org.openntf.domino.nsfdata.structs;

import java.util.EnumSet;
import java.util.Set;

/**
 * This structure contains the view table format descriptor. All view notes contain a $VIEWFORMAT item (also known as a "View Table Format"
 * item). A $VIEWFORMAT item is an item of TYPE_VIEW_FORMAT with item name VIEW_VIEW_FORMAT_ITEM. The item value of a $VIEWFORMAT item
 * consists of a single VIEW_TABLE_FORMAT structure, followed by one VIEW_COLUMN_FORMAT structure for each column, followed by an item
 * name/formula/column title set for each column, followed by a VIEW_TABLE_FORMAT2 structure, followed by one VIEW_COLUMN_FORMAT2 structure
 * for each column, followed by a VIEW_TABLE_FORMAT3 structure. (viewfmt.h)
 * 
 * @since forever
 *
 */
public class VIEW_TABLE_FORMAT extends AbstractStruct {
	/**
	 * VIEW_TABLE_xxx
	 */
	public static enum Flag {
		COLLAPSED(0x0001), FLATINDEX(0x0002), DISP_ALLUNREAD(0x0004), CONFLICT(0x0008), DISP_UNREADDOCS(0x0010), GOTO_TOP_ON_OPEN(0x0020),
		GOTO_BOTTOM_ON_OPEN(0x0040), ALTERNATE_ROW_COLORING(0x0080), HIDE_HEADINGS(0x0100), HIDE_LEFT_MARGIN(0x0200),
		SIMPLE_HEADINGS(0x0400), VARIABLE_LINE_COUNT(0x0800), GOTO_TOP_ON_REFRESH(0x1000), GOTO_BOTTOM_ON_REFRESH(0x2000),
		EXTEND_LAST_COLUMN(0x04000), RTLVIEW(0x8000);

		private final short value_;

		private Flag(final short value) {
			value_ = value;
		}

		private Flag(final int value) {
			value_ = (short) value;
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

	/**
	 * VIEW_TABLE_xxx
	 */
	public static enum Flag2 {
		FLAT_HEADINGS(0x0001), COLORIZE_ICONS(0x0002), HIDE_SB(0x0004), HIDE_CAL_HEADER(0x0008), NOT_CUSTOMIZED(0x0010),
		SHOW_PARTIAL_THREADS(0x0020), PARTIAL_FLATINDEX(0x0020);

		private final short value_;

		private Flag2(final short value) {
			value_ = value;
		}

		private Flag2(final int value) {
			value_ = (short) value;
		}

		public short getValue() {
			return value_;
		}

		public static Set<Flag2> valuesOf(final short flags) {
			Set<Flag2> result = EnumSet.noneOf(Flag2.class);
			for (Flag2 flag : values()) {
				if ((flag.getValue() & flags) > 0) {
					result.add(flag);
				}
			}
			return result;
		}
	}

	public final VIEW_FORMAT_HEADER Header = inner(new VIEW_FORMAT_HEADER());
	public final Unsigned16 Columns = new Unsigned16();
	public final Unsigned16 ItemSequenceNumber = new Unsigned16();
	/**
	 * Use getFlags() for access
	 */
	@Deprecated
	public final Unsigned16 Flags = new Unsigned16();
	/**
	 * Use getFlags2() for access
	 */
	@Deprecated
	public final Unsigned16 Flags2 = new Unsigned16();

	public Set<Flag> getFlags() {
		return Flag.valuesOf((short) Flags.get());
	}

	public Set<Flag2> getFlags2() {
		return Flag2.valuesOf((short) Flags2.get());
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Header=" + Header + ", Columns=" + Columns.get() + ", ItemSequenceNumber="
				+ ItemSequenceNumber.get() + ", Flags=" + getFlags() + ", Flags2=" + getFlags2() + "]";
	}
}
