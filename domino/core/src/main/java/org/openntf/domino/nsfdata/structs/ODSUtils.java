package org.openntf.domino.nsfdata.structs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import javolution.io.Struct.Unsigned8;

import org.openntf.domino.Stream;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public enum ODSUtils {
	;
	private static String X_LMBCS_1 = "x-lmbcs-1";

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
		if (Charset.isSupported(X_LMBCS_1)) {
			Charset lmbcs = Charset.forName(X_LMBCS_1);
			CharBuffer buffer = lmbcs.decode(data.duplicate());
			return buffer.toString();
		} else {
			System.err.println(X_LMBCS_1 + " not supported by JVM - using a Stream ");
			try {
				File temp = File.createTempFile("lmbcs", "tmp");
				Stream stream = Factory.getSession(SessionType.CURRENT).createStream();
				stream.open(temp.getAbsolutePath(), "LMBCS");
				stream.write(data.array());
				stream.setPosition(0);
				String ret = stream.readText();
				stream.close();
				temp.delete();
				return ret;
			} catch (IOException ioe) {
				ioe.printStackTrace();
				return null;
			}
		}
	}

	public static ByteBuffer toLMBCS(final String value) {
		if (Charset.isSupported(X_LMBCS_1)) {
			Charset lmbcs = Charset.forName(X_LMBCS_1);
			return lmbcs.encode(value);
		} else {
			System.err.println(X_LMBCS_1 + " not supported by JVM - using a Stream ");
			try {
				File temp = File.createTempFile("lmbcs", "tmp");
				Stream stream = Factory.getSession(SessionType.CURRENT).createStream();
				stream.open(temp.getAbsolutePath(), "LMBCS");
				stream.writeText(value);
				stream.setPosition(0);
				ByteBuffer ret = ByteBuffer.wrap(stream.read());
				stream.close();
				temp.delete();
				return ret;
			} catch (IOException ioe) {
				ioe.printStackTrace();
				return null;
			}
		}
	}

	public static String fromAscii(final Unsigned8[] data) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		for (int i = 0; i < data.length; i++) {
			byte aByte = (byte) data[i].get();
			if (aByte == 0) {
				break;
			} else {
				bos.write(aByte);
			}
		}
		try {
			return new String(bos.toByteArray(), "US-ASCII");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
