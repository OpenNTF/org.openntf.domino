package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.BSIG;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD record defines the end of a series of CD records. Not all CD records are enclosed within a CDBEGINRECORD/CDENDRECORD combination.
 * (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDENDRECORD extends CDRecord {
	public final BSIG Header = inner(new BSIG());
	public final Unsigned16 Version = new Unsigned16();
	public final Unsigned16 Signature = new Unsigned16();

	@Override
	public SIG getHeader() {
		return Header;
	}

	/**
	 * @return Signature of record end is for
	 */
	public short getEndSignature() {
		// TODO implement mapping method to CDSignature
		return (short) Signature.get();
	}
}
