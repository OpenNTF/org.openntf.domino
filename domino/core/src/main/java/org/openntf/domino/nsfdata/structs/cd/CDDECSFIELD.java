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

import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This CD Record gives information pertaining to data connection resource information in a field or form. (editods.h)
 * 
 * @since Lotus Notes/Domino 6.0
 */
@SuppressWarnings("nls")
public class CDDECSFIELD extends CDRecord {
	public static enum Flag {
		KEY_FIELD((short) 0x001), STORE_LOCALLY((short) 0x002);

		private final short value_;

		private Flag(final short value) {
			value_ = value;
		}

		public short getValue() {
			return value_;
		}

		public static Set<Flag> valuesOf(final short flags) {
			Set<Flag> result = EnumSet.noneOf(Flag.class);
			for (Flag flag : values()) {
				if ((flag.getValue() & flags) > 0) {
					result.add(flag);
				}
			}
			return result;
		}
	}

	public final WSIG Header = inner(new WSIG());
	/**
	 * Use getFlags for access.
	 */
	@Deprecated
	public final Unsigned16 Flags = new Unsigned16();
	public final Unsigned16 ExternalNameLength = new Unsigned16();
	public final Unsigned16 MetadataNameLength = new Unsigned16();
	public final Unsigned16 DCRNameLength = new Unsigned16();
	public final Unsigned16[] Spare = array(new Unsigned16[8]);

	static {
		addVariableString("ExternalName", "ExternalNameLength");
		addVariableString("MetadataName", "MetadataNameLength");
		addVariableString("DCRName", "DCRNameLength");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf((short) Flags.get());
	}

	public String getExternalName() {
		return (String) getVariableElement("ExternalName");
	}

	public String getMetadataName() {
		return (String) getVariableElement("MetadataName");
	}

	public String getDCRName() {
		return (String) getVariableElement("DCRName");
	}
}
