package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import org.openntf.domino.nsfdata.structs.COLOR_VALUE;
import org.openntf.domino.nsfdata.structs.FRAMESETLENGTH;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * A FRAMESETLENGTH structure will follow depending on the value found in either RowQty or ColQty. There could be multiple FRAMESETLENGTH
 * structures defining the RowQty and ColQty values. (fsods.h)
 * 
 * @since Lotus Notes/Domino 5.0.1
 *
 */
public class CDFRAMESET extends CDRecord {
	public CDFRAMESET(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return fFSxxxxxxx as defined below. Unused bits must be set to 0
	 */
	public int getFlags() {
		// TODO make enum
		return getData().getInt(getData().position() + 0);
	}

	/**
	 * @return HTML FRAMEBORDER attribute
	 */
	public boolean getBorderEnable() {
		return getData().get(getData().position() + 4) != 0;
	}

	/**
	 * Reserved, must be 0
	 */
	public byte getAvailable1() {
		return getData().get(getData().position() + 5);
	}

	/**
	 * Reserved, must be 0
	 */
	public short getReserved1() {
		return getData().getShort(getData().position() + 6);
	}

	/**
	 * Reserved, must be 0
	 */
	public short getReserved2() {
		return getData().getShort(getData().position() + 8);
	}

	/**
	 * @return HTML BORDER attribute
	 */
	public int getFrameBorderWidth() {
		return getData().getShort(getData().position() + 10) & 0xFFFF;
	}

	/**
	 * Reserved, must be 0
	 */
	public short getReserved3() {
		return getData().getShort(getData().position() + 12);
	}

	/**
	 * @return HTML FRAMESPACING attribute
	 */
	public int getFrameSpacingWidth() {
		return getData().getShort(getData().position() + 14) & 0xFFFF;
	}

	/**
	 * Reserved, must be 0
	 */
	public short getReserved4() {
		return getData().getShort(getData().position() + 16);
	}

	/**
	 * reserved for future use
	 */
	public COLOR_VALUE getReservedColor1() {
		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 18);
		data.limit(data.position() + 6);
		return new COLOR_VALUE(data);
	}

	/**
	 * reserved for future use
	 */
	public COLOR_VALUE getReservedColor2() {
		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 24);
		data.limit(data.position() + 6);
		return new COLOR_VALUE(data);
	}

	/**
	 * @return The number of FRAMESETLENGTH structures defining row information
	 */
	public int getRowQty() {
		return getData().getShort(getData().position() + 30) & 0xFFFF;
	}

	/**
	 * @return The number of FRAMESETLENGTH structures defining column information
	 */
	public int getColQty() {
		return getData().getShort(getData().position() + 32) & 0xFFFF;
	}

	/**
	 * Reserved, must be 0
	 */
	public short getReserved5() {
		return getData().getShort(getData().position() + 34);
	}

	/**
	 * Reserved, must be 0
	 */
	public short getReserved6() {
		return getData().getShort(getData().position() + 36);
	}

	/**
	 * @return HTML BORDERCOLOR attribute
	 */
	public COLOR_VALUE getFrameBorderColor() {
		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 38);
		data.limit(data.position() + 6);
		return new COLOR_VALUE(data);
	}

	/**
	 * @return Theme Setting
	 * @since IBM Notes/Domino 8.5
	 */
	public byte getThemeSetting() {
		// TODO make enum?
		return getData().get(getData().position() + 44);
	}

	/**
	 * Reserved, must be 0
	 */
	public byte getReserved7() {
		return getData().get(getData().position() + 45);
	}

	public FRAMESETLENGTH[] getRows() {
		int count = getRowQty();
		FRAMESETLENGTH[] result = new FRAMESETLENGTH[count];
		for (int i = 0; i < count; i++) {
			ByteBuffer data = getData().duplicate();
			data.position(data.position() + 46 + (4 * i));
			data.limit(data.position() + 4);
			result[i] = new FRAMESETLENGTH(data);
		}
		return result;
	}

	public FRAMESETLENGTH[] getCols() {
		int preceding = 4 * getRowQty();

		int count = getColQty();
		FRAMESETLENGTH[] result = new FRAMESETLENGTH[count];
		for (int i = 0; i < count; i++) {
			ByteBuffer data = getData().duplicate();
			data.position(data.position() + 46 + preceding + (4 * i));
			data.limit(data.position() + 4);
			result[i] = new FRAMESETLENGTH(data);
		}
		return result;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ", BorderEnable: " + getBorderEnable() + ", FrameBorderWidth: " + getFrameBorderWidth()
				+ ", FrameSpacingWidth: " + getFrameSpacingWidth() + ", FrameBorderColor: " + getFrameBorderColor() + ", ThemeSetting: "
				+ getThemeSetting() + ", RowQty: " + getRowQty() + ", ColQty: " + getColQty() + ", Rows: " + Arrays.asList(getRows())
				+ ", Cols: " + Arrays.asList(getCols()) + "]";
	}
}
