package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.BSIG;
import org.openntf.domino.nsfdata.structs.COLOR_VALUE;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This defines the structure of the document information field in a form note. A document information field is an item with name $INFO
 * (ITEM_NAME_DOCUMENT) and data type TYPE_COMPOSITE. The document information field defines attributes of documents created with that form.
 * (editods.h)
 *
 */
public class CDDOCUMENT extends CDRecord {
	public final BSIG Header = inner(new BSIG());
	/**
	 * Use getPaperColor for access.
	 */
	@Deprecated
	public final Unsigned16 PaperColor = new Unsigned16();
	/**
	 * Use getFormFlags for access.
	 */
	@Deprecated
	public final Unsigned16 FormFlags = new Unsigned16();
	/**
	 * Use getNotePrivileges for access.
	 */
	@Deprecated
	public final Unsigned16 NotePrivileges = new Unsigned16();
	/**
	 * Use getFormFlags2 for access.
	 */
	@Deprecated
	public final Unsigned16 FormFlags2 = new Unsigned16();
	public final Unsigned16 InherFieldNameLength = new Unsigned16();
	/**
	 * Use getPaperColorExt for access.
	 */
	@Deprecated
	public final Unsigned16 PaperColorExt = new Unsigned16();
	public final COLOR_VALUE PaperColorValue = inner(new COLOR_VALUE());
	/**
	 * Use getFormFlags3 for access.
	 */
	@Deprecated
	public final Unsigned16 FormFlags3 = new Unsigned16();
	public final Unsigned16[] Spare = array(new Unsigned16[1]);

	static {
		addVariableString("InherFieldName", "InherFieldNameLength");
		addVariableString("FieldName", "getFieldNameLength");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	/**
	 * @return Color of the paper being used
	 */
	public short getPaperColor() {
		// TODO make an enum
		return (short) PaperColor.get();
	}

	/**
	 * @return Form Flags
	 */
	public short getFormFlags() {
		// TODO make an enum
		return (short) FormFlags.get();
	}

	/**
	 * @return Privs for notes created when using form
	 */
	public short getNotePrivileges() {
		// TODO make an enum
		return (short) NotePrivileges.get();
	}

	/**
	 * @return more Form Flags
	 */
	public short getFormFlags2() {
		// TODO make an enum
		return (short) FormFlags2.get();
	}

	/**
	 * @return Palette Color of the paper being used. Index into 240 color table.
	 * @since Lotus Notes 4.0
	 */
	public short getPaperColorExt() {
		// TODO make an enum
		return (short) PaperColorExt.get();
	}

	public short getFormFlags3() {
		// TODO make an enum
		return (short) FormFlags3.get();
	}

	public String getInheritFieldName() {
		return (String) getVariableElement("InherFieldName");
	}

	public int getFieldNameLength() {
		return (int) (Header.getRecordLength() - size() - InherFieldNameLength.get());
	}

	/**
	 * @return string indicating which field to append version number to
	 */
	public String getFieldName() {
		return (String) getVariableElement("FieldName");
	}
}
