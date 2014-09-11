package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * Beginning header record to both a CDFRAMESET and CDFRAME record. (fsods.h)
 * 
 * @since Lotus Notes/Domino 5.0.1
 *
 */
public class CDFRAMESETHEADER extends CDRecord {

	static {
		addFixed("Version", Short.class);
		addFixedUnsigned("RecCount", Short.class);
		addFixedArray("Reserved", Integer.class, 4);
	}

	public static final int SIZE = getFixedStructSize();

	public CDFRAMESETHEADER(final CDSignature cdSig) {
		super(new WSIG(cdSig, cdSig.getSize() + SIZE), ByteBuffer.wrap(new byte[SIZE]));
	}

	public CDFRAMESETHEADER(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return See FRAMESETHEADER_VERSION
	 */
	public short getVersion() {
		// TODO make enum
		return (Short) getStructElement("Version");
	}

	/**
	 * @return Total number of CDFRAMESET and CDFRAME recs that follow
	 */
	public int getRecCount() {
		return (Integer) getStructElement("RecCount");
	}

	/**
	 * Reserved for future use, must be 0
	 */
	public int[] getReserved() {
		return (int[]) getStructElement("Reserved");
	}
}
