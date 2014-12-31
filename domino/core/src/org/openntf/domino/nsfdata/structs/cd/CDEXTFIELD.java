package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

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
		NONE, ADDRDLG, ACLDLG, VIEWDLG
	}

	// TODO make enum
	public final Unsigned32 Flags1 = new Unsigned32();
	// TODO make enum
	public final Unsigned32 Flags2 = new Unsigned32();
	public final Enum16<FieldHelper> EntryHelper = new Enum16<FieldHelper>(FieldHelper.values());
	public final Unsigned16 EntryDBNameLen = new Unsigned16();
	public final Unsigned16 EntryViewNameLen = new Unsigned16();
	public final Unsigned16 EntryColumnNumber = new Unsigned16();

	static {
		addVariableString("EntryDBName", "EntryDBNameLen");
		addVariableString("EntryViewName", "EntryViewNameLen");
	}

	public CDEXTFIELD(final CDSignature cdSig) {
		super(cdSig);
	}

	public CDEXTFIELD(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public String getEntryHelperDBName() {
		return (String) getVariableElement("EntryDBName");
	}

	public String getEntryHelperViewName() {
		return (String) getVariableElement("EntryViewName");
	}
}
