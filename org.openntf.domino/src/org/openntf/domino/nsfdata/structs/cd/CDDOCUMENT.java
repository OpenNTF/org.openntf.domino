package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.COLOR_VALUE;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This defines the structure of the document information field in a form note. A document information field is an item with name $INFO
 * (ITEM_NAME_DOCUMENT) and data type TYPE_COMPOSITE. The document information field defines attributes of documents created with that form.
 * (editods.h)
 *
 */
public class CDDOCUMENT extends CDRecord {

	static {
		addFixed("PaperColor", Short.class);
		addFixed("FormFlags", Short.class);
		addFixed("NotePrivileges", Short.class);
		addFixed("FormFlags2", Short.class);
		addFixedUpgrade("InherFieldNameLength", Short.class);
		addFixed("PaperColorExt", Short.class);
		addFixed("PaperColorValue", COLOR_VALUE.class);
		addFixed("FormFlags3", Short.class);
		addFixed("Spare", Short.class);

		addVariableString("InherFieldName", "getInherFieldNameLength");
		addVariableString("FieldName", "getFieldNameLength");
	}

	protected CDDOCUMENT(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return Color of the paper being used
	 */
	public short getPaperColor() {
		// TODO make an enum
		return (Short) getStructElement("PaperColor");
	}

	/**
	 * @return Form Flags
	 */
	public short getFormFlags() {
		// TODO make an enum
		return (Short) getStructElement("FormFlags");
	}

	/**
	 * @return Privs for notes created when using form
	 */
	public short getNotePrivileges() {
		// TODO make an enum
		return (Short) getStructElement("NotePrivileges");
	}

	/**
	 * @return more Form Flags
	 */
	public short getFormFlags2() {
		// TODO make an enum
		return (Short) getStructElement("FormFlags2");
	}

	/**
	 * @return Length of the name, which follows this struct
	 */
	public int getInherFieldNameLength() {
		return (Integer) getStructElement("InherFieldNameLength");
	}

	/**
	 * @return Palette Color of the paper being used. Index into 240 color table.
	 * @since Lotus Notes 4.0
	 */
	public short getPaperColorExt() {
		// TODO make an enum
		return (Short) getStructElement("PaperColorExt");
	}

	/**
	 * @return Paper Color: As of v5.0 stored as RGB, other formats possible
	 */
	public COLOR_VALUE getPaperColorValue() {
		return (COLOR_VALUE) getStructElement("PaperColorValue");
	}

	public short getFormFlags3() {
		// TODO make an enum
		return (Short) getStructElement("FormFlags3");
	}

	public String getInheritFieldName() {
		return (String) getStructElement("InherFieldName");
	}

	public int getFieldNameLength() {
		return getDataLength() - 22 - getInherFieldNameLength();
	}

	/**
	 * @return string indicating which field to append version number to
	 */
	public String getFieldName() {
		return (String) getStructElement("FieldName");
	}
}
