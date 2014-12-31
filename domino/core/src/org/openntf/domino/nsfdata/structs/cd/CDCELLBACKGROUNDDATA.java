package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.RepeatType;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD Record gives information pertaining to Background Data for a Table, specifically the 'Cell Image' repeat value. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDCELLBACKGROUNDDATA extends CDRecord {
	public final Enum8<RepeatType> Repeat = new Enum8<RepeatType>(RepeatType.values());

	public CDCELLBACKGROUNDDATA(final CDSignature cdSig) {
		super(cdSig);
	}

	public CDCELLBACKGROUNDDATA(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}
}
