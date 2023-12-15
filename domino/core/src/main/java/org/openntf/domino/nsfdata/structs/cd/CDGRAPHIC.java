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

import org.openntf.domino.nsfdata.structs.CROPRECT;
import org.openntf.domino.nsfdata.structs.LSIG;
import org.openntf.domino.nsfdata.structs.RECTSIZE;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * The CDGRAPHIC record contains information used to control display of graphic objects in a document. This record marks the beginning of a
 * composite graphic object, and must be present for any graphic object to be loaded or displayed. (editods.h)
 *
 */
public class CDGRAPHIC extends CDRecord {

	public final LSIG Header = inner(new LSIG());
	public final RECTSIZE DestSize = inner(new RECTSIZE());
	public final RECTSIZE CropSize = inner(new RECTSIZE());
	public final CROPRECT CropOffset = inner(new CROPRECT());
	public final Unsigned16 fResize = new Unsigned16();
	// TODO make enum
	public final Unsigned8 Version = new Unsigned8();
	public final Unsigned8 bFlags = new Unsigned8();
	public final Unsigned16 wReserved = new Unsigned16();

	@Override
	public SIG getHeader() {
		return Header;
	}

}
