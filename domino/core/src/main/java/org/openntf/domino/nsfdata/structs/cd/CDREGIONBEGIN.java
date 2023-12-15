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

import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This CD Record is used within mail templates. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDREGIONBEGIN extends CDRecord {

	public static final int MAXREGIONNAME = 35;

	public final WSIG Header = inner(new WSIG());
	public final Unsigned16 Version = new Unsigned16();
	public final Unsigned16 Flags = new Unsigned16();
	public final Unsigned16 RegionNum = new Unsigned16();
	/**
	 * Use getRegionName for access.
	 */
	@Deprecated
	public final Unsigned8[] RegionName = array(new Unsigned8[MAXREGIONNAME + 1]);

	@Override
	public SIG getHeader() {
		return Header;
	}

	public String getRegionName() {
		return ODSUtils.fromLMBCS(RegionName);
	}
}
