package org.openntf.domino.nsfdata.structs;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import javolution.io.Struct.Unsigned8;

public enum ODSUtils {
	;

	public static String fromLMBCS(final byte[] data) {
		return fromLMBCS(ByteBuffer.wrap(data));
	}

	public static String fromLMBCS(final Unsigned8[] data) {
		byte[] result = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			result[i] = (byte) data[i].get();
		}
		return fromLMBCS(result);
	}

	public static String fromLMBCS(final ByteBuffer data) {
		Charset lmbcs = Charset.forName("x-lmbcs-1");
		CharBuffer buffer = lmbcs.decode(data.duplicate());
		return buffer.toString();
	}

	public static ByteBuffer toLMBCS(final String value) {
		Charset lmbcs = Charset.forName("x-lmbcs-1");
		return lmbcs.encode(value);
	}

	public static String fromAscii(final Unsigned8[] data) {
		byte[] result = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			result[i] = (byte) data[i].get();
		}
		return new String(result);
	}
}
