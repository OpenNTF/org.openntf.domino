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
import org.openntf.domino.nsfdata.structs.RECTSIZE;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * Identifies a Windows Graphics Device Interface (GDI) metafile embedded in a rich text field. This record must be preceded by a CDGRAPHIC
 * record. Since Windows GDI metafiles can be large, but Domino and Notes have an internal limit of 65,536 bytes (64kB) for a segment, a
 * metafile may be divided into segments of up to 64kB; each segment must be preceded by a CDWINMETASEG record. (editods.h)
 *
 */
public class CDWINMETAHEADER extends CDRecord {

	public final LSIG Header = inner(new LSIG());
	public final Signed16 mm = new Signed16();
	public final Signed16 xExt = new Signed16();
	public final Signed16 yExt = new Signed16();
	public final RECTSIZE OriginalDisplaySize = inner(new RECTSIZE());
	public final Unsigned32 MetafileSize = new Unsigned32();
	public final Unsigned16 SegCount = new Unsigned16();

	@Override
	public SIG getHeader() {
		return Header;
	}

}
