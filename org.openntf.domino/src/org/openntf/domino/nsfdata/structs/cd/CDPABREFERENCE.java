package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This structure is placed at the start of each paragraph in a rich-text field, and specifies which CDPABDEFINITION is used as the format
 * for the paragraph. (editods.h)
 *
 */
public class CDPABREFERENCE extends CDRecord {

	static {
		addFixed("PABID", Short.class);
	}

	public static final int SIZE = getFixedStructSize();

	public CDPABREFERENCE(final CDSignature cdSig) {
		super(new WSIG(cdSig, cdSig.getSize() + SIZE), ByteBuffer.wrap(new byte[SIZE]));
	}

	public CDPABREFERENCE(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return ID number of the CDPABDEFINITION used by this paragraph
	 */
	public short getPabId() {
		return (Short) getStructElement("PABID");
	}
}
