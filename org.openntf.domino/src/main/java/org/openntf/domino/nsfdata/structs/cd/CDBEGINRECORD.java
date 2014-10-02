package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This CD record defines the beginning of a series of CD Records. Not all CD records are enclosed within a CDBEGINRECORD/CDENDRECORD
 * combination. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDBEGINRECORD extends CDRecord {

	static {
		addFixed("Version", Short.class);
		addFixed("Signature", Short.class);
	}

	public static final int SIZE = getFixedStructSize();

	public CDBEGINRECORD(final CDSignature cdSig) {
		super(new WSIG(cdSig, cdSig.getSize() + SIZE), ByteBuffer.wrap(new byte[SIZE]));
	}

	public CDBEGINRECORD(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public short getVersion() {
		// TODO map to weird table in docs
		return (Short) getStructElement("Version");
	}

	/**
	 * @return Signature of record begin is for
	 */
	public short getBeginSignature() {
		// TODO implement mapping method to CDSignature
		return (Short) getStructElement("Signature");
	}
}
