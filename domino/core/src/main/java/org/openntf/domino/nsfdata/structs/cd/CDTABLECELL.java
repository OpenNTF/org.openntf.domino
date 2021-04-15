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
 * This structure specifies the cell of a table. Use this structure when accessing a table in a rich text field. (editods.h)
 *
 */
public class CDTABLECELL extends CDRecord {

	public final BSIG Header = inner(new BSIG());
	public final Unsigned8 Row = new Unsigned8();
	public final Unsigned8 Column = new Unsigned8();
	public final Unsigned16 LeftMargin = new Unsigned16();
	public final Unsigned16 RightMargin = new Unsigned16();
	public final Unsigned16 FractionalWidth = new Unsigned16();
	// TODO make enum
	public final Unsigned8 Border = new Unsigned8();
	// TODO make enum
	public final Unsigned8 Flags = new Unsigned8();
	// TODO make enum
	public final Unsigned16 v42Border = new Unsigned16();
	public final Unsigned8 RowSpan = new Unsigned8();
	public final Unsigned8 ColumnSpan = new Unsigned8();
	public final Unsigned16 BackgroundColor = new Unsigned16();

	@Override
	public SIG getHeader() {
		return Header;
	}

}
