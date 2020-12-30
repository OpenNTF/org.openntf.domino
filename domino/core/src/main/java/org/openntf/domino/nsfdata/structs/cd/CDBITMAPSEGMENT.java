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

import org.openntf.domino.nsfdata.structs.LSIG;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * The bitmap data is divided into segments to optimize data storage within Domino. It is recommended that each segment be no larger than
 * 10k bytes. For best display speed, the segments sould be as large as possible, up to the practical 10k limit. A scanline must be
 * contained within a single segment, and cannot be divided between two segments. A bitmap must contain at least one segment, but may have
 * many segments. (editods.h)
 *
 */
@SuppressWarnings("nls")
public class CDBITMAPSEGMENT extends CDRecord {
	public final LSIG Header = inner(new LSIG());
	public final Unsigned32[] Reserved = array(new Unsigned32[2]);
	public final Unsigned16 ScanlineCount = new Unsigned16();
	public final Unsigned16 DataSize = new Unsigned16();

	static {
		addVariableData("BitmapData", "DataSize");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	// TODO uncompress the data (see docs)
	public byte[] getBitmapData() {
		return (byte[]) getVariableElement("BitmapData");
	}
}
