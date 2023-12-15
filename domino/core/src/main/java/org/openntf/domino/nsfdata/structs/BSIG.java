/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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


/**
 * Every CD record begins with a header. There are three types of headers, BSIG, WSIG, and LSIG. The first byte of the header is a signature
 * which identifies the type of the header and the type of the CD record that follows. (ods.h)
 *
 */
public class BSIG extends SIG {
	public final Unsigned8 Signature = new Unsigned8();
	public final Unsigned8 Length = new Unsigned8();

	@Override
	public long getRecordLength() {
		return Length.get();
	}

	@Override
	public void setRecordLength(final long length) {
		Length.set((short) length);
	}

	@Override
	public int getSigIdentifier() {
		return Signature.get() & 0xFF;
	}

	@Override
	public void setSigIdentifier(final int identifier) {
		Signature.set((short) identifier);
	}
}
