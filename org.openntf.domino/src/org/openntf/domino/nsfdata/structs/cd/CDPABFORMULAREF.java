package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * Starting in Release 4.0 of Notes, a paragraph may have an associated formula that determines when the paragraph is to be hidden. The
 * CDPABFORMULAREF is similar to the CDPABREFERENCE record, with an additional field that identifies the CDPABDEFINITION record containing
 * the CDPABHIDE record for the "Hide When" formula. (editods.h)
 * 
 * @since Lotus Notes 4.0
 *
 */
public class CDPABFORMULAREF extends CDRecord {

	static {
		addFixed("SourcePABID", Short.class);
		addFixed("DestPABID", Short.class);
	}

	public CDPABFORMULAREF(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return ID number of the source PAB containing the formula.
	 */
	public short getSourcePabId() {
		return (Short) getStructElement("SourcePABID");
	}

	/**
	 * @return ID number of the dest PAB
	 */
	public short getDestPabId() {
		return (Short) getStructElement("DestPABID");
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": SourcePabId=" + getSourcePabId() + ", DestPabId=" + getDestPabId() + "]";
	}
}
