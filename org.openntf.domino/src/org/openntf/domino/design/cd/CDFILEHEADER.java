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