package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This structure specifies the cell of a table. Use this structure when accessing a table in a rich text field. (editods.h)
 *
 */
public class CDTABLECELL extends CDRecord {

	static {
		addFixedUnsigned("Row", Byte.class);
		addFixedUnsigned("Column", Byte.class);
		addFixedUnsigned("LeftMargin", Short.class);
		addFixedUnsigned("RightMargin", Short.class);
		addFixedUnsigned("FractionalWidth", Short.class);
		addFixed("Border", Byte.class);
		addFixed("Flags", Byte.class);
		addFixed("v42Border", Short.class);
		addFixedUnsigned("RowSpan", Short.class);
		addFixedUnsigned("ColumnSpan", Short.class);
		addFixed("BackgroundColor", Short.class);
	}

	public static final int SIZE = getFixedStructSize();

	public CDTABLECELL(final CDSignature cdSig) {
		super(new WSIG(cdSig, cdSig.getSize() + SIZE), ByteBuffer.wrap(new byte[SIZE]));
	}

	public CDTABLECELL(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return Row number (0 based)
	 */
	public short getRow() {
		return (Short) getStructElement("Row");
	}

	/**
	 * @return Column number (0 based)
	 */
	public short getColumn() {
		return (Short) getStructElement("Column");
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
	public int getRightMargin() {
		return (Integer) getStructElement("RightMargin");
	}

	/**
	 * @return 20" (in twips) * CellWidth / TableWidth Used only if AutoCellWidth is specified in the TABLEBEGIN.
	 */
	public int getFractionalWidth() {
		return (Integer) getStructElement("FractionalWidth");
	}

	/**
	 * @return 4 cell borders, each 2 bits wide (see shift and mask CDTC_xxx values) Value of each cell border is one of TABLE_BORDER_xxx.
	 */
	public byte getBorder() {
		// TODO create enum
		return (Byte) getStructElement("Border");
	}

	/**
	 * @return See CDTABLECELL_xxx
	 */
	public byte getFlags() {
		// TODO create enum
		return (Byte) getStructElement("Flags");
	}

	/**
	 * @return Wider borders, see CDTC_xxx_V42_xxx
	 */
	public short getV42Border() {
		// TODO create enum
		return (Short) getStructElement("v42Border");
	}

	public short getRowSpan() {
		return (Short) getStructElement("RowSpan");
	}

	public short getColumnSpan() {
		return (Short) getStructElement("ColumnSpan");
	}

	/**
	 * @return Color of background of cell
	 */
	public short getBackgroundColor() {
		// TODO create enum
		return (Short) getStructElement("BackgroundColor");
	}
}
