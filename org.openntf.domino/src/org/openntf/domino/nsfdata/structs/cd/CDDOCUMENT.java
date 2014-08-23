package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.COLOR_VALUE;
import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This defines the structure of the document information field in a form note. A document information field is an item with name $INFO
 * (ITEM_NAME_DOCUMENT) and data type TYPE_COMPOSITE. The document information field defines attributes of documents created with that form.
 * (editods.h)
 *
 */
public class CDDOCUMENT extends CDRecord {

	protected CDDOCUMENT(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return Color of the paper being used
	 */
	public short getPaperColor() {
		// TODO make an enum
		return getData().getShort(getData().position() + 0);
	}

	/**
	 * @return Form Flags
	 */
	public short getFormFlags() {
		// TODO make an enum
		return getData().getShort(getData().position() + 2);
	}

	/**
	 * @return Privs for notes created when using form
	 */
	public short getNotePrivileges() {
		// TODO make an enum
		return getData().getShort(getData().position() + 4);
	}

	/**
	 * @return more Form Flags
	 */
	public short getFormFlags2() {
		// TODO make an enum
		return getData().getShort(getData().position() + 6);
	}

	/**
	 * @return Length of the name, which follows this struct
	 */
	public short getInherFieldNameLength() {
		return getData().getShort(getData().position() + 8);
	}

	/**
	 * @return Palette Color of the paper being used. Index into 240 color table.
	 * @since Lotus Notes 4.0
	 */
	public short getPaperColorExt() {
		// TODO make an enum
		return getData().getShort(getData().position() + 10);
	}

	/**
	 * @return Paper Color: As of v5.0 stored as RGB, other formats possible
	 */
	public COLOR_VALUE getPaperColorValue() {
		ByteBuffer data = getData().duplicate();
		data.position(getData().position() + 12);
		data.limit(data.position() + 6);
		return new COLOR_VALUE(data);
	}

	public short getFormFlags3() {
		// TODO make an enum
		// There's an extra byte at the end of the color value
		return getData().getShort(getData().position() + 18);
	}

	public String getInheritFieldName() {
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 20);
		data.limit(data.position() + getInherFieldNameLength());
		return ODSUtils.fromLMBCS(data);
	}

	/**
	 * @return string indicating which field to append version number to
	 */
	public String getFieldName() {
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 20 + getInherFieldNameLength());
		return ODSUtils.fromLMBCS(data);
	}
}
