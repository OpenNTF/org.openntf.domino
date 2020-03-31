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

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This CD record defines a resource within a database. There may be many resources defined within a particular database. A resource can be
 * an image, an applet, a shared field or a script library. (rsrcods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDRESOURCE extends CDRecord {
	public static enum Flag {
		/**
		 * The type's data is a formula valid for CDRESOURCE_TYPE_URL and CDRESOURCE_TYPE_NAMEDELEMENT.
		 */
		FORMULA(0x00000001),
		/**
		 * The notelink variable length data contains the notelink itself - not an index into a $Links items
		 */
		NOTELINKINLINE(0x00000002),
		/**
		 * If specified, the link is to an absolute database or thing. Used to make a hard link to a specific DB
		 */
		ABSOLUTE(0x00000004),
		/**
		 * If specified, the server and file hint are filled in and should be attempted before trying other copies
		 */
		USEHINTFIRST(0x00000008),
		/**
		 * The type's data is a canned image file (data/domino/icons/[*].gif) valid for CDRESOURCE_TYPE_URL && CDRESOURCE_CLASS_IMAGE only
		 */
		CANNEDIMAGE(0x00000010),
		/**
		 * The object is private in its database.
		 * 
		 * NOTE: CDRESOURCE_FLAGS_PRIVATE_DATABASE and CDRESOURCE_FLAGS_PRIVATE_DESKTOP are mutually exclusive
		 */
		PRIVATE_DATABASE(0x00000020),
		/**
		 * The object is private in the desktop database.
		 * 
		 * NOTE: CDRESOURCE_FLAGS_PRIVATE_DATABASE and CDRESOURCE_FLAGS_PRIVATE_DESKTOP are mutually exclusive
		 */
		PRIVATE_DESKTOP(0x00000040),
		/**
		 * The replica in the CD resource needs to be obtained via RLGetReplicaID to handle special replica IDs like 'current' mail file
		 */
		REPLICA_WILDCARD(0x00000080),
		/**
		 * Used with class view and folder to mean "Simple View"
		 */
		SIMPLE(0x00000100),
		/**
		 * Open this up in design mode
		 */
		DESIGN_MODE(0x00000200),
		/**
		 * open this up in preivew mode, if supported. Not saved to disk
		 */
		PREVIEW(0x00000400),
		/**
		 * we will be doing a search after link opened. Not saved to disk
		 */
		SEARCH(0x00000800),
		/**
		 * An UNID is added to the end of the hResource that means something to that type - currently used in named element type
		 */
		UNIDADDED(0x00001000),
		/**
		 * document should be in edit mode
		 */
		EDIT_MODE(0x00002000),
		/**
		 * Reserved meaning for each resource link class
		 */
		RESERVED1(0x10000000),
		/**
		 * Reserved meaning for each resource link class
		 */
		RESERVED2(0x20000000),
		/**
		 * Reserved meaning for each resource link class
		 */
		RESERVED3(0x40000000),
		/**
		 * Reserved meaning for each resource link class
		 */
		RESERVED4(0x80000000);

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

	public static enum ResourceType {
		EMPTY((short) 0), URL((short) 1), NOTELINK((short) 2), NAMEDELEMENT((short) 3),
		/**
		 * Currently not written to disk only used in RESOURCELINK
		 */
		NOTEIDLINK((short) 4),
		/**
		 * This would be used in conjunction with the formula flag. The formula is an @Command that would perform some action, typically it
		 * would also switch to a Notes UI element. This will be used to reference the replicator page and other UI elements.
		 */
		ACTION((short) 5),
		/**
		 * Currently not written to disk only used in RESOURCELINK
		 */
		NAMEDITEMELEMENT((short) 6),
		/**
		 * Sitemaps/Outlines use the same type identifiers as resource links. However, there are some types that are special to an outline,
		 * and we want to reserve an upper range for those special types.
		 * 
		 * For now, reserve the entire upper range 32,000 and up for them. The IDs are started at MAXWORD and work their way down.
		 */
		RESERVERS((short) 32000);

		private final short value_;

		private ResourceType(final short value) {
			value_ = value;
		}

		public short getValue() {
			return value_;
		}

		public static ResourceType valueOf(final short typeCode) {
			for (ResourceType type : values()) {
				if (type.getValue() == typeCode) {
					return type;
				}
			}
			throw new IllegalArgumentException("No matching ResourceType found for type code " + typeCode);
		}
	}

	public static enum ResourceClassType {
		UNKNOWN((short) 0), DOCUMENT((short) 1), VIEW((short) 2), FORM((short) 3), NAVIGATOR((short) 4), DATABASE((short) 5),
		FRAMESET((short) 6), PAGE((short) 7), IMAGE((short) 8), ICON((short) 9), HELPABOUT((short) 10), HELPUSING((short) 11),
		SERVER((short) 12), APPLET((short) 13),
		/**
		 * A compile formula someplace
		 */
		FORMULA((short) 14), AGENT((short) 15),
		/**
		 * A file on disk (file:)
		 */
		FILE((short) 16),
		/**
		 * A file attached to a note
		 */
		FILEATTACHMENT((short) 17), OLEEMBEDDING((short) 18), SHAREDIMAGE((short) 19), FOLDER((short) 20),
		/**
		 * An old (4.6) or new style portfolio. Which gets incorporated into the bookmark bar as a tab, rather than getting opened as a
		 * database
		 */
		PORTFOLIO((short) 21), OUTLINE((short) 22),

		NOTFOUND((short) -1);

		private final short value_;

		private ResourceClassType(final short value) {
			value_ = value;
		}

		public short getValue() {
			return value_;
		}

		public static ResourceClassType valueOf(final short typeCode) {
			for (ResourceClassType type : values()) {
				if (type.getValue() == typeCode) {
					return type;
				}
			}
			return NOTFOUND;
			//			throw new IllegalArgumentException("No matching ResourceClass found for type code " + typeCode);
		}
	}

	public final WSIG Header = inner(new WSIG());
	/**
	 * Use getFlags for access.
	 */
	@Deprecated
	public final Unsigned32 Flags = new Unsigned32();
	/**
	 * Use getType for access.
	 */
	@Deprecated
	public final Unsigned16 Type = new Unsigned16();
	/**
	 * Use getResourceClass for access.
	 */
	@Deprecated
	public final Unsigned16 ResourceClass = new Unsigned16();
	public final Unsigned16 Length1 = new Unsigned16();
	public final Unsigned16 ServerHintLength = new Unsigned16();
	public final Unsigned16 FileHintLength = new Unsigned16();
	public final Unsigned8[] Reserved = array(new Unsigned8[8]);

	static {
		addVariableString("ServerHint", "ServerHintLength");
		addVariableString("FileHint", "FileHintLength");
		addVariableData("Data1", "Length1");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf((int) Flags.get());
	}

	public ResourceType getType() {
		return ResourceType.valueOf((short) Type.get());
	}

	public ResourceClassType getResourceClass() {
		return ResourceClassType.valueOf((short) ResourceClass.get());
	}

	// TODO add remaining data

	//	@Override
	//	public String toString() {
	//		return "[" + getClass().getSimpleName() + ", Flags: " + getFlags() + ", Type: " + getType() + ", ResourceClass: "
	//				+ getResourceClass() + ", ServerHint: " + getServerHint() + ", FileHint: " + getFileHint() + "]";
	//	}
}
