/*
 * Copyright 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

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
