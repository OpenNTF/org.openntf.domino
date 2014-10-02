package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * New field attributes have been added in Release 4.0 of Notes. To preserve compatibility with existing applications, the new attributes
 * have been placed in this extension to the CDFIELD record. This record is optional, and may not be present in the $Body item of the form
 * note. (editods.h)
 * 
 * @since Lotus Notes 4.0
 */
public class CDEXTFIELD extends CDRecord {
	/**
	 * These identifiers indicate the type of helper in use by the Keyword and the Name helper/pickers.
	 * 
	 * @since Lotus Notes 4.0
	 *
	 */
	public static enum FieldHelper {
		/**
		 * No helper type.
		 */
		NONE((short) 0),
		/**
		 * Mail address dialog helper type.
		 */
		ADDRDLG((short) 1),
		/**
		 * ACL dialog helper type.
		 */
		ACLDLG((short) 2),
		/**
		 * View dialog helper type.
		 */
		VIEWDLG((short) 3);

		private final short value_;

		private FieldHelper(final short value) {
			value_ = value;
		}

		public short getValue() {
			return value_;
		}

		public static FieldHelper valueOf(final short typeCode) {
			for (FieldHelper type : FieldHelper.values()) {
				if (type.getValue() == typeCode) {
					return type;
				}
			}
			throw new IllegalArgumentException("No matching FieldHelper found for type code " + typeCode);
		}
	}

	static {
		addFixed("Flags1", Integer.class);
		addFixed("Flags2", Integer.class);
		addFixed("EntryHelper", Short.class);
		addFixedUnsigned("EntryDBNameLen", Short.class);
		addFixedUnsigned("EntryViewNameLen", Short.class);
		addFixedUnsigned("EntryColumnNumber", Short.class);

		addVariableString("EntryDBName", "getEntryDBNameLen");
		addVariableString("EntryViewName", "getEntryViewNameLen");
	}

	public static final int SIZE = getFixedStructSize();

	public CDEXTFIELD(final CDSignature cdSig) {
		super(new WSIG(cdSig, cdSig.getSize() + SIZE), ByteBuffer.wrap(new byte[SIZE]));
	}

	public CDEXTFIELD(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return Field Flags (see FEXT_xxx)
	 */
	public int getFlags1() {
		// TODO make enum
		return (Integer) getStructElement("Flags1");
	}

	public int getFlags2() {
		// TODO make enum
		return (Integer) getStructElement("Flags2");
	}

	/**
	 * @return Field entry helper type (see FIELD_HELPER_xxx)
	 */
	public FieldHelper getEntryHelper() {
		return FieldHelper.valueOf((Short) getStructElement("EntryHelper"));
	}

	/**
	 * @return Entry helper DB name length
	 */
	public int getEntryDBNameLen() {
		return (Integer) getStructElement("EntryDBNameLen");
	}

	/**
	 * @return Entry helper View name length
	 */
	public int getEntryViewNameLen() {
		return (Integer) getStructElement("EntryViewNameLen");
	}

	/**
	 * @return Entry helper column number
	 */
	public int getEntryColumnNumber() {
		return (Integer) getStructElement("EntryColumnNumber");
	}

	public String getEntryHelperDBName() {
		return (String) getStructElement("EntryDBName");
	}

	public String getEntryHelperViewName() {
		return (String) getStructElement("EntryViewName");
	}
}
