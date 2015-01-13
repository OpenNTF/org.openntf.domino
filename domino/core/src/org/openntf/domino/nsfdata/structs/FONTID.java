package org.openntf.domino.nsfdata.structs;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * This structure allows access to the fields within a FONTID. Overlay this structure onto the FontID field to set or read the font
 * information for the rich text field, or more preferably, use the FontSetxxx macros using a FONTID directly.
 * 
 * The Face member of the FONTIDFIELDS structure may be one of three standard font faces - - FONT_FACE_ROMAN, FONT_FACE_SWISS, or
 * FONT_FACE_TYPEWRITER - - or a font ID resolved by a font table. A Face member value greater than or equal to STATIC_FONT_FACES (5) refers
 * to an entry in a font table.
 * 
 * To resolve a non standard font face, the note must contain a font table. A font table is an item with name $FONT (ITEM_NAME_FONTS) and
 * data type TYPE_COMPOSITE. A font table consists of a CDFONTTABLE structure followed by any number of CDFACE structures. If the Face
 * member value is greater than or equal to STATIC_FONT_FACES, then it must identify one of the CDFACE entries in the font table. (global.h)
 * (fontid.h)
 *
 */
public class FONTID extends AbstractStruct {
	public final Unsigned8 Face = new Unsigned8();
	public final Unsigned8 Attrib = new Unsigned8();
	public final Unsigned8 Color = new Unsigned8();
	public final Unsigned8 PointSize = new Unsigned8();

	@Override
	public void init() {
		super.init();
		getByteBuffer().order(ByteOrder.nativeOrder());
	}

	@Override
	public void init(final ByteBuffer data) {
		super.init(data);
		getByteBuffer().order(ByteOrder.nativeOrder());
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Face=" + Face.get() + ", Attrib=" + Attrib.get() + ", Color=" + Color.get()
				+ ", PointSize=" + PointSize.get() + "]";
	}
}
