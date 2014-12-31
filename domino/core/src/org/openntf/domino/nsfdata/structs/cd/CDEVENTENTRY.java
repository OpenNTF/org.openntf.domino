package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * Contains additional event information for Notes/Domino 6. (editods.h)
 * 
 * @since Lotus Notes/Domino 6.0
 *
 */
public class CDEVENTENTRY extends CDRecord {
	public static enum Platform {
		UNUSED0, CLIENT_ODS, WEB_ODS
	}

	public final Enum16<Platform> wPlatform = new Enum16<Platform>(Platform.values());
	// TODO expand HTMLEvent from CDEVENT to work with getter for this value
	public final Unsigned16 wEventId = new Unsigned16();
	// TODO add enum with getter for this (ACTION_* skips values and Enum16 wouldn't suffice)
	public final Unsigned16 wActionType = new Unsigned16();
	public final Unsigned16 wReserved = new Unsigned16();
	public final Unsigned32 dwReserved = new Unsigned32();

	public CDEVENTENTRY(final CDSignature cdSig) {
		super(cdSig);
	}

	public CDEVENTENTRY(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}
}
