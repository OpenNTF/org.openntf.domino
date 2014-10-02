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