package org.openntf.domino.nsfdata.structs;

public final class ODSConstants {
	private ODSConstants() { }

	public static enum RecordLength {
		LONG(0x0000), WORD(0xff), BYTE(0);

		private final int value_;

		private RecordLength(final int value) {
			value_ = value;
		}
		public int getValue() {
			return value_;
		}

		public static RecordLength valueOf(final int value) {
			if(value == LONG.getValue()) {
				return LONG;
			} else if(value == WORD.getValue()) {
				return WORD;
			} else {
				return BYTE;
			}
		}
	}
}
