package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.OLE_GUID;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * Structure of an on-disk autolaunch item. Most of the information contained in this structure refers to OLE autolaunching behaviors.
 * (oleods.h)
 * 
 * @since Lotus Notes 4.1
 *
 */
public class CDDOCAUTOLAUNCH extends CDRecord {
	/**
	 * Possible values for the ObjectType member of the CDDOCAUTOLAUNCH structure. (oleods.h)
	 * 
	 * @since Lotus Notes 4.1
	 *
	 */
	public static enum ObjType {
		/**
		 * Object type is not specified.
		 */
		NONE(0x00000000),
		/**
		 * OLE Class ID (GUID)
		 */
		OLE_CLASS(0x00000001),
		/**
		 * First OLE object
		 */
		OLEOBJ(0x00000002),
		/**
		 * First doclink
		 */
		DOCLINK(0x00000004),
		/**
		 * First attachment
		 */
		ATTACH(0x00000008),
		/**
		 * AutoLaunch the url in the URL field
		 */
		URL(0x00000010);

		private final int value_;

		private ObjType(final int value) {
			value_ = value;
		}

		public int getValue() {
			return value_;
		}

		public static ObjType valueOf(final int typeCode) {
			for (ObjType type : values()) {
				if (type.getValue() == typeCode) {
					return type;
				}
			}
			throw new IllegalArgumentException("No matching ObjType found for type code " + typeCode);
		}
	}

	/**
	 * Possible values for HideWhenFlags member of CDDOCAUTOLAUNCH structure. (oleods.h)
	 * 
	 * @since Lotus Notes 4.1
	 *
	 */
	public static enum HideWhenFlag {
		OPEN_CREATE(0x00000001), OPEN_EDIT(0x00000002), OPEN_READ(0x00000004), CLOSE_CREATE(0x00000008), CLOSE_EDIT(0x00000010),
		CLOSE_READ(0x00000020);

		private final int value_;

		private HideWhenFlag(final int value) {
			value_ = value;
		}

		public int getValue() {
			return value_;
		}

		public static Set<HideWhenFlag> valuesOf(final int flags) {
			Set<HideWhenFlag> result = EnumSet.noneOf(HideWhenFlag.class);
			for (HideWhenFlag flag : values()) {
				if ((flag.getValue() & flags) > 0) {
					result.add(flag);
				}
			}
			return result;
		}
	}

	/**
	 * These are the possible values for the LaunchWhenFlags member of the CDDOCAUTOLAUNCH structure.
	 * 
	 * @since Lotus Notes 4.1
	 *
	 */
	public static enum LaunchWhenFlag {
		CREATE(0x00000001), EDIT(0x00000002), READ(0x00000004);

		private final int value_;

		private LaunchWhenFlag(final int value) {
			value_ = value;
		}

		public int getValue() {
			return value_;
		}

		public static Set<LaunchWhenFlag> valuesOf(final int flags) {
			Set<LaunchWhenFlag> result = EnumSet.noneOf(LaunchWhenFlag.class);
			for (LaunchWhenFlag flag : values()) {
				if ((flag.getValue() & flags) > 0) {
					result.add(flag);
				}
			}
			return result;
		}
	}

	/**
	 * Possible values for the OleFlags member of the CDDOCAUTOLAUNCH structure.
	 * 
	 * @since Lotus Notes 4.1
	 *
	 */
	public static enum OLEFlag {
		EDIT_INPLACE(0x00000001), MODAL_WINDOW(0x00000002), ADV_OPTIONS(0x00000004);

		private final int value_;

		private OLEFlag(final int value) {
			value_ = value;
		}

		public int getValue() {
			return value_;
		}

		public static Set<OLEFlag> valuesOf(final int flags) {
			Set<OLEFlag> result = EnumSet.noneOf(OLEFlag.class);
			for (OLEFlag flag : values()) {
				if ((flag.getValue() & flags) > 0) {
					result.add(flag);
				}
			}
			return result;
		}
	}

	/**
	 * Possible values for the CopyToFieldFlags member of the CDDOCAUTOLAUNCH structure.
	 * 
	 * @since Lotus Notes 4.1
	 *
	 */
	public static enum FieldCopyFlag {
		/**
		 * Don't copy object to any field (V3 compatabile)
		 */
		NONE(0x00000001),
		/**
		 * Copy object to first rich text field
		 */
		FIRST(0x00000002),
		/**
		 * Copy object to named rich text field
		 */
		NAMED(0x00000004);

		private final int value_;

		private FieldCopyFlag(final int value) {
			value_ = value;
		}

		public int getValue() {
			return value_;
		}

		public static Set<FieldCopyFlag> valuesOf(final int flags) {
			Set<FieldCopyFlag> result = EnumSet.noneOf(FieldCopyFlag.class);
			for (FieldCopyFlag flag : values()) {
				if ((flag.getValue() & flags) > 0) {
					result.add(flag);
				}
			}
			return result;
		}
	}

	/**
	 * Use getObjectType for access.
	 */
	@Deprecated
	public final Unsigned32 ObjectType = new Unsigned32();
	/**
	 * Use getHideWhenFlags for access.
	 */
	@Deprecated
	public final Unsigned32 HideWhenFlags = new Unsigned32();
	/**
	 * Use getLaunchWhenFlags for access.
	 */
	@Deprecated
	public final Unsigned32 LaunchWhenFlags = new Unsigned32();
	/**
	 * Use getOleFlags for access.
	 */
	@Deprecated
	public final Unsigned32 OleFlags = new Unsigned32();
	/**
	 * Use getCopyToFieldFlags for access.
	 */
	@Deprecated
	public final Unsigned32 CopyToFieldFlags = new Unsigned32();
	public final Unsigned32 Spare1 = new Unsigned32();
	public final Unsigned32 Spare2 = new Unsigned32();
	public final Unsigned16 FieldNameLength = new Unsigned16();
	public final OLE_GUID OleObjClass = inner(new OLE_GUID());

	static {
		addVariableString("FieldName", "FieldNameLength");
	}

	public CDDOCAUTOLAUNCH(final CDSignature cdSig) {
		super(cdSig);
	}

	public CDDOCAUTOLAUNCH(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return Type of object to launch
	 */
	public ObjType getObjectType() {
		return ObjType.valueOf((int) ObjectType.get());
	}

	public Set<HideWhenFlag> getHideWhenFlags() {
		return HideWhenFlag.valuesOf((int) HideWhenFlags.get());
	}

	public Set<LaunchWhenFlag> getLaunchWhenFlags() {
		return LaunchWhenFlag.valuesOf((int) LaunchWhenFlags.get());
	}

	public Set<OLEFlag> getOLEFlags() {
		return OLEFlag.valuesOf((int) OleFlags.get());
	}

	public Set<FieldCopyFlag> getCopyToFieldFlags() {
		return FieldCopyFlag.valuesOf((int) CopyToFieldFlags.get());
	}

	/**
	 * @return ClassID GUID of OLE object, if create new
	 */
	public String getFieldName() {
		return (String) getVariableElement("FieldName");
	}
}
