package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * Contains collapsible section, button type, style sheet or field limit information for Notes/Domino 6. A CD record (CDBAR, CDBUTTON,
 * CDBORDERINFO, CDFIELDHINT, etc.) may be followed by a CDDATAFLAGS structure. (editods.h)
 * 
 * @author jgallagher
 * @since Lotus Notes/Domino 6.0
 */
public class CDDATAFLAGS extends CDRecord {

	public CDDATAFLAGS(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return number of flags
	 */
	public short getNumFlags() {
		return getData().getShort(getData().position() + 0);
	}

	/**
	 * @return Element these flags are for, CD_xxx_ELEMENT
	 */
	public short getElemType() {
		// TODO make enum
		return getData().getShort(getData().position() + 2);
	}

	/**
	 * @return Future
	 */
	public int getReserved() {
		return getData().getInt(getData().position() + 4);
	}

	public int[] getFlags() {
		int[] result = new int[getNumFlags()];
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 8);
		for (int i = 0; i < getNumFlags(); i++) {
			result[i] = data.getInt();
		}
		return result;
	}
}
