package org.openntf.domino.nsfdata.structs;

import java.nio.ByteBuffer;

/**
 * One structure for each row or column. (fsods.h)
 * 
 * @since Lotus Notes/Domino 5.0.1
 *
 */
public class FRAMESETLENGTH extends AbstractStruct {
	public static enum LengthType {
		UNDEFINED, PIXELS, PERCENTAGE, RELATIVE;
	}

	public final Enum16<LengthType> Type = new Enum16<LengthType>(LengthType.values());
	public final Unsigned16 Value = new Unsigned16();

	public FRAMESETLENGTH() {
		super();
	}

	public FRAMESETLENGTH(final ByteBuffer data) {
		super(data);
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Type=" + Type.get() + ", Value=" + Value.get() + "]";
	}
}
