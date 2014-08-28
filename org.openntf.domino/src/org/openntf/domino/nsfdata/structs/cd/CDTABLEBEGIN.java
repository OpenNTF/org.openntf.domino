package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure specifies the beginning of a table. It contains information about the format and size of the table. Use this structure
 * when accessing a table in a rich text field. As of R5, this structure is preceded by a CDPRETABLEBEGIN structure. The CDPRETABLEBEGIN
 * structure specifies additional table properties. (editods.h)
 *
 */
public class CDTABLEBEGIN extends CDRecord {

	static {
		addFixedUnsigned("LeftMargin", Short.class);
		addFixedUnsigned("HorizInterCellSpace", Short.class);
		addFixedUnsigned("VertInterCellSpace", Short.class);
		addFixedUnsigned("V4HorizInterCellSpace", Short.class);
		addFixedUnsigned("V4VertInterCellSpace", Short.class);
		addFixed("Flags", Short.class);
	}

	public CDTABLEBEGIN(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return TWIPS
	 */
	public int getLeftMargin() {
		return (Integer) getStructElement("LeftMargin");
	}

	/**
	 * @return TWIPS
	 */
	public int getHorizInterCellSpace() {
		return (Integer) getStructElement("HorizInterCellSpace");
	}

	/**
	 * @return TWIPS
	 */
	public int getVertInterCellSpace() {
		return (Integer) getStructElement("VertInterCellSpace");
	}

	/**
	 * @return TWIPS -- field was spare in V3
	 */
	public int getV4HorizInterCellSpace() {
		return (Integer) getStructElement("V4HorizInterCellSpace");
	}

	/**
	 * @return TWIPS -- field was spare in V3
	 */
	public int getV4VertInterCellSpace() {
		return (Integer) getStructElement("V4VertInterCellSpace");
	}

	/**
	 * @return Flags
	 */
	public short getFlags() {
		// TODO create enum
		return (Short) getStructElement("Flags");
	}
}
