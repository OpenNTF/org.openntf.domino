package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.BSIG;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD record defines the beginning of a series of CD Records. Not all CD records are enclosed within a CDBEGINRECORD/CDENDRECORD
 * combination. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDBEGINRECORD extends CDRecord {
	public final BSIG Header = inner(new BSIG());
	// TODO map to weird table in docs
	public final Unsigned16 Version = new Unsigned16();
	/**
	 * Use getRecordSignature()
	 */
	@Deprecated
	public final Unsigned16 Signature = new Unsigned16();

	@Override
	public SIG getHeader() {
		return Header;
	}

	public CDSignature getRecordSignature() {
		return CDSignature.sigForShort((short) Signature.get());
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Version=" + Version.get() + ", RecordSignature=" + getRecordSignature() + "]";
	}
}
