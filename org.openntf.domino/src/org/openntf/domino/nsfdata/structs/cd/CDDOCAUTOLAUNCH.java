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
	public static enum ObjectType {
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

		private ObjectType(final int value) {
			value_ = value;
		}

		public int getValue() {
			return value_;
		}

		public static ObjectType valueOf(final int typeCode) {
			for (ObjectType type : values()) {
				if (type.getValue() == typeCode) {
					return type;
				}
			}
			throw new IllegalArgumentException("No matching ObjectType found for type code " + typeCode);
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

	static {
		addFixed("ObjectType", Integer.class);
		addFixed("HideWhenFlags", Integer.class);
		addFixed("LaunchWhenFlags", Integer.class);
		addFixed("OleFlags", Integer.class);
		addFixed("CopyToFieldFlags", Integer.class);
		addFixed("Spare1", Integer.class);
		addFixed("Spare2", Integer.class);
		addFixed("OleObjClass", OLE_GUID.class);
		addFixedUnsigned("FieldNameLength", Short.class);

		addVariableString("FieldName", "getFieldNameLength");
	}

	public CDDOCAUTOLAUNCH(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return Type of object to launch
	 */
	public ObjectType getObjectType() {
		return ObjectType.valueOf((Integer) getStructElement("ObjectType"));
	}

	public Set<HideWhenFlag> getHideWhenFlags() {
		return HideWhenFlag.valuesOf((Integer) getStructElement("HideWhenFlags"));
	}

	public Set<LaunchWhenFlag> getLaunchWhenFlags() {
		return LaunchWhenFlag.valuesOf((Integer) getStructElement("LaunchWhenFlags"));
	}

	public Set<OLEFlag> getOLEFlags() {
		return OLEFlag.valuesOf((Integer) getStructElement("OleFlags"));
	}

	public Set<FieldCopyFlag> getCopyToFieldFlags() {
		return FieldCopyFlag.valuesOf((Integer) getStructElement("CopyToFieldFlags"));
	}

	public int getSpare1() {
		return (Integer) getStructElement("Spare1");
	}

	public int getSpare2() {
		return (Integer) getStructElement("Spare2");
	}

	/**
	 * @return If named field, field name length
	 */
	public int getFieldNameLength() {
		return (Integer) getStructElement("FieldNameLength");
	}

	public OLE_GUID getOLEObjectClass() {
		return (OLE_GUID) getStructElement("OleObjClass");
	}

	/**
	 * @return ClassID GUID of OLE object, if create new
	 */
	public String getFieldName() {
		return (String) getStructElement("FieldName");
	}
}
