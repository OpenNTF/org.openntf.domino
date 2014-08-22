package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD record defines the end of a series of CD records. Not all CD records are enclosed within a CDBEGINRECORD/CDENDRECORD combination.
 * (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDENDRECORD extends CDRecord {

	public CDENDRECORD(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public short getVersion() {
		return getData().getShort(getData().position() + 0);
	}

	/**
	 * @return Signature of record end is for
	 */
	public short getEndSignature() {
		// TODO implement mapping method to CDSignature
		return getData().getShort(getData().position() + 2);
	}
}
