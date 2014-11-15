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

	public final Unsigned16 Flags = new Unsigned16();
	public final Unsigned8 Component1 = new Unsigned8();
	public final Unsigned8 Component2 = new Unsigned8();
	public final Unsigned8 Component3 = new Unsigned8();

	public COLOR_VALUE() {
		super();
	}

	public COLOR_VALUE(final ByteBuffer data) {
		super(data);
	}

	public int getFlags() {
		// TODO make enum
		return Flags.get();
	}

	public short getRed() {
		return Component1.get();
	}

	public void setRed(final short red) {
		Component1.set(red);
	}

	public short getBlue() {
		return Component3.get();
	}

	public void setBlue(final short blue) {
		Component3.set(blue);
	}

	public short getGreen() {
		return Component2.get();
	}

	public void setGreen(final short green) {
		Component2.set(green);
	}

	public Color getColor() {
		return new Color(getRed(), getGreen(), getBlue());
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ", Red=" + getRed() + ", Green=" + getGreen() + ", Blue=" + getBlue() + "]";
	}
}
