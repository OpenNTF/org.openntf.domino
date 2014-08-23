package org.openntf.domino.nsfdata.structs.cd;

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

	public CDPRETABLEBEGIN(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public int getFlags() {
		// TODO create enum
		return getData().getInt(getData().position() + 0);
	}

	public byte getRows() {
		return getData().get(getData().position() + 4);
	}

	public byte getColumns() {
		return getData().get(getData().position() + 5);
	}

	public int getColumnSizingBits1() {
		return getData().getInt(getData().position() + 10);
	}

	public int getColumnSizingBits2() {
		return getData().getInt(getData().position() + 14);
	}

	public byte getViewerType() {
		// TODO create enum
		return getData().get(getData().position() + 20);
	}

	public byte getSpare() {
		return getData().get(getData().position() + 21);
	}

	public short getMinRowHeight() {
		return getData().getShort(getData().position() + 22);
	}

	public short getSpare2() {
		return getData().getShort(getData().position() + 24);
	}

	// TODO determine if there's a reason the docs call this DWORD instead of COLOR_VALUE
	public COLOR_VALUE getStyleColor1() {
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 26);
		data.limit(data.position() + 6);
		return new COLOR_VALUE(data);
	}

	public COLOR_VALUE getStyleColor2() {
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 32);
		data.limit(data.position() + 6);
		return new COLOR_VALUE(data);
	}

	public COLOR_VALUE getInnerBorderColor() {
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 38);
		data.limit(data.position() + 6);
		return new COLOR_VALUE(data);
	}

	public short getNameLength() {
		return getData().getShort(getData().position() + 44);
	}

	public short getImagePacketLength() {
		return getData().getShort(getData().position() + 46);
	}

	public short getRowLabelDataLength() {
		return getData().getShort(getData().position() + 48);
	}
}
