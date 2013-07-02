package org.openntf.domino.design.cd;

import java.util.Arrays;

public class CDFILEHEADER {
	public static final int LENGTH = 24;

	final LSIG Header;
	final short FileExtLen; // WORD
	final int FileDataSize; // DWORD
	final int SegCount; // DWORD
	final int Flags; // DWORD
	final int Reserved; // DWORD

	public CDFILEHEADER(final byte[] data) {
		Header = new LSIG(Arrays.copyOfRange(data, 0, 6));
		FileExtLen = CDUtils.wordToShort(Arrays.copyOfRange(data, 6, 8));
		FileDataSize = CDUtils.dwordToInt(Arrays.copyOfRange(data, 8, 12));
		SegCount = CDUtils.dwordToInt(Arrays.copyOfRange(data, 12, 16));
		Flags = CDUtils.dwordToInt(Arrays.copyOfRange(data, 16, 20));
		Reserved = CDUtils.dwordToInt(Arrays.copyOfRange(data, 20, 24));

		// System.out.println("CDFILEHEADER:");
		// System.out.println("\tHeader.Signature: " + Header.Signature);
		// System.out.println("\tHeader.Length: " + Header.Length);
		// System.out.println("\tFileExtLen: " + FileExtLen);
		// System.out.println("\tFileDataSize: " + FileDataSize);
		// System.out.println("\tSegCount: " + SegCount);
		// System.out.println("\tFlags: " + Flags);
		// System.out.println("\tReserved: " + Reserved);
	}

	public byte[] getBytes() {
		byte[] result = new byte[LENGTH];
		int offset = 0;

		for (byte aByte : Header.getBytes()) {
			result[offset++] = aByte;
		}
		for (byte aByte : CDUtils.toWord(FileExtLen)) {
			result[offset++] = aByte;
		}
		for (byte aByte : CDUtils.toWord(FileDataSize)) {
			result[offset++] = aByte;
		}
		for (byte aByte : CDUtils.toWord(SegCount)) {
			result[offset++] = aByte;
		}
		for (byte aByte : CDUtils.toWord(Flags)) {
			result[offset++] = aByte;
		}
		for (byte aByte : CDUtils.toWord(Reserved)) {
			result[offset++] = aByte;
		}

		return result;
	}
}