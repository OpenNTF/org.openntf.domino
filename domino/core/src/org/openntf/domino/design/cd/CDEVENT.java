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

public class CDEVENT {
	public static final int LENGTH = 34;

	final LSIG Header;
	final int Flags; // DWORD
	final short EventType; // WORD
	final short ActionType; // WORD
	final int ActionLength; // DWORD
	final short SignatureLength; // WORD
	final byte[] Reserved; // 14

	public CDEVENT(final byte[] data) {
		Header = new LSIG(Arrays.copyOfRange(data, 0, 6));
		Flags = CDUtils.dwordToInt(Arrays.copyOfRange(data, 6, 10));
		EventType = CDUtils.wordToShort(Arrays.copyOfRange(data, 10, 12));
		ActionType = CDUtils.wordToShort(Arrays.copyOfRange(data, 12, 14));
		ActionLength = CDUtils.dwordToInt(Arrays.copyOfRange(data, 14, 18));
		SignatureLength = CDUtils.wordToShort(Arrays.copyOfRange(data, 18, 20));
		Reserved = Arrays.copyOfRange(data, 20, 34);
		//Reserved = CDUtils.dwordToInt(Arrays.copyOfRange(data, 20, 24));
	}

	public byte[] getBytes() {
		byte[] result = new byte[LENGTH];
		int offset = 0;

		for (byte aByte : Header.getBytes()) {
			result[offset++] = aByte;
		}
		for (byte aByte : CDUtils.toWord(Flags)) {
			result[offset++] = aByte;
		}
		for (byte aByte : CDUtils.toWord(EventType)) {
			result[offset++] = aByte;
		}
		for (byte aByte : CDUtils.toWord(ActionType)) {
			result[offset++] = aByte;
		}
		for (byte aByte : CDUtils.toWord(ActionLength)) {
			result[offset++] = aByte;
		}
		for (byte aByte : CDUtils.toWord(SignatureLength)) {
			result[offset++] = aByte;
		}
		for (byte aByte : Reserved) {
			result[offset++] = aByte;
		}
		return result;
	}
}