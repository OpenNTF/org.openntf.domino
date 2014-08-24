package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.SIG;

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

	public static enum Type {
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
		 * and we want to reserve an upper range for thos special types.
		 * 
		 * For now, reserve the entire upper range 32,000 and up for them. The IDs are started at MAXWORD and work their way down.
		 */
		RESERVERS((short) 32000);

		private final short value_;

		private Type(final short value) {
			value_ = value;
		}

		public short getValue() {
			return value_;
		}

		public static Type valueOf(final short typeCode) {
			for (Type type : values()) {
				if (type.getValue() == typeCode) {
					return type;
				}
			}
			throw new IllegalArgumentException("No matching Type found for type code " + typeCode);
		}
	}

	public static enum ResourceClass {
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

		private ResourceClass(final short value) {
			value_ = value;
		}

		public short getValue() {
			return value_;
		}

		public static ResourceClass valueOf(final short typeCode) {
			for (ResourceClass type : values()) {
				if (type.getValue() == typeCode) {
					return type;
				}
			}
			return NOTFOUND;
			//			throw new IllegalArgumentException("No matching ResourceClass found for type code " + typeCode);
		}
	}

	static {
		addFixed("Flags", Integer.class);
		addFixed("Type", Short.class);
		addFixed("ResourceClass", Short.class);
		addFixedUnsigned("Length1", Short.class);
		addFixedUnsigned("ServerHintLength", Short.class);
		addFixedUnsigned("FileHintLength", Short.class);
		addFixedArray("Reserved", Byte.class, 8);

		addVariableString("ServerHint", "getServerHintLength");
		addVariableString("FileHint", "getFileHintLength");
		addVariableData("Data1", "getLength1");
	}

	public CDRESOURCE(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf((Integer) getStructElement("Flags"));
	}

	public Type getType() {
		return Type.valueOf((Short) getStructElement("Type"));
	}

	public ResourceClass getResourceClass() {
		return ResourceClass.valueOf((Short) getStructElement("ResourceClass"));
	}

	/**
	 * meaning depends on Type
	 */
	public int getLength1() {
		return (Integer) getStructElement("Length1");
	}

	/**
	 * @return length of the server hint
	 */
	public int getServerHintLength() {
		return (Integer) getStructElement("ServerHintLength");
	}

	/**
	 * @return length of the file hint
	 */
	public int getFileHintLength() {
		return (Integer) getStructElement("FileHintLength");
	}

	public byte[] getReserved() {
		return (byte[]) getStructElement("Reserved");
	}

	public String getServerHint() {
		return (String) getStructElement("ServerHint");
	}

	public String getFileHint() {
		return (String) getStructElement("FileHint");
	}

	public byte[] getData1() {
		return (byte[]) getStructElement("Data1");
	}

	// TODO add remaining data

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ", Flags: " + getFlags() + ", Type: " + getType() + ", ResourceClass: "
				+ getResourceClass() + ", ServerHint: " + getServerHint() + ", FileHint: " + getFileHint() + "]";
	}
}
