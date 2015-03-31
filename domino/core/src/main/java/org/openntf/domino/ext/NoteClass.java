package org.openntf.domino.ext;

/**
 * Enum for NOTE_CLASS_xxx
 * 
 * @author Roland Praml, FOCONIS AG
 *
 */
public enum NoteClass {

	UNKNOWN(0x0000),  			//	Hex value (from nsfnote.h) 
	DOCUMENT(0x0001), 			//	Document note

	/** notefile info (help-about) note */
	INFO(0x0002),
	/** form note */
	FORM(0x0004),
	/** view note */
	VIEW(0x0008),
	/** icon note */
	ICON(0x0010),
	/** design note collection - not exportable! */
	DESIGN(0x0020),
	/** acl note - not exportable - but fixed by ODA */
	ACL(0x0040),
	/** Notes product help index note */
	HELP_INDEX(0x0080),
	/** designer's help note (=HelpUsing) */
	HELP(0x0100),
	/** filter note */
	FILTER(0x0200),
	/** field note */
	FIELD(0x0400),
	/** replication formula */
	REPLFORMULA(0x0800);
	public final int nativeValue;

	private String strID_;

	NoteClass(final int nativeValue) {
		this.nativeValue = nativeValue;
	}

	public int defaultIDint() {
		return 0xFFFF0000 | nativeValue;
	}

	public String defaultID() {
		if (strID_ == null)
			strID_ = Integer.toHexString(defaultIDint());
		return strID_;
	}

	public static NoteClass valueOf(final int iClass) {
		for (NoteClass value : values()) {
			if ((iClass & value.nativeValue) > 0)
				return value;
		}
		return NoteClass.UNKNOWN;
	}
}
