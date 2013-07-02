package org.openntf.domino.design.cd;

import java.util.Arrays;

public class LSIG {
	public static final int LENGTH = 6;

	// Signature indicates the record type. 97 = CD header, 96 = CD segment
	final short Signature; // WORD
	// Length is the length of the record, which is the header + data
	final int Length; // DWORD

	public LSIG(final byte[] data) {
		Signature = CDUtils.wordToShort(Arrays.copyOfRange(data, 0, 2));
		Length = CDUtils.dwordToInt(Arrays.copyOfRange(data, 2, 6));
	}

	public byte[] getBytes() {
		byte[] result = new byte[LENGTH];

		byte[] sig = CDUtils.toWord(Signature);
		result[0] = sig[0];
		result[1] = sig[1];

		byte[] len = CDUtils.toWord(Length);
		result[2] = len[0];
		result[3] = len[1];
		result[4] = len[2];
		result[5] = len[3];

		return result;
	}
}