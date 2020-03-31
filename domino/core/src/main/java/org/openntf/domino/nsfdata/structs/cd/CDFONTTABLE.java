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

import java.util.Arrays;

import org.openntf.domino.nsfdata.structs.AbstractStruct;
import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This defines part of the structure of a font table item in a note. A font table item in a note allows rich text in the note to be
 * displayed using fonts other than those defined in FONT_FACE_xxx. (editods.h)
 *
 */
public class CDFONTTABLE extends CDRecord {
	/**
	 * This defines part of the structure of a font table item in a note. A font table item in a note allows rich text in the note to be
	 * displayed using fonts other than those defined in FONT_FACE_xxx. (editods.h)
	 *
	 */
	public static class CDFACE extends AbstractStruct {
		public static enum Pitch {
			DEFAULT((short) 0x00), FIXED((short) 0x01), VARIABLE((short) 0x02), MONO_FONT((short) 0x08);

			private final short value_;

			private Pitch(final short value) {
				value_ = value;
			}

			public short getValue() {
				return value_;
			}
		}

		public static enum FamilyType {
			DONTCARE((short) 0x00), ROMAN((short) 0x10), SWISS((short) 0x20), MODERN((short) 0x30), SCRIPT((short) 0x40),
			DECORATIVE((short) 0x50);

			private final short value_;

			private FamilyType(final short value) {
				value_ = value;
			}

			public short getValue() {
				return value_;
			}
		}

		public static final int MAXFACESIZE = 32;

		public static final int TRUETYPE_FONTTYPE = 0x004;

		public final Unsigned8 Face = new Unsigned8();
		public final Unsigned8 Family = new Unsigned8();
		public final Unsigned8[] Name = array(new Unsigned8[MAXFACESIZE]);

		public boolean isTrueType() {
			return (Family.get() & TRUETYPE_FONTTYPE) > 0;
		}

		public Pitch getPitch() {
			for (Pitch pitch : Pitch.values()) {
				if ((Family.get() & pitch.getValue()) > 0) {
					return pitch;
				}
			}
			return null;
		}

		public FamilyType getFamily() {
			for (FamilyType family : FamilyType.values()) {
				if ((Family.get() & family.getValue()) > 0) {
					return family;
				}
			}
			return null;
		}

		public String getName() {
			return ODSUtils.fromLMBCS(Name);
		}

		@Override
		public String toString() {
			return "[" + getClass().getSimpleName() + ": TrueType=" + isTrueType() + ", Pitch=" + getPitch() + ", Family=" + getFamily()
					+ ", Name=" + getName() + "]";
		}
	}

	public final WSIG Header = inner(new WSIG());
	public final Unsigned16 Fonts = new Unsigned16();

	static {
		addVariableArray("FontFaces", "Fonts", CDFACE.class);
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	public CDFACE[] getFonts() {
		return (CDFACE[]) getVariableElement("FontFaces");
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Fonts=" + Fonts.get() + ", FontFaces=" + Arrays.asList(getFonts()) + "]";
	}
}
