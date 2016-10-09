package org.openntf.domino.nsfdata.structs;

/**
 * One structure for each row or column. (fsods.h)
 * 
 * @since Lotus Notes/Domino 5.0.1
 *
 */
public class FRAMESETLENGTH extends AbstractStruct {
	public static enum LengthType {
		UNUSED0, PIXELS, PERCENTAGE, RELATIVE;
	}

	public final Enum16<LengthType> Type = new Enum16<LengthType>(LengthType.values());
	public final Unsigned16 Value = new Unsigned16();

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Type=" + Type.get() + ", Value=" + Value.get() + "]";
	}
}
