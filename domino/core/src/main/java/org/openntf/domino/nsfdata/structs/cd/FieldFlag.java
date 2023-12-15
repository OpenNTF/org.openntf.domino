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

public enum FieldFlag {
	/**
	 * Field contains read/writers
	 */
	READWRITERS((short) 0x0001),
	/**
	 * Field is editable, not read only
	 */
	EDITABLE((short) 0x0002),
	/**
	 * Field contains distinguished names
	 */
	NAMES((short) 0x0004),
	/**
	 * Store DV, even if not spec'ed by user
	 */
	STOREDV((short) 0x0008),
	/**
	 * Field contains document readers
	 */
	READERS((short) 0x0010),
	/**
	 * Field contains a section
	 */
	SECTION((short) 0x0020),
	/**
	 * can be assumed to be clear in memory, V3 & later
	 */
	SPARE3((short) 0x0040),
	/**
	 * IF CLEAR, CLEAR AS ABOVE
	 */
	V3FAB((short) 0x0080),
	/**
	 * Field is a computed field
	 */
	COMPUTED((short) 0x0100),
	/**
	 * Field is a keywords field
	 */
	KEYWORDS((short) 0x0200),
	/**
	 * Field is protected
	 */
	PROTECTED((short) 0x0400),
	/**
	 * Field name is simply a reference to a shared field note
	 */
	REFERENCE((short) 0x0800),
	/**
	 * sign field
	 */
	SIGN((short) 0x1000),
	/**
	 * seal field
	 */
	SEAL((short) 0x2000),
	/**
	 * standard UI
	 */
	KEYWORDS_UI_STANDARD((short) 0x0000),
	/**
	 * checkbox UI
	 */
	KEYWORDS_UI_CHECKBOX((short) 0x4000),
	/**
	 * radiobutton UI
	 */
	KEYWORDS_UI_RADIOBUTTON((short) 0x8000),
	/**
	 * allow doc editor to add new values
	 */
	KEYWORDS_UI_ALLOW_NEW((short) 0xc000);

	private final short value_;

	private FieldFlag(final short value) {
		value_ = value;
	}

	public short getValue() {
		return value_;
	}

	public static Set<FieldFlag> valuesOf(final short flags) {
		Set<FieldFlag> result = EnumSet.noneOf(FieldFlag.class);
		for (FieldFlag flag : values()) {
			if ((flag.getValue() & flags) > 0) {
				result.add(flag);
			}
		}
		return result;
	}
}