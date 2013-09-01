package org.openntf.domino.design.cd;

import java.util.Arrays;

public class CDFILESEGMENT {
	public static final int LENGTH = 18;

	final LSIG Header;
	final short DataSize; // WORD
	final short SegSize; // WORD
	final int Flags; // DWORD
	final int Reserved; // DWORD

	byte[] data;

	public CDFILESEGMENT(final byte[] data) {
		Header = new LSIG(Arrays.copyOfRange(data, 0, 6));
		DataSize = CDUtils.wordToShort(Arrays.copyOfRange(data, 6, 8));
		SegSize = CDUtils.wordToShort(Arrays.copyOfRange(data, 8, 10));
		Flags = CDUtils.dwordToInt(Arrays.copyOfRange(data, 10, 14));
		Reserved = CDUtils.dwordToInt(Arrays.copyOfRange(data, 14, 18));
	}

	public byte[] getBytes() {
		byte[] result = new byte[LENGTH + data.length];
		int offset = 0;

		for (byte aByte : Header.getBytes()) {
			result[offset++] = aByte;
		}

		for (byte aByte : CDUtils.toWord(DataSize)) {
			result[offset++] = aByte;
		}
		for (byte aByte : CDUtils.toWord(SegSize)) {
			result[offset++] = aByte;
		}
		for (byte aByte : CDUtils.toWord(Flags)) {
			result[offset++] = aByte;
		}
		for (byte aByte : CDUtils.toWord(Reserved)) {
			result[offset++] = aByte;
		}

		for (byte aByte : data) {
			result[offset++] = aByte;
		}

		return result;
	}
}