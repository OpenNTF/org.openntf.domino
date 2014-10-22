package org.openntf.domino.nsfdata.structs.cd;

/**
 * These values specify the DDE clipboard format with which data should be rendered. They are used in the CDDDEBEGIN structure and in
 * the CDOLEBEGIN structure.
 *
 */
public enum DDEFormat {
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