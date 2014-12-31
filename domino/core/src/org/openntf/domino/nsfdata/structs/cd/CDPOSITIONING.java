package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.LENGTH_VALUE;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD record contains position information for a layer box. (editods.h)
 * 
 * @since Lotus Notes/Domino 6.0
 *
 */
public class CDPOSITIONING extends CDRecord {

	public static enum SchemeType {
		STATIC, ABSOLUTE, RELATIVE, FIXED
	}

	public final Enum8<SchemeType> Scheme = new Enum8<SchemeType>(SchemeType.values());
	public final Unsigned8 bReserved = new Unsigned8();
	public final Signed32 ZIndex = new Signed32();
	public final LENGTH_VALUE Top = new LENGTH_VALUE();
	public final LENGTH_VALUE Left = new LENGTH_VALUE();
	public final LENGTH_VALUE Bottom = new LENGTH_VALUE();
	public final LENGTH_VALUE Right = new LENGTH_VALUE();
	public final Float64 BrowserLeftOffset = new Float64();
	public final Float64 BrowserRightOffset = new Float64();

	public CDPOSITIONING(final CDSignature cdSig) {
		super(cdSig);
	}

	public CDPOSITIONING(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}
}
