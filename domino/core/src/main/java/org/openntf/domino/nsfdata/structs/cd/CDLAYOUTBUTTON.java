package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.ELEMENTHEADER;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * A button in a layout region of a form is defined by a CDLAYOUTBUTTON record. This record must be between a CDLAYOUT record and a
 * CDLAYOUTEND record. This record is usually followed by other CD records identifying text, graphical, or action elements associated with
 * the button. (editods.h)
 * 
 * @since Lotus Notes 4.1
 *
 */
public class CDLAYOUTBUTTON extends CDRecord {

	static {
		addFixed("ElementHeader", ELEMENTHEADER.class);
		addFixed("Flags", Integer.class);
		addFixedArray("Reserved", Byte.class, 16);
	}

	public static final int SIZE = getFixedStructSize();

	public CDLAYOUTBUTTON(final CDSignature cdSig) {
		super(new WSIG(cdSig, cdSig.getSize() + SIZE), ByteBuffer.wrap(new byte[SIZE]));
	}

	public CDLAYOUTBUTTON(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public ELEMENTHEADER getElementHeader() {
		return (ELEMENTHEADER) getStructElement("ElementHeader");
	}

	public Set<?> getFlags() {
		return Collections.emptySet();
	}

	public byte[] getReserved() {
		return (byte[]) getStructElement("Reserved");
	}
}
