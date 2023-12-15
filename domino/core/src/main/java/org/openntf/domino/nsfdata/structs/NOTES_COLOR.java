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

public class NOTES_COLOR {

	/**
	 * These symbols are used to specify text color, graphic color and background color in a variety of C API structures. (colorid.h)
	 *
	 */
	@SuppressWarnings("nls")
	public static enum StandardColor {
		BLACK((short) 0), WHITE((short) 1), RED((short) 2), GREEN((short) 3), BLUE((short) 4), MAGENTA((short) 5), YELLOW((short) 6),
		CYAN((short) 7), DKRED((short) 8), DKGREEN((short) 9), DKBLUE((short) 10), DKMAGENTA((short) 11), DKYELLOW((short) 12),
		DKCYAN((short) 13), GRAY((short) 14), LTGRAY((short) 15);

		private final short value_;

		private StandardColor(final short value) {
			value_ = value;
		}

		public short getValue() {
			return value_;
		}

		public static StandardColor valueOf(final short typeCode) {
			for (StandardColor type : values()) {
				if (type.getValue() == typeCode) {
					return type;
				}
			}
			throw new IllegalArgumentException("No matching StandardColor found for type code " + typeCode);
		}
	}

	private final short value_;

	public NOTES_COLOR(final short value) {
		value_ = value;
	}

	public StandardColor getStandardColor() {
		try {
			return StandardColor.valueOf(value_);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	public boolean isStandard() {
		return getStandardColor() != null;
	}

	public boolean isPassThrough() {
		return value_ == -1;
	}

	public short getValue() {
		return value_;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": value=" + value_ + ", StandardColor=" + getStandardColor() + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}
}
