package org.openntf.domino.design.cd;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public enum CDUtils {
	;

	public static int dwordToInt(final byte[] dword) {
		ByteBuffer bb = ByteBuffer.wrap(dword);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		return bb.getInt();
	}

	public static short wordToShort(final byte[] word) {
		ByteBuffer bb = ByteBuffer.wrap(word);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		return bb.getShort();
	}

	public static byte[] toWord(final int value) {
		byte[] result = new byte[4];
		int written = 0;
		for (int i = 0; i < 4; i++) {
			Double place = ((value - written) % Math.pow(256, i + 1)) / Math.pow(256, i);
			result[i] = (byte) place.intValue();
			written += (result[i] & 0xFF) * Math.pow(256, i);
		}
		return result;
	}

	public static byte[] toWord(final short value) {
		byte[] result = new byte[2];
		int written = 0;
		for (int i = 0; i < 2; i++) {
			Double place = ((value - written) % Math.pow(256, i + 1)) / Math.pow(256, i);
			result[i] = (byte) place.intValue();
			written += (result[i] & 0xFF) * Math.pow(256, i);
		}
		return result;
	}
}
