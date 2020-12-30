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
package org.openntf.domino.nsfdata.structs.cd;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This structure implements a document link in a rich text field. It contains an index into a Doc Link Reference List. A Doc Link Reference
 * (a NOTELINK structure) contains all the information necessary to open the specified document from any database on any server. (editods.h)
 *
 */
@SuppressWarnings("nls")
public class CDLINK2 extends CDRecord {

	public final WSIG Header = inner(new WSIG());
	public final Unsigned16 LinkID = new Unsigned16();

	// TODO add null-terminated-string support

	@Override
	public SIG getHeader() {
		return Header;
	}

	/**
	 * @return Display comment
	 */
	public String getComment() {
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 2);
		// Now build an array until the first null byte
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte aByte;
		int breaker = 0;
		do {
			aByte = data.get();
			bos.write(aByte);
			if (breaker++ > 1000) {
				System.out.println("we dug too deep!");
				return "";
			}
		} while (aByte != 0);
		return ODSUtils.fromLMBCS(bos.toByteArray());
	}

	/**
	 * @return Server "hint"
	 */
	public String getHint() {
		// Seek to the first null byte (the end of Comment)
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 2);
		byte aByte;
		int breaker = 0;
		do {
			aByte = data.get();
			if (breaker++ > 1000) {
				System.out.println("we dug too deep!");
				return "";
			}
		} while (aByte != 0);

		// Now we're past comment. Time to read in Hint
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		breaker = 0;
		if (data.hasRemaining()) {
			do {
				aByte = data.get();
				bos.write(aByte);
				if (breaker++ > 1000) {
					System.out.println("we dug too deep!");
					return "";
				}
			} while (aByte != 0 && data.hasRemaining());
			return ODSUtils.fromLMBCS(bos.toByteArray());
		} else {
			return "";
		}
	}

	/**
	 * @return Anchor text (optional)
	 */
	public String getAnchor() {
		// Seek to the first null byte (the end of Comment)
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 2);
		byte aByte;
		int breaker = 0;
		do {
			aByte = data.get();
			if (breaker++ > 1000) {
				System.out.println("we dug too deep!");
				return "";
			}
		} while (aByte != 0 && data.hasRemaining());
		if (!data.hasRemaining()) {
			return "";
		}

		// Now seek to the second null byte (end of Hint)
		data.get();
		breaker = 0;
		do {
			aByte = data.get();
			if (breaker++ > 1000) {
				System.out.println("we dug too deep!");
				return "";
			}
		} while (aByte != 0 && data.hasRemaining());

		// Now we're past Hint. Time to read in Anchor
		if (data.hasRemaining()) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			breaker = 0;
			do {
				aByte = data.get();
				bos.write(aByte);
				if (breaker++ > 1000) {
					System.out.println("we dug too deep!");
					return "";
				}
			} while (aByte != 0 && data.hasRemaining());
			return ODSUtils.fromLMBCS(bos.toByteArray());
		} else {
			return "";
		}
	}

	@Override
	public int getExtraLength() {
		return (int) (Header.getRecordLength() % 2);
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ", Link ID: " + LinkID.get() + ", Comment: " + getComment() + ", Hint: " + getHint()
				+ ", Anchor: " + getAnchor() + "]";
	}
}
