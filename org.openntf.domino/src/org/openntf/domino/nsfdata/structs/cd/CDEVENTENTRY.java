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

	protected CDEVENTENTRY(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return Platform type
	 */
	public short getPlatform() {
		// TODO create enum
		return getData().getShort(getData().position() + 0);
	}

	/**
	 * @return Event id. The event that this will run on... OnClick, Exit, etc.
	 */
	public short getEventId() {
		// TODO create enum
		return getData().getShort(getData().position() + 2);
	}

	/**
	 * @return Action type (the language... LotusScript, Javascript, Formula, etc.
	 */
	public short getActionType() {
		// TODO create enum
		return getData().getShort(getData().position() + 4);
	}

	/**
	 * @return future use
	 */
	public short getReserved() {
		return getData().getShort(getData().position() + 6);
	}

	/**
	 * @return future use
	 */
	public int getReserved2() {
		return getData().getInt(getData().position() + 8);
	}
}
