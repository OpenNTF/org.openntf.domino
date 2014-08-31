package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.OLE_GUID;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This structure specifies a connection to an OLE object. (oleods.h)
 * 
 * @since Lotus Notes/Domino 4.5
 *
 */
public class CDOLEOBJ_INFO extends CDRecord {

	/**
	 * These symbols define possible values for the StorageFormat member of the CDOLEOBJ_INFO structure. (oleods.h)
	 * 
	 * @since Lotus Notes/Domino 4.5
	 *
	 */
	public static enum StorageFormat {
		STRUCT_STORAGE((short) 1), ISTORAGE_ISTREAM((short) 2), STRUCT_STREAM((short) 3);

		private final short value_;

		private StorageFormat(final short value) {
			value_ = value;
		}

		public short getValue() {
			return value_;
		}

		public static StorageFormat valueOf(final short typeCode) {
			for (StorageFormat type : values()) {
				if (type.getValue() == typeCode) {
					return type;
				}
			}
			throw new IllegalArgumentException("No matching StorageFormat found for type code " + typeCode);
		}
	}

	/**
	 * These flags are stored in the Flags field of the CDOLEOBJ_INFO record. (oleods.h)
	 * 
	 * @since Lotus Notes/Domino 4.5
	 *
	 */
	public static enum Flag {
		/**
		 * Object is scripted
		 */
		SCRIPTED(0x00000001),
		/**
		 * Object is run in read-only mode
		 */
		RUNREADONLY(0x00000002),
		/**
		 * Object is a control
		 */
		CONTROL(0x00000004),
		/**
		 * Object is sized to fit to window
		 */
		FITTOWINDOW(0x00000008),
		/**
		 * Object is sized to fit below fields
		 */
		FITBELOWFIELDS(0x00000010),
		/**
		 * Object is to be updated from document
		 */
		UPDATEFROMDOCUMENT(0x00000020),
		/**
		 * Object is to be updated from document
		 */
		INCLUDERICHTEXT(0x00000040),
		/**
		 * Object is stored in IStorage/IStream format rather than RootIStorage/ IStorage/IStream
		 */
		ISTORAGE_ISTREAM(0x00000080),
		/**
		 * Object has HTML data
		 */
		HTMLDATA(0x00000100);

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

	static {
		addFixedUnsigned("FileObjNameLength", Short.class);
		addFixedUnsigned("DescriptionNameLength", Short.class);
		addFixedUnsigned("FieldNameLength", Short.class);
		addFixedUnsigned("TextIndexObjNameLength", Short.class);
		addFixed("OleObjClass", OLE_GUID.class);
		addFixed("StorageFormat", Short.class);
		addFixed("DisplayFormat", Short.class);
		addFixed("Flags", Integer.class);
		addFixed("StorageFormatAppearedIn", Short.class);
		addFixedUnsigned("HTMLDataLength", Short.class);
		addFixedUnsigned("AssociatedFILEsLength", Short.class);
		addFixed("Reserved3", Short.class);
		addFixed("Reserved4", Integer.class);

		// TODO verify that these are indeed ASCII
		addVariableAsciiString("FileObjectName", "getFileObjNameLength");
		addVariableAsciiString("DescriptionName", "getDescriptionNameLength");
		addVariableAsciiString("FieldName", "getFieldNameLength");
		addVariableAsciiString("TextIndexObjName", "getTextIndexObjNameLength");
		addVariableData("HTMLData", "getHTMLDataLength");
		addVariableData("AssociatedFILEs", "getAssociatedFILEsLength");
	}

	public static final int SIZE = getFixedStructSize();

	public CDOLEOBJ_INFO(final CDSignature cdSig) {
		super(new WSIG(cdSig, cdSig.getSize() + SIZE), ByteBuffer.wrap(new byte[SIZE]));
	}

	public CDOLEOBJ_INFO(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return Length of the name of extendable $FILE object containing object data
	 */
	public int getFileObjNameLength() {
		return (Integer) getStructElement("FileObjNameLength");
	}

	/**
	 * @return Length of object description
	 */
	public int getDescriptionNameLength() {
		return (Integer) getStructElement("DescriptionNameLength");
	}

	/**
	 * @return Length of field name in which object resides
	 */
	public int getFieldNameLength() {
		return (Integer) getStructElement("FieldNameLength");
	}

	/**
	 * @return Length of the name of the $FILE object containing LMBCS text for object
	 */
	public int getTextIndexObjNameLength() {
		return (Integer) getStructElement("TextIndexObjNameLength");
	}

	/**
	 * @return OLE ClassID GUID of OLE object
	 */
	public OLE_GUID getOleObjClass() {
		return (OLE_GUID) getStructElement("OleObjClass");
	}

	public StorageFormat getStorageFormat() {
		return StorageFormat.valueOf((Short) getStructElement("StorageFormat"));
	}

	public DDEFormat getDisplayFormat() {
		return DDEFormat.valueOf((Short) getStructElement("DisplayFormat"));
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf((Integer) getStructElement("Flags"));
	}

	/**
	 * Note: this returns a String to handle "x.10" versions
	 * 
	 * @return Version # of Notes, for display purposes
	 */
	public String getStorageFormatAppearedIn() {
		int low = getData().get(getData().position() + OLE_GUID.SIZE + 16) & 0xFF;
		int high = getData().get(getData().position() + OLE_GUID.SIZE + 17) & 0xFF;
		return high + "." + low;
	}

	/**
	 * @return Length of HTML data for object
	 */
	public int getHTMLDataLength() {
		return (Integer) getStructElement("HTMLDataLength");
	}

	/**
	 * @return Length of Associated $FILEs data for object
	 */
	public int getAssociatedFILEsLength() {
		return (Integer) getStructElement("AssociatedFILEsLength");
	}

	/**
	 * Unused, must be 0
	 */
	public short getReserved3() {
		return (Short) getStructElement("Reserved3");
	}

	/**
	 * Unused, must be 0
	 */
	public int getReserved4() {
		return (Integer) getStructElement("Reserved4");
	}

	/**
	 * @return Name of extendable $FILE object containing object data
	 */
	public String getFileObjectName() {
		return (String) getStructElement("FileObjectName");
	}

	/**
	 * @return Object description
	 */
	public String getDescriptionName() {
		return (String) getStructElement("DescriptionName");
	}

	/**
	 * @return Field Name in Document in which this object resides
	 */
	public String getFieldName() {
		return (String) getStructElement("FieldName");
	}

	/**
	 * @return Full Text index $FILE object name
	 */
	public String getTextIndexObjName() {
		return (String) getStructElement("TextIndexObjName");
	}

	/**
	 * @return HTML data, as a byte array
	 */
	public byte[] getHTMLData() {
		return (byte[]) getStructElement("HTMLData");
	}

	/**
	 * @return Associated $FILEs Data
	 */
	public byte[] getAssociatedFILEs() {
		return (byte[]) getStructElement("AssociatedFILEs");
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": OleObjClass=" + getOleObjClass() + ", StorageFormat=" + getStorageFormat()
				+ ", DisplayFormat=" + getDisplayFormat() + ", Flags=" + getFlags() + ", StorageFormatAppearedIn="
				+ getStorageFormatAppearedIn() + ", FileObjectName=" + getFileObjectName() + ", DescriptionName=" + getDescriptionName()
				+ ", FieldName=" + getFieldName() + ", TextIndexObjName=" + getTextIndexObjName() + ", HTMLDataLength="
				+ getHTMLDataLength() + ", AssociatedFILEsLength=" + getAssociatedFILEsLength() + "]";
	}
}
