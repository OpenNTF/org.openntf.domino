/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
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
 * This structure is used to define a JPEG or GIF Image that is part of a Domino document. The CDIMAGEHEADER structure follows a CDGRAPHIC
 * structure. CDIMAGESEGMENT structure(s) then follow the CDIMAGEHEADER. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDIMAGEHEADER extends CDRecord {

	public final LSIG Header = inner(new LSIG());
	// TODO make enum
	public final Unsigned16 ImageType = new Unsigned16();
	public final Unsigned16 Width = new Unsigned16();
	public final Unsigned16 Height = new Unsigned16();
	public final Unsigned32 ImageDataSize = new Unsigned32();
	public final Unsigned32 SegCount = new Unsigned32();
	public final Unsigned32 Flags = new Unsigned32();
	public final Unsigned32 Reserved = new Unsigned32();

	@Override
	public SIG getHeader() {
		return Header;
	}
}
