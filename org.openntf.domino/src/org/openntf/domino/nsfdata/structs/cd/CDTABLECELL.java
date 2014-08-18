package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure specifies the cell of a table. Use this structure when accessing a table in a rich text field. (editods.h)
 * 
 * @author jgallagher
 *
 */
public class CDTABLECELL extends CDRecord {

	public CDTABLECELL(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return Row number (0 based)
	 */
	public byte getRow() {
		return getData().get(getData().position() + 0);
	}

	/**
	 * @return Column number (0 based)
	 */
	public byte getColumn() {
		return getData().get(getData().position() + 1);
	}

	/**
	 * @return TWIPS
	 */
	public short getLeftMargin() {
		return getData().getShort(getData().position() + 2);
	}

	/**
	 * @return TWIPS
	 */
	public short getRightMargin() {
		return getData().getShort(getData().position() + 4);
	}

	/**
	 * @return 20" (in twips) * CellWidth / TableWidth Used only if AutoCellWidth is specified in the TABLEBEGIN.
	 */
	public short getFractionalWidth() {
		return getData().getShort(getData().position() + 6);
	}

	/**
	 * @return 4 cell borders, each 2 bits wide (see shift and mask CDTC_xxx values) Value of each cell border is one of TABLE_BORDER_xxx.
	 */
	public byte getBorder() {
		// TODO create enum
		return getData().get(getData().position() + 8);
	}

	/**
	 * @return See CDTABLECELL_xxx
	 */
	public byte getFlags() {
		// TODO create enum
		return getData().get(getData().position() + 9);
	}

	/**
	 * @return Wider borders, see CDTC_xxx_V42_xxx
	 */
	public short getV42Border() {
		// TODO create enum
		return getData().getShort(getData().position() + 10);
	}

	public byte getRowSpan() {
		return getData().get(getData().position() + 12);
	}

	public byte getColumnSpan() {
		return getData().get(getData().position() + 13);
	}

	/**
	 * @return Color of background of cell
	 */
	public short getBackgroundColor() {
		// TODO create enum
		return getData().getShort(getData().position() + 14);
	}
}
