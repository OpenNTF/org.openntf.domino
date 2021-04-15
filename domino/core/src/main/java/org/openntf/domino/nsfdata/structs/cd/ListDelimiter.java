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

public enum ListDelimiter {
	SPACE((short) 0x0001), COMMA((short) 0x0002), SEMICOLON((short) 0x0004), NEWLINE((short) 0x0008), BLANKLINE((short) 0x0010),
	D_SPACE((short) 0x1000), D_COMMA((short) 0x2000), D_SEMICOLON((short) 0x3000), D_NEWLINE((short) 0x4000), D_BLANKLINE((short) 0x5000);

	public static final int LD_MASK = 0x0fff;
	public static final int LDD_MASK = 0xf000;

	private final short value_;

	private ListDelimiter(final short value) {
		value_ = value;
	}

	public short getValue() {
		return value_;
	}

	public static Set<ListDelimiter> valuesOf(final int flags) {
		Set<ListDelimiter> result = EnumSet.noneOf(ListDelimiter.class);
		for (ListDelimiter flag : values()) {
			if ((flag.getValue() & flags) > 0) {
				result.add(flag);
			}
		}
		return result;
	}
}