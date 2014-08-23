package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.ELEMENTHEADER;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * A button in a layout region of a form is defined by a CDLAYOUTBUTTON record. This record must be between a CDLAYOUT record and a
 * CDLAYOUTEND record. This record is usually followed by other CD records identifying text, graphical, or action elements associated with
 * the button. (editods.h)
 * 
 * @since Lotus Notes 4.1
 *
 */
public class CDLAYOUTBUTTON extends CDRecord {

	public CDLAYOUTBUTTON(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public ELEMENTHEADER getElementHeader() {
		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.limit(data.position() + ELEMENTHEADER.SIZE);
		return new ELEMENTHEADER(data);
	}

	public Set<?> getFlags() {
		return Collections.emptySet();
	}

	public byte[] getReserved() {
		byte[] result = new byte[16];
		getData().duplicate().get(result, 4, 16);
		return result;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + "]";
	}
}
