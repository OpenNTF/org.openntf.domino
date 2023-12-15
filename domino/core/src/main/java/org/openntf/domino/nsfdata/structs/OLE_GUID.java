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
package org.openntf.domino.nsfdata.structs;

import java.util.UUID;

/**
 * @since Lotus Notes 4.1
 *
 */
public class OLE_GUID extends AbstractStruct {

	public final Unsigned32 Data1 = new Unsigned32();
	public final Unsigned16 Data2 = new Unsigned16();
	public final Unsigned16 Data3 = new Unsigned16();
	public final Unsigned8[] Data4 = array(new Unsigned8[8]);

	public UUID getUUID() {
		return UUID.nameUUIDFromBytes(getBytes());
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Data1=" + Data1.get() + ", Data2=" + Data2.get() + ", Data3=" + Data3.get() //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				+ ", Data4=" + Data4 + "]"; //$NON-NLS-1$ //$NON-NLS-2$
	}
}
