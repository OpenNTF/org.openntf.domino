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

import org.openntf.domino.nsfdata.structs.LSIG;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * A portion of a Windows GDI metafile. This record must be preceded by a CDWINMETAHEADER record. Since Windows GDI metafiles can be large,
 * but Domino and Notes have an internal limit of 65,536 bytes (64kB) for a segment, a metafile may be divided into segments of up to 64kB;
 * each segment must be preceded by a CDWINMETASEG record. (editods.h)
 *
 */
@SuppressWarnings("nls")
public class CDWINMETASEG extends CDRecord {

	public final LSIG Header = inner(new LSIG());
	public final Unsigned16 DataSize = new Unsigned16();
	public final Unsigned16 SegSize = new Unsigned16();

	static {
		addVariableData("Data", "DataSize");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	/**
	 * @return Windows Metafile Bits for this segment. Each segment must be <= 64K bytes.
	 */
	public byte[] getSegmentData() {
		return (byte[]) getVariableElement("Data");
	}
}
