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

	static {
		addFixed("wPlatform", Short.class);
		addFixed("wEventId", Short.class);
		addFixed("wActionType", Short.class);
		addFixed("wReserved", Short.class);
		addFixed("dwReserved", Integer.class);
	}

	protected CDEVENTENTRY(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return Platform type
	 */
	public short getPlatform() {
		// TODO create enum
		return (Short) getStructElement("wPlatform");
	}

	/**
	 * @return Event id. The event that this will run on... OnClick, Exit, etc.
	 */
	public short getEventId() {
		// TODO create enum
		return (Short) getStructElement("wEventId");
	}

	/**
	 * @return Action type (the language... LotusScript, Javascript, Formula, etc.
	 */
	public short getActionType() {
		// TODO create enum
		return (Short) getStructElement("wActionType");
	}

	/**
	 * @return future use
	 */
	public short getReserved() {
		return (Short) getStructElement("wReserved");
	}

	/**
	 * @return future use
	 */
	public int getReserved2() {
		return (Integer) getStructElement("dwReserved");
	}
}
