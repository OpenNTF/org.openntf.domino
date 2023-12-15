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
package org.openntf.domino.nsfdata.structs.cd;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.openntf.domino.nsfdata.structs.LSIG;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * A color table is one of the optional records following a CDBITMAPHEADER record. The color table specifies the mapping between 8-bit
 * bitmap samples and 24-bit Red/Green/Blue colors. (editods.h)
 *
 */
public class CDCOLORTABLE extends CDRecord {

	public final LSIG Header = inner(new LSIG());

	@Override
	public SIG getHeader() {
		return Header;
	}

	public Color[] getColors() {
		// This doesn't fit into the usual pattern
		int count = (int) ((Header.getRecordLength() - Header.size()) / 3);
		Color[] result = new Color[count];
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + Header.size());
		data.order(ByteOrder.LITTLE_ENDIAN);
		for (int i = 0; i < count; i++) {
			int r = data.get() & 0xFF;
			int g = data.get() & 0xFF;
			int b = data.get() & 0xFF;
			result[i] = new Color(r, g, b);
		}
		return result;
	}
}
