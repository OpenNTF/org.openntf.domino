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

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * Contains additional event information for Notes/Domino 6. (editods.h)
 * 
 * @since Lotus Notes/Domino 6.0
 *
 */
public class CDEVENTENTRY extends CDRecord {
	public static enum Platform {
		UNUSED0, CLIENT_ODS, WEB_ODS
	}

	public final WSIG Header = inner(new WSIG());
	public final Enum16<Platform> wPlatform = new Enum16<Platform>(Platform.values());
	// TODO expand HTMLEvent from CDEVENT to work with getter for this value
	public final Unsigned16 wEventId = new Unsigned16();
	// TODO add enum with getter for this (ACTION_* skips values and Enum16 wouldn't suffice)
	public final Unsigned16 wActionType = new Unsigned16();
	public final Unsigned16 wReserved = new Unsigned16();
	public final Unsigned32 dwReserved = new Unsigned32();

	@Override
	public SIG getHeader() {
		return Header;
	}
}
