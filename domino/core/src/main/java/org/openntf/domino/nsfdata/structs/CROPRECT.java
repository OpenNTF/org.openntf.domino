package org.openntf.domino.nsfdata.structs;


/**
 * Specifies a cropping rectangle for display of graphical data. (editods.h)
 *
 */
public class CROPRECT extends AbstractStruct {

	public final Unsigned16 left = new Unsigned16();
	public final Unsigned16 top = new Unsigned16();
	public final Unsigned16 right = new Unsigned16();
	public final Unsigned16 bottom = new Unsigned16();

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Left=" + left.get() + ", Top=" + top.get() + ", Right=" + right.get() + ", Bottom="
				+ bottom.get() + "]";
	}
}
