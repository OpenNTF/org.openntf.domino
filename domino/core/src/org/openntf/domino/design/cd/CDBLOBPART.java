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

public class CDBLOBPART {
	public static final int LENGTH = 20;

	final LSIG Header;
	final short OwnerSig; // WORD
	final short Length; // WORD
	final short BlobMax; // WORD
	final byte[] Reserved; // 8

	byte[] data;

	public CDBLOBPART(final byte[] data, final int ofs) {
		Header = new LSIG(Arrays.copyOfRange(data, ofs + 0, ofs + 6));
		OwnerSig = CDUtils.wordToShort(Arrays.copyOfRange(data, ofs + 6, ofs + 8));
		Length = CDUtils.wordToShort(Arrays.copyOfRange(data, ofs + 8, ofs + 10));
		BlobMax = CDUtils.wordToShort(Arrays.copyOfRange(data, ofs + 10, ofs + 12));
		Reserved = Arrays.copyOfRange(data, ofs + 12, ofs + 20);

		this.data = Arrays.copyOfRange(data, ofs + 20, ofs + 20 + this.Length);
	}

	public byte[] getBytes() {
		byte[] result = new byte[LENGTH + data.length];
		int offset = 0;

		for (byte aByte : Header.getBytes()) {
			result[offset++] = aByte;
		}

		for (byte aByte : CDUtils.toWord(OwnerSig)) {
			result[offset++] = aByte;
		}
		for (byte aByte : CDUtils.toWord(Length)) {
			result[offset++] = aByte;
		}
		for (byte aByte : CDUtils.toWord(BlobMax)) {
			result[offset++] = aByte;
		}
		for (byte aByte : Reserved) {
			result[offset++] = aByte;
		}

		for (byte aByte : data) {
			result[offset++] = aByte;
		}

		return result;
	}
}