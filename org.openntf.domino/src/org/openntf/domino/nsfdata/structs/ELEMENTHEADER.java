package org.openntf.domino.nsfdata.structs;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * This structure contains the common fields for the graphical elements in a layout region of a form.
 * 
 * @since Lotus Notes 4.1
 *
 */
public class ELEMENTHEADER extends AbstractStruct {
	public static final int SIZE = 10 + FONTID.SIZE + COLOR_VALUE.SIZE;

	public ELEMENTHEADER(final ByteBuffer data) {
		super(data);
	}

	@Override
	public int getStructSize() {
		return SIZE;
	}

	/**
	 * @return Location of the left edge of the element in twips
	 */
	public int getLeft() {
		return getData().getShort(getData().position() + 0) & 0xFFFF;
	}

	/**
	 * @return Location of the top of the element in twips
	 */
	public int getTop() {
		return getData().getShort(getData().position() + 2) & 0xFFFF;
	}

	/**
	 * @return Width of the element in twips
	 */
	public int getWidth() {
		return getData().getShort(getData().position() + 4) & 0xFFFF;
	}

	/**
	 * @return Height of the element in twips
	 */
	public int getHeight() {
		return getData().getShort(getData().position() + 6) & 0xFFFF;
	}

	/**
	 * @return Font used to display text in the element
	 */
	public FONTID getFontId() {
		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 8);
		data.limit(data.position() + FONTID.SIZE);
		return new FONTID(data);
	}

	/**
	 * @return Background color for the element
	 */
	public byte getBackColor() {
		return getData().get(getData().position() + 8 + FONTID.SIZE);
	}

	public byte getSpare() {
		return getData().get(getData().position() + 9 + FONTID.SIZE);
	}

	public COLOR_VALUE getBackgroundColor() {
		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 10 + FONTID.SIZE);
		data.limit(data.position() + COLOR_VALUE.SIZE);
		return new COLOR_VALUE(data);
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Left=" + getLeft() + ", Top=" + getTop() + ", Width=" + getWidth() + ", Height="
				+ getHeight() + ", FontID=" + getFontId() + ", BackColor=" + getBackColor() + ", BackgroundColor=" + getBackgroundColor()
				+ "]";
	}
}
