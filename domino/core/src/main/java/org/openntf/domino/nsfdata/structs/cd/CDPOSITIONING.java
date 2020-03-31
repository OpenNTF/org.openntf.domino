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
import org.openntf.domino.nsfdata.structs.LENGTH_VALUE;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD record contains position information for a layer box. (editods.h)
 * 
 * @since Lotus Notes/Domino 6.0
 *
 */
public class CDPOSITIONING extends CDRecord {

	public static enum SchemeType {
		STATIC, ABSOLUTE, RELATIVE, FIXED
	}

	public final BSIG Header = inner(new BSIG());
	public final Enum8<SchemeType> Scheme = new Enum8<SchemeType>(SchemeType.values());
	public final Unsigned8 bReserved = new Unsigned8();
	public final Signed32 ZIndex = new Signed32();
	public final LENGTH_VALUE Top = new LENGTH_VALUE();
	public final LENGTH_VALUE Left = new LENGTH_VALUE();
	public final LENGTH_VALUE Bottom = new LENGTH_VALUE();
	public final LENGTH_VALUE Right = new LENGTH_VALUE();
	public final Float64 BrowserLeftOffset = new Float64();
	public final Float64 BrowserRightOffset = new Float64();

	@Override
	public SIG getHeader() {
		return Header;
	}
}
