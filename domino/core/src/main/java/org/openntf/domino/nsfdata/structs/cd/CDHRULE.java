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

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * Specifies a horizontal line (editods.h)
 * 
 * @since Lotus Notes/Domino 4.6
 *
 */
public class CDHRULE extends CDRecord {
	/**
	 * Flag values stored in the Flags field of the CDHRULE record. (editods.h)
	 * 
	 * @since Lotus Notes/Domino 4.6
	 *
	 */
	public static enum Flag {
		/**
		 * Use the color specified in the record when drawing the line.
		 */
		USECOLOR(0x00000001),
		/**
		 * Use the gradient color specified in the record when drawing the line.
		 */
		USEGRADIENT(0x00000002),
		/**
		 * Fit the horizontal length of the line to the window where the document is being displayed.
		 */
		FITTOWINDOW(0x00000004),
		/**
		 * Do not display 3D effects when drawing the line.
		 */
		NOSHADOW(0x00000008);

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
	/**
	 * Use getFlags for access.
	 */
	@Deprecated
	public final Unsigned32 Flags = new Unsigned32();
	public final Unsigned16 Width = new Unsigned16();
	public final Unsigned16 Height = new Unsigned16();
	public final Unsigned16 Color = new Unsigned16();
	public final Unsigned16 GradientColor = new Unsigned16();

	/**
	 * Default value for Height member of the CDRULE structure in TWIPS.
	 */
	public static final short DEFAULTHRULEHEIGHT = 7;
	/**
	 * Default value for Width member of the CDRULE structure in TWIPS.
	 */
	public static final short DEFAULTHRULEWIDTH = 720;

	@Override
	public SIG getHeader() {
		return Header;
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf((int) Flags.get());
	}

	//	@Override
	//	public String toString() {
	//		return "[" + getClass().getSimpleName() + ": Flags=" + getFlags() + ", Width=" + getWidth() + ", Height=" + getHeight()
	//				+ ", Color=" + getColor() + ", GradientColor=" + getGradientColor() + "]";
	//}
}
