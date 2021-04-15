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
 * This structure specifies the end of a hot region in a rich text field. There are special cases for Release 4.x and Release 5.x hotspot
 * records which have either Lotus Script or Release 4.x (or 5.x) actions associated with them. These hotspots contain a CDHOTSPOTBEGIN
 * record with the signature SIG_CD_V4HOTSPOTBEGIN (or SIG_CD_V5HOTSPOTBEGIN), and a CDHOTSPOTEND record with the signature
 * SIG_CD_V4HOTSPOTEND (or SIG_CD_V5HOTSPOTEND). (editods.h)
 *
 */
public class CDHOTSPOTEND extends CDRecord {

	public final BSIG Header = inner(new BSIG());

	@Override
	public SIG getHeader() {
		return Header;
	}

}
