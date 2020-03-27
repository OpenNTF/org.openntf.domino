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

import org.openntf.domino.nsfdata.structs.LSIG;
import org.openntf.domino.nsfdata.structs.RECTSIZE;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * A rich text field may contain a bitmap image. There are three types, monochrome, 8-bit mapped color, and 16-bit color; a gray scale
 * bitmap is stored as an 8-bit color bitmap with a color table having entries [0, 0, 0], [1, 1, 1], . . . , [255, 255, 255]. All bitmaps
 * are stored as a single plane (some graphics devices support multiple planes). (editods.h)
 *
 */
public class CDBITMAPHEADER extends CDRecord {
	/**
	 * Option flags set in the CDBITMAPHEADER record
	 *
	 */
	public static enum Flag {
		/**
		 * Bitmap uses >16 colors or >4 grey scale levels
		 */
		REQUIRES_PALETTE((short) 1),
		/**
		 * Initialized by import code for "first time" importing of bitmaps from clipboard or file, to tell Notes that it should compute
		 * whether or not to use a color palette or not. All imports and API programs should initially set this bit to let the Editor
		 * compute whether it needs the palette or not.
		 */
		COMPUTE_PALETTE((short) 2);

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

	public final LSIG Header = inner(new LSIG());
	public final RECTSIZE Dest = inner(new RECTSIZE());
	public final RECTSIZE Crop = inner(new RECTSIZE());
	/**
	 * Use getFlags for access.
	 */
	@Deprecated
	public final Unsigned16 Flags = new Unsigned16();
	public final Unsigned16 wReserved = new Unsigned16();
	public final Unsigned32 lReserved = new Unsigned32();
	public final Unsigned16 Width = new Unsigned16();
	public final Unsigned16 Height = new Unsigned16();
	public final Unsigned16 BitsPerPixel = new Unsigned16();
	public final Unsigned16 SamplesPerPixel = new Unsigned16();
	public final Unsigned16 BitsPerSample = new Unsigned16();
	public final Unsigned16 SegmentCount = new Unsigned16();
	public final Unsigned16 ColorCount = new Unsigned16();
	public final Unsigned16 PatternCount = new Unsigned16();

	@Override
	public SIG getHeader() {
		return Header;
	}

	/**
	 * If CDBITMAP_FLAG_REQUIRES_PALETTE is set, the color table is required
	 * 
	 * @return CDBITMAP_FLAGS (version 2 and later)
	 */
	public Set<Flag> getFlags() {
		return Flag.valuesOf((short) Flags.get());
	}
}
