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
	public static enum StorageFormatType {
		STRUCT_STORAGE, ISTORAGE_ISTREAM, STRUCT_STREAM
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

	public final WSIG Header = inner(new WSIG());
	public final Unsigned16 FileObjNameLength = new Unsigned16();
	public final Unsigned16 DescriptionNameLength = new Unsigned16();
	public final Unsigned16 FieldNameLength = new Unsigned16();
	public final Unsigned16 TextIndexObjNameLength = new Unsigned16();
	public final OLE_GUID OleObjClass = inner(new OLE_GUID());
	public final Enum16<StorageFormatType> StorageFormat = new Enum16<StorageFormatType>(StorageFormatType.values());
	public final Enum16<DDEFormat> DisplayFormat = new Enum16<DDEFormat>(DDEFormat.values());
	/**
	 * Use getFlags for access
	 */
	@Deprecated
	public final Unsigned32 Flags = new Unsigned32();
	public final Unsigned16 StorageFormatAppearedIn = new Unsigned16();
	public final Unsigned16 HTMLDataLength = new Unsigned16();
	public final Unsigned16 AssociatedFILEsLength = new Unsigned16();
	public final Unsigned16 Reserved3 = new Unsigned16();
	public final Unsigned32 Reserved4 = new Unsigned32();

	static {
		// TODO verify that these are indeed ASCII
		addVariableAsciiString("FileObjectName", "FileObjNameLength");
		addVariableAsciiString("DescriptionName", "DescriptionNameLength");
		addVariableAsciiString("FieldName", "FieldNameLength");
		addVariableAsciiString("TextIndexObjName", "TextIndexObjNameLength");
		addVariableData("HTMLData", "HTMLDataLength");
		addVariableData("AssociatedFILEs", "AssociatedFILEsLength");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf((int) Flags.get());
	}

	/**
	 * Note: this returns a String to handle "x.10" versions
	 * 
	 * @return Version # of Notes, for display purposes
	 */
	public String getStorageFormatAppearedIn() {
		int low = getData().get((int) (getData().position() + OleObjClass.getStructSize() + 16)) & 0xFF;
		int high = getData().get((int) (getData().position() + OleObjClass.getStructSize() + 17)) & 0xFF;
		return high + "." + low;
	}

	/**
	 * @return Name of extendable $FILE object containing object data
	 */
	public String getFileObjectName() {
		return (String) getVariableElement("FileObjectName");
	}

	/**
	 * @return Object description
	 */
	public String getDescriptionName() {
		return (String) getVariableElement("DescriptionName");
	}

	/**
	 * @return Field Name in Document in which this object resides
	 */
	public String getFieldName() {
		return (String) getVariableElement("FieldName");
	}

	/**
	 * @return Full Text index $FILE object name
	 */
	public String getTextIndexObjName() {
		return (String) getVariableElement("TextIndexObjName");
	}

	/**
	 * @return HTML data, as a byte array
	 */
	public byte[] getHTMLData() {
		return (byte[]) getVariableElement("HTMLData");
	}

	/**
	 * @return Associated $FILEs Data
	 */
	public byte[] getAssociatedFILEs() {
		return (byte[]) getVariableElement("AssociatedFILEs");
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": OleObjClass=" + OleObjClass + ", StorageFormat=" + StorageFormat.get()
				+ ", DisplayFormat=" + DisplayFormat.get() + ", Flags=" + getFlags() + ", StorageFormatAppearedIn="
				+ getStorageFormatAppearedIn() + ", FileObjectName=" + getFileObjectName() + ", DescriptionName=" + getDescriptionName()
				+ ", FieldName=" + getFieldName() + ", TextIndexObjName=" + getTextIndexObjName() + "]";
	}
}
