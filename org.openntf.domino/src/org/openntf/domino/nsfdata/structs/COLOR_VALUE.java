package org.openntf.domino.nsfdata.structs;

import java.awt.Color;
import java.nio.ByteBuffer;

/**
 * This data structure defines the three components of an RGB color which consist of a red, green, and blue color value. (colorods.h)
 * 
 * @author jgallagher
 * @since Lotus Notes/Domino 5.0
 *
 */
public class COLOR_VALUE extends AbstractStruct {

	public COLOR_VALUE(final ByteBuffer data) {
		super(data);
	}

	public int getRed() {
		return getData().get(getData().position() + 0) & 0xFF;
	}

	public int getBlue() {
		return getData().get(getData().position() + 1) & 0xFF;
	}

	public int getGreen() {
		return getData().get(getData().position() + 2) & 0xFF;
	}

	public Color getColor() {
		return new Color(getRed(), getBlue(), getGreen());
	}

	@Override
	public int getStructSize() {
		return 4;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ", Red=" + getRed() + ", Blue=" + getBlue() + ", Green=" + getGreen() + "]";
	}
}
