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

import org.openntf.domino.nsfdata.structs.BSIG;
import org.openntf.domino.nsfdata.structs.LENGTH_VALUE;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD record contains size information for a layer box. The units (pixels, twips, etc.) for the Width and Height are set in the "Units"
 * members of the "Top", "Left", "Bottom" and "Right" members of the CDPOSITIONING structure. (editods.h)
 * 
 * @since Lotus Notes/Domino 6.0
 *
 */
public class CDBOXSIZE extends CDRecord {

	public final BSIG Header = inner(new BSIG());
	public final LENGTH_VALUE Width = inner(new LENGTH_VALUE());
	public final LENGTH_VALUE Height = inner(new LENGTH_VALUE());
	public final LENGTH_VALUE[] Reserved = array(new LENGTH_VALUE[4]);
	public final Unsigned32[] dwReserved = array(new Unsigned32[4]);

	@Override
	public SIG getHeader() {
		return Header;
	}

}
