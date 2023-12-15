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

import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * A CD record of this type specifies the start of a DDE link. (editods.h)
 * 
 * @since forever
 *
 */
@SuppressWarnings("nls")
public class CDDDEBEGIN extends CDRecord {
	public static enum Flag {
		AUTOLINK(0x01), MANUALLINK(0x02), EMBEDDED(0x04), INITIATE(0x08), CDP(0x10), NOTES_LAUNCHED(0x20), CONV_ACTIVE(0x40),
		EMBEDEXTRACTED(0x80), NEWOBJECT(0x100);

		private final int value_;

		private Flag(final int value) {
			value_ = value;
		}

		public int getValue() {
			return value_;
		}

		public static Set<Flag> valuesOf(final int flags) {
			Set<Flag> result = EnumSet.noneOf(Flag.class);
			for (Flag flag : values()) {
				if ((flag.getValue() & flags) > 0) {
					result.add(flag);
				}
			}
			return result;
		}
	}

	public static final int DDESERVERNAMEMAX = 32;
	public static final int DDEITEMNAMEMAX = 64;
	public static final int DDESERVERCOMMANDMAX = 256;

	public final WSIG Header = inner(new WSIG());
	/**
	 * @deprecated Use getServerName() for access
	 */
	@Deprecated
	public final Unsigned8[] ServerName = array(new Unsigned8[DDESERVERNAMEMAX]);
	/**
	 * @deprecated Use getTopicName() for access
	 */
	@Deprecated
	public final Unsigned8[] TopicName = array(new Unsigned8[100]);
	/**
	 * @deprecated Use getItemName() for access
	 */
	@Deprecated
	public final Unsigned8[] ItemName = array(new Unsigned8[DDEITEMNAMEMAX]);
	/**
	 * @deprecated Use getFlags() for access
	 */
	@Deprecated
	public final Unsigned32 Flags = new Unsigned32();
	/**
	 * @deprecated Use getPasteEmbedDocName() for access
	 */
	@Deprecated
	public final Unsigned8[] PasteEmbedDocName = array(new Unsigned8[80]);
	public final Unsigned16 EmbeddedDocCount = new Unsigned16();
	public final Enum16<DDEFormat> ClipFormat = new Enum16<DDEFormat>(DDEFormat.values());

	@Override
	public SIG getHeader() {
		return Header;
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf((int) Flags.get());
	}

	public String getServerName() {
		return ODSUtils.fromAscii(ServerName);
	}

	public String getTopicName() {
		return ODSUtils.fromAscii(TopicName);
	}

	public String getItemName() {
		return ODSUtils.fromAscii(ItemName);
	}

	public String getPasteEmbedDocName() {
		return ODSUtils.fromAscii(PasteEmbedDocName);
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": ServerName=" + getServerName() + ", TopicName=" + getTopicName() + ", ItemName="
				+ getItemName() + ", Flags=" + getFlags() + ", PasteEmbedDocName=" + getPasteEmbedDocName() + ", EmbeddedDocCount="
				+ EmbeddedDocCount.get() + ", ClipFormat=" + ClipFormat.get() + "]";
	}
}
