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