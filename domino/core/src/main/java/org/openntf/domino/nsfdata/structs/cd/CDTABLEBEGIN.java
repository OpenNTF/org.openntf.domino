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

import org.openntf.domino.nsfdata.structs.BSIG;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure specifies the beginning of a table. It contains information about the format and size of the table. Use this structure
 * when accessing a table in a rich text field. As of R5, this structure is preceded by a CDPRETABLEBEGIN structure. The CDPRETABLEBEGIN
 * structure specifies additional table properties. (editods.h)
 *
 */
public class CDTABLEBEGIN extends CDRecord {

	public final BSIG Header = inner(new BSIG());
	public final Unsigned16 LeftMargin = new Unsigned16();
	public final Unsigned16 HorizInterCellSpace = new Unsigned16();
	public final Unsigned16 VertInterCellSpace = new Unsigned16();
	public final Unsigned16 V4HorizInterCellSpace = new Unsigned16();
	public final Unsigned16 V4VertInterCellSpace = new Unsigned16();
	// TODO make enum
	public final Unsigned16 Flags = new Unsigned16();

	@Override
	public SIG getHeader() {
		return Header;
	}

}
