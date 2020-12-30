/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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
package org.openntf.domino.nsfdata.structs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import javolution.io.Struct.Unsigned8;

import org.openntf.domino.Stream;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

@SuppressWarnings("nls")
public enum ODSUtils {
	;
	private static String X_LMBCS_1 = "x-lmbcs-1"; //$NON-NLS-1$

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
				Path temp = Files.createTempFile("lmbcs", ".tmp"); //$NON-NLS-1$ //$NON-NLS-2$
				try {
					Stream stream = Factory.getSession(SessionType.CURRENT).createStream();
					stream.open(temp.toString(), "LMBCS"); //$NON-NLS-1$
					stream.write(data.array());
					stream.setPosition(0);
					String ret = stream.readText();
					stream.close();
					return ret;
				} finally {
					Files.delete(temp);
				}
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
				Path temp = Files.createTempFile("lmbcs", "tmp"); //$NON-NLS-1$ //$NON-NLS-2$
				try {
					Stream stream = Factory.getSession(SessionType.CURRENT).createStream();
					stream.open(temp.toString(), "LMBCS"); //$NON-NLS-1$
					stream.writeText(value);
					stream.setPosition(0);
					ByteBuffer ret = ByteBuffer.wrap(stream.read());
					stream.close();
					return ret;
				} finally {
					Files.delete(temp);
				}
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
			return new String(bos.toByteArray(), "US-ASCII"); //$NON-NLS-1$
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
