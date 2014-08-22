package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.nsfdata.structs.OLE_GUID;
import org.openntf.domino.nsfdata.structs.SIG;

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
	 * These values specify the DDE clipboard format with which data should be rendered. They are used in the CDDDEBEGIN structure and in
	 * the CDOLEBEGIN structure.
	 *
	 */
	public static enum DDEFormat {
		/**
		 * CF_TEXT
		 */
		TEXT((short) 1),
		/**
		 * CF_METAFILE or CF_METAFILEPICT
		 */
		METAFILE((short) 2),
		/**
		 * CF_BITMAP
		 */
		BITMAP((short) 3),
		/**
		 * Rich Text Format
		 */
		RTF((short) 4),
		/**
		 * OLE Ownerlink (never saved in CD_DDE or CD_OLE: used at run time)
		 */
		OWNERLINK((short) 6),
		/**
		 * OLE Objectlink (never saved in CD_DDE or CD_OLE: used at run time)
		 */
		OBJECTLINK((short) 7),
		/**
		 * OLE Native (never saved in CD_DDE or CD_OLE: used at run time)
		 */
		NATIVE((short) 8),
		/**
		 * Program Icon for embedded object
		 */
		ICON((short) 9);

		private final short value_;

		private DDEFormat(final short value) {
			value_ = value;
		}

		public short getValue() {
			return value_;
		}

		public static DDEFormat valueOf(final short typeCode) {
			for (DDEFormat type : values()) {
				if (type.getValue() == typeCode) {
					return type;
				}
			}
			throw new IllegalArgumentException("No matching DDEFormat found for type code " + typeCode);
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

	public CDOLEOBJ_INFO(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return Length of the name of extendable $FILE object containing object data
	 */
	public int getFileObjNameLength() {
		return getData().getShort(getData().position() + 0) & 0xFFFF;
	}

	/**
	 * @return Length of object description
	 */
	public int getDescriptionNameLength() {
		return getData().getShort(getData().position() + 2) & 0xFFFF;
	}

	/**
	 * @return Length of field name in which object resides
	 */
	public int getFieldNameLength() {
		return getData().getShort(getData().position() + 4) & 0xFFFF;
	}

	/**
	 * @return Length of the name of the $FILE object containing LMBCS text for object
	 */
	public int getTextIndexObjNameLength() {
		return getData().getShort(getData().position() + 6) & 0xFFFF;
	}

	/**
	 * @return OLE ClassID GUID of OLE object
	 */
	public OLE_GUID getOleObjClass() {
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 8);
		data.limit(data.position() + OLE_GUID.SIZE);
		return new OLE_GUID(data);
	}

	public StorageFormat getStorageFormat() {
		return StorageFormat.valueOf(getData().getShort(getData().position() + OLE_GUID.SIZE + 8));
	}

	public DDEFormat getDisplayFormat() {
		return DDEFormat.valueOf(getData().getShort(getData().position() + OLE_GUID.SIZE + 10));
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf(getData().getInt(getData().position() + OLE_GUID.SIZE + 12));
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
		return getData().getShort(getData().position() + OLE_GUID.SIZE + 18) & 0xFFFF;
	}

	/**
	 * @return Length of Associated $FILEs data for object
	 */
	public int getAssociatedFILEsLength() {
		return getData().getShort(getData().position() + OLE_GUID.SIZE + 20) & 0xFFFF;
	}

	/**
	 * Unused, must be 0
	 */
	public short getReserved3() {
		return getData().getShort(getData().position() + OLE_GUID.SIZE + 22);
	}

	/**
	 * Unused, must be 0
	 */
	public int getReserved4() {
		return getData().getInt(getData().position() + OLE_GUID.SIZE + 24);
	}

	/**
	 * @return Name of extendable $FILE object containing object data
	 */
	public String getFileObjectName() {
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + OLE_GUID.SIZE + 28);
		data.limit(data.position() + getFileObjNameLength());
		return ODSUtils.fromLMBCS(data);
	}

	/**
	 * @return Object description
	 */
	public String getDescriptionName() {
		int preceding = getFileObjNameLength();

		ByteBuffer data = getData().duplicate();
		data.position(data.position() + OLE_GUID.SIZE + 28 + preceding);
		data.limit(data.position() + getDescriptionNameLength());
		return ODSUtils.fromLMBCS(data);
	}

	/**
	 * @return Field Name in Document in which this object resides
	 */
	public String getFieldName() {
		int preceding = getFileObjNameLength() + getDescriptionNameLength();

		ByteBuffer data = getData().duplicate();
		data.position(data.position() + OLE_GUID.SIZE + 28 + preceding);
		data.limit(data.position() + getFieldNameLength());
		return ODSUtils.fromLMBCS(data);
	}

	/**
	 * @return Full Text index $FILE object name
	 */
	public String getTextIndexObjName() {
		int preceding = getFileObjNameLength() + getDescriptionNameLength() + getFieldNameLength();

		ByteBuffer data = getData().duplicate();
		data.position(data.position() + OLE_GUID.SIZE + 28 + preceding);
		data.limit(data.position() + getTextIndexObjNameLength());
		return ODSUtils.fromLMBCS(data);
	}

	/**
	 * @return HTML data, as a byte array
	 */
	public byte[] getHTMLData() {
		int preceding = getFileObjNameLength() + getDescriptionNameLength() + getFieldNameLength() + getTextIndexObjNameLength();

		ByteBuffer data = getData().duplicate();
		int length = getHTMLDataLength();
		byte[] result = new byte[length];
		if (length > 0) {
			data.get(result, OLE_GUID.SIZE + 28 + preceding, length);
		}
		return result;
	}

	/**
	 * @return Associated $FILEs Data
	 */
	public byte[] getAssociatedFILEs() {
		int preceding = getFileObjNameLength() + getDescriptionNameLength() + getFieldNameLength() + getTextIndexObjNameLength()
				+ getHTMLDataLength();

		ByteBuffer data = getData().duplicate();
		int length = getAssociatedFILEsLength();
		byte[] result = new byte[length];
		if (length > 0) {
			data.get(result, OLE_GUID.SIZE + 28 + preceding, length);
		}
		return result;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": OleObjClass=" + getOleObjClass() + ", StorageFormat=" + getStorageFormat()
				+ ", DisplayFormat=" + getDisplayFormat() + ", Flags=" + getFlags() + ", StorageFormatAppearedIn="
				+ getStorageFormatAppearedIn() + ", FileObjectName=" + getFileObjectName() + ", DescriptionName=" + getDescriptionName()
				+ ", FieldName=" + getFieldName() + ", TextIndexObjName=" + getTextIndexObjName() + ", HTMLDataLength="
				+ getHTMLDataLength() + ", FileDataLength=" + getAssociatedFILEsLength() + "]";
	}
}
