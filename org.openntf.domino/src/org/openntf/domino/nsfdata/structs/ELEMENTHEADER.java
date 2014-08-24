package org.openntf.domino.nsfdata.structs;

import java.nio.ByteBuffer;

/**
 * This structure contains the common fields for the graphical elements in a layout region of a form.
 * 
 * @since Lotus Notes 4.1
 *
 */
public class ELEMENTHEADER extends AbstractStruct {
	public static final int SIZE = 10 + FONTID.SIZE + COLOR_VALUE.SIZE;

	static {
		addFixedUnsigned("wLeft", Short.class);
		addFixedUnsigned("wTop", Short.class);
		addFixedUnsigned("wWidth", Short.class);
		addFixedUnsigned("wHeight", Short.class);
		addFixed("FontID", FONTID.class);
		addFixed("byBackColor", Byte.class);
		addFixed("bSpare", Byte.class);
		addFixed("BackgroundColor", COLOR_VALUE.class);
	}

	public ELEMENTHEADER(final ByteBuffer data) {
		super(data);
	}

	@Override
	public long getStructSize() {
		return SIZE;
	}

	/**
	 * @return Location of the left edge of the element in twips
	 */
	public int getLeft() {
		return (Integer) getStructElement("wLeft");
	}

	/**
	 * @return Location of the top of the element in twips
	 */
	public int getTop() {
		return (Integer) getStructElement("wTop");
	}

	/**
	 * @return Width of the element in twips
	 */
	public int getWidth() {
		return (Integer) getStructElement("wWidth");
	}

	/**
	 * @return Height of the element in twips
	 */
	public int getHeight() {
		return (Integer) getStructElement("wHeight");
	}

	/**
	 * @return Font used to display text in the element
	 */
	public FONTID getFontId() {
		return (FONTID) getStructElement("FontID");
	}

	/**
	 * @return Background color for the element
	 */
	public byte getBackColor() {
		return (Byte) getStructElement("byBackColor");
	}

	public byte getSpare() {
		return (Byte) getStructElement("bSpare");
	}

	public COLOR_VALUE getBackgroundColor() {
		return (COLOR_VALUE) getStructElement("BackgroundColor");
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Left=" + getLeft() + ", Top=" + getTop() + ", Width=" + getWidth() + ", Height="
				+ getHeight() + ", FontID=" + getFontId() + ", BackColor=" + getBackColor() + ", BackgroundColor=" + getBackgroundColor()
				+ "]";
	}
}
