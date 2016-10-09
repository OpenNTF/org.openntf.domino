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