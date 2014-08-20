package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure specifies the beginning of a table. It contains information about the format and size of the table. Use this structure
 * when accessing a table in a rich text field. As of R5, this structure is preceded by a CDPRETABLEBEGIN structure. The CDPRETABLEBEGIN
 * structure specifies additional table properties. (editods.h)
 * 
 * @author jgallagher
 *
 */
public class CDTABLEBEGIN extends CDRecord {

	public CDTABLEBEGIN(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return TWIPS
	 */
	public short getLeftMargin() {
		// TODO add TWIPS conversion
		return getData().getShort(getData().position() + 0);
	}

	/**
	 * @return TWIPS
	 */
	public short getHorizInterCellSpace() {
		return getData().getShort(getData().position() + 2);
	}

	/**
	 * @return TWIPS
	 */
	public short getVertInterCellSpace() {
		return getData().getShort(getData().position() + 4);
	}

	/**
	 * @return TWIPS -- field was spare in V3
	 */
	public short getV4HorizInterCellSpace() {
		return getData().getShort(getData().position() + 6);
	}

	/**
	 * @return TWIPS -- field was spare in V3
	 */
	public short getV4VertInterCellSpace() {
		return getData().getShort(getData().position() + 8);
	}

	/**
	 * @return Flags
	 */
	public short getFlags() {
		// TODO create enum
		return getData().getShort(getData().position() + 10);
	}
}
