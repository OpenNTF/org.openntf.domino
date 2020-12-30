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

import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This CD Record further defines information for a table. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
@SuppressWarnings("nls")
public class CDTABLELABEL extends CDRecord {
	public static enum Flag {
		ROWLABEL((short) 0x0001), TABLABEL((short) 0x002);

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
	 * Use getLabel for access.
	 */
	@Deprecated
	public final Unsigned8[] Label = array(new Unsigned8[128]);
	public final Unsigned16[] Reserved = array(new Unsigned16[3]);
	/**
	 * Use getFlags for access.
	 */
	@Deprecated
	public final Unsigned16 Flags = new Unsigned16();

	@Override
	public SIG getHeader() {
		return Header;
	}

	public String getLabel() {
		return ODSUtils.fromLMBCS(Label);
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf((short) Flags.get());
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Label=" + getLabel() + ", Flags=" + getFlags() + "]";
	}
}
