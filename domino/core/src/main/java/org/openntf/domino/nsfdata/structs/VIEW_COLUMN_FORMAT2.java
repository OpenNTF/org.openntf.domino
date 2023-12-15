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
package org.openntf.domino.nsfdata.structs;

import java.util.EnumSet;
import java.util.Set;

/**
 * This structure describes the format of one column in a view as of Notes Release 4.
 * 
 * @since Lotus Notes 4.0
 *
 */
public class VIEW_COLUMN_FORMAT2 extends AbstractStruct {
	public static enum Flag3 {
		FlatInV5(0x0001), CaseSensitiveSortInV5(0x0002), AccentSensitiveSortInV5(0x0004), HideWhenFormula(0x008), TwistieResource(0x0010),
		Color(0x0020), ExtDate(0x0040), NumberFormat(0x0080), IsColumnEditable(0x0100), UserDefinableColor(0x0200), HideInR5(0x0400),
		NamesFormat(0x0800), HideColumnTitle(0x1000), IsSharedColumn(0x2000), UseShareColumnFormulaOnly(0x4000),
		ExtendedViewColFmt6(0x8000);

		private final short value_;

		private Flag3(final int value) {
			value_ = (short) value;
		}

		public short getValue() {
			return value_;
		}

		public static Set<Flag3> valuesOf(final short flags) {
			Set<Flag3> result = EnumSet.noneOf(Flag3.class);
			for (Flag3 flag : values()) {
				if ((flag.getValue() & flags) > 0) {
					result.add(flag);
				}
			}
			return result;
		}
	}

	public final Unsigned16 Signature = new Unsigned16();
	public final FONTID HeaderFontID = inner(new FONTID());
	public final UNIVERSALNOTEID ResortToViewUNID = inner(new UNIVERSALNOTEID());
	public final Unsigned16 wSecondResortColumnIndex = new Unsigned16();
	/**
	 * Use getFlags3() for access
	 */
	@Deprecated
	public final Unsigned16 Flags3 = new Unsigned16();
	public final Unsigned16 wHideWhenFormulaSize = new Unsigned16();
	public final Unsigned16 wTwistieResourceSize = new Unsigned16();
	public final Unsigned16 wCustomOrder = new Unsigned16();
	public final Unsigned16 wCustomHiddenFlags = new Unsigned16();
	public final COLOR_VALUE ColumnColor = inner(new COLOR_VALUE());
	public final COLOR_VALUE HeaderFontColor = inner(new COLOR_VALUE());

	public Set<Flag3> getFlags3() {
		return Flag3.valuesOf((short) Flags3.get());
	}
}
