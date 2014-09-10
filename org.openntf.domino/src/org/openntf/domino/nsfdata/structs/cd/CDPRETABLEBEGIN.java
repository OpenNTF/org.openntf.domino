package org.openntf.domino.nsfdata.structs.cd;

import java.awt.Color;
import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.COLOR_VALUE;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD record provides additional table properties, expanding the information provided in CDTABLEBEGIN. It will only be recognized in
 * Domino versions 5.0 and greater. This record will be ignored in pre 5.0 versions. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDPRETABLEBEGIN extends CDRecord {

	static {
		addFixed("Flags", Integer.class);
		addFixedUnsigned("Rows", Byte.class);
		addFixedUnsigned("Columns", Byte.class);
		addFixed("ColumnSizingBits1", Integer.class);
		addFixed("ColumnSizingBits2", Integer.class);
		addFixed("ViewerType", Byte.class);
		addFixed("Spare", Byte.class);
		addFixedUnsigned("MinRowHeight", Short.class);
		addFixed("Spares", Short.class);
		addFixed("StyleColor1", Integer.class);
		addFixed("StyleColor2", Integer.class);
		addFixed("InnerBorderColor", COLOR_VALUE.class);
		addFixedUnsigned("NameLength", Short.class);
		addFixed("ImagePacketLength", Short.class);
		addFixed("RowLabelDataLength", Short.class);

		addVariableString("Name", "getNameLength");
	}

	public CDPRETABLEBEGIN(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public int getFlags() {
		// TODO create enum
		return (Integer) getStructElement("Flags");
	}

	public short getRows() {
		return (Short) getStructElement("Rows");
	}

	public short getColumns() {
		return (Short) getStructElement("Columns");
	}

	public int getColumnSizingBits1() {
		return (Integer) getStructElement("ColumnSizingBits1");
	}

	public int getColumnSizingBits2() {
		return (Integer) getStructElement("ColumnSizingBits2");
	}

	public byte getViewerType() {
		// TODO create enum
		return (Byte) getStructElement("ViewerType");
	}

	public byte getSpare() {
		return (Byte) getStructElement("Spare");
	}

	public int getMinRowHeight() {
		return (Integer) getStructElement("MinRowHeight");
	}

	public short getSpare2() {
		return (Short) getStructElement("");
	}

	public Color getStyleColor1() {
		int red = getData().get(getData().position() + 20) & 0xFF;
		int green = getData().get(getData().position() + 21) & 0xFF;
		int blue = getData().get(getData().position() + 22) & 0xFF;
		return new Color(red, green, blue);
	}

	public Color getStyleColor2() {
		int red = getData().get(getData().position() + 24) & 0xFF;
		int green = getData().get(getData().position() + 25) & 0xFF;
		int blue = getData().get(getData().position() + 26) & 0xFF;
		return new Color(red, green, blue);
	}

	public COLOR_VALUE getInnerBorderColor() {
		return (COLOR_VALUE) getStructElement("InnerBorderColor");
	}

	public int getNameLength() {
		return (Integer) getStructElement("NameLength");
	}

	public short getImagePacketLength() {
		return (Short) getStructElement("ImagePacketLength");
	}

	public short getRowLabelDataLength() {
		return (Short) getStructElement("RowLabelDataLength");
	}

	public String getName() {
		return (String) getStructElement("Name");
	}
}
