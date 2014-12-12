package org.openntf.domino.nsfdata.structs.cd;

/**
 * These values specify the DDE clipboard format with which data should be rendered. They are used in the CDDDEBEGIN structure and in the
 * CDOLEBEGIN structure.
 *
 */
public enum DDEFormat {
	/**
	 * CF_TEXT
	 */
	TEXT,
	/**
	 * CF_METAFILE or CF_METAFILEPICT
	 */
	METAFILE,
	/**
	 * CF_BITMAP
	 */
	BITMAP,
	/**
	 * Rich Text Format
	 */
	RTF, UNUSED5,
	/**
	 * OLE Ownerlink (never saved in CD_DDE or CD_OLE: used at run time)
	 */
	OWNERLINK,
	/**
	 * OLE Objectlink (never saved in CD_DDE or CD_OLE: used at run time)
	 */
	OBJECTLINK,
	/**
	 * OLE Native (never saved in CD_DDE or CD_OLE: used at run time)
	 */
	NATIVE,
	/**
	 * Program Icon for embedded object
	 */
	ICON
}