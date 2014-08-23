package org.openntf.domino.nsfdata.structs;

import java.awt.Color;
import java.nio.ByteBuffer;

/**
 * This data structure defines the three components of an RGB color which consist of a red, green, and blue color value. (colorods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class COLOR_VALUE extends AbstractStruct {
	public static final int SIZE = 6;

	static {
		addFixed("Flags", Short.class);
		addFixedUnsigned("Component1", Byte.class);
		addFixedUnsigned("Component2", Byte.class);
		addFixedUnsigned("Component3", Byte.class);
		addFixed("Component4", Byte.class);
	}

	public COLOR_VALUE(final ByteBuffer data) {
		super(data);
	}

	public short getFlags() {
		// TODO make enum
		return (Short) getStructElement("Flags");
	}

	public short getRed() {
		return (Short) getStructElement("Component1");
	}

	public short getBlue() {
		return (Short) getStructElement("Component2");
	}

	public short getGreen() {
		return (Short) getStructElement("Component3");
	}

	public Color getColor() {
		return new Color(getRed(), getBlue(), getGreen());
	}

	@Override
	public int getStructSize() {
		return SIZE;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ", Red=" + getRed() + ", Blue=" + getBlue() + ", Green=" + getGreen() + "]";
	}
}
