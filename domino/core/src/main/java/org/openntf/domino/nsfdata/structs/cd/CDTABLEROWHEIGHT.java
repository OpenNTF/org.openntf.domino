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

import org.openntf.domino.nsfdata.structs.BSIG;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD record describes the Row Height property for a table. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDTABLEROWHEIGHT extends CDRecord {

	public final BSIG Header = inner(new BSIG());
	public final Unsigned16 RowHeight = new Unsigned16();

	@Override
	public SIG getHeader() {
		return Header;
	}
}
