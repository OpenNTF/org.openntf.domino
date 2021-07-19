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
package org.openntf.domino.nsfdata.structs;


/**
 * This record contains information for storing a length to disk. (editods.h)
 *
 */
public class LENGTH_VALUE extends AbstractStruct {

	public static enum CDLENGTH_UNITS {
		UNUSED0, TWIPS, PIXELS, PERCENT, EMS, EXS, CHARS
	}

	public final Unsigned16 Flags = new Unsigned16();
	public final Float64 Length = new Float64();
	public final Enum8<CDLENGTH_UNITS> Units = new Enum8<CDLENGTH_UNITS>(CDLENGTH_UNITS.values());
	public final Unsigned8 Reserved = new Unsigned8();

	public int getFlags() {
		// TODO make enum
		return Flags.get();
	}
}
