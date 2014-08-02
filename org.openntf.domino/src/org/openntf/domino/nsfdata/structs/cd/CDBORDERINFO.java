package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.openntf.domino.nsfdata.structs.COLOR_VALUE;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD record describes border information for a given table. This CD record will be preceded with CD record CDPRETABLEBEGIN both
 * encapsulated between a CDBEGINRECORD and a CDENDRECORD record with CD record signature CDPRETABLEBEGIN. (editods.h)
 * 
 * @author jgallagher
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDBORDERINFO extends CDRecord {

	public CDBORDERINFO(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * Not Used must be 0
	 */
	public int getFlags() {
		return getData().getInt(getData().position() + 0);
	}

	/**
	 * @return CDBORDERSTYLE_xxx
	 */
	public int getBorderStyle() {
		// TODO make enum
		return getData().getInt(getData().position() + 4);
	}

	/**
	 * @return Thickness Top
	 */
	public short getBorderWidthTop() {
		return getData().getShort(getData().position() + 8);
	}

	/**
	 * @return Thickness Left
	 */
	public short getBorderWidthLeft() {
		return getData().getShort(getData().position() + 10);
	}

	/**
	 * @return Thickness Bottom
	 */
	public short getBorderWidthBottom() {
		return getData().getShort(getData().position() + 12);
	}

	/**
	 * @return Thickness Right
	 */
	public short getBorderWidthRight() {
		return getData().getShort(getData().position() + 14);
	}

	public int getSpare() {
		return getData().getInt(getData().position() + 16);
	}

	/**
	 * @return CDBORDER_FLAGS_xxx
	 */
	public short getBorderFlags() {
		// TODO make enum
		return getData().getShort(getData().position() + 20);
	}

	/**
	 * @return Border Effects Drop Shadow Width
	 */
	public short getDropShadowWidth() {
		return getData().getShort(getData().position() + 22);
	}

	/**
	 * @return Inside Thickness Top
	 */
	public short getInnerWidthTop() {
		return getData().getShort(getData().position() + 24);
	}

	/**
	 * @return Inside Thickness Left
	 */
	public short getInnerWidthLeft() {
		return getData().getShort(getData().position() + 26);
	}

	/**
	 * @return Inside Thickness Bottom
	 */
	public short getInnerWidthBottom() {
		return getData().getShort(getData().position() + 28);
	}

	/**
	 * @return Inside Thickness Right
	 */
	public short getInnerWidthRight() {
		return getData().getShort(getData().position() + 32);
	}

	/**
	 * @return Outside Thickness Top
	 */
	public short getOuterWidthTop() {
		return getData().getShort(getData().position() + 34);
	}

	/**
	 * @return Outside Thickness Left
	 */
	public short getOuterWidthLeft() {
		return getData().getShort(getData().position() + 36);
	}

	/**
	 * @return Outside Thickness Bottom
	 */
	public short getOuterWidthBottom() {
		return getData().getShort(getData().position() + 38);
	}

	/**
	 * @return Outside Thickness Right
	 */
	public short getOuterWidthRight() {
		return getData().getShort(getData().position() + 40);
	}

	/**
	 * @return Border Color
	 */
	public COLOR_VALUE getColor() {
		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 42);
		data.limit(data.position() + 6);
		return new COLOR_VALUE(data);
	}

	public short[] getSpares() {
		short[] result = new short[5];
		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 48);
		data.limit(data.position() + 10);
		for (int i = 0; i < 5; i++) {
			result[i] = data.getShort();
		}
		return result;
	}
}
