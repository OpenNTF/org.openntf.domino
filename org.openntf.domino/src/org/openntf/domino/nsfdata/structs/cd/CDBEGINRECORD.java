package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD record defines the beginning of a series of CD Records. Not all CD records are enclosed within a CDBEGINRECORD/CDENDRECORD
 * combination. (editods.h)
 * 
 * @author jgallagher
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDBEGINRECORD extends CDRecord {

	protected CDBEGINRECORD(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public short getVersion() {
		return getData().getShort(getData().position() + 0);
	}

	/**
	 * @return Signature of record begin is for
	 */
	public short getBeginSignature() {
		// TODO implement mapping method to CDSignature
		return getData().getShort(getData().position() + 2);
	}
}
