package org.openntf.domino.nsfdata.structs;


/**
 * This record contains information for storing a length to disk. (editods.h)
 *
 */
public class LENGTH_VALUE extends AbstractStruct {

	public static enum CDLENGTH_UNITS {
		UNUSED0, TWIPS, PIXELS, PERCENT, EMS, EXS, CHARS
	}

	public final Unsigned16 Flags = new Unsigned16();
	public final Float64 Length = new Float64();
	public final Enum8<CDLENGTH_UNITS> Units = new Enum8<CDLENGTH_UNITS>(CDLENGTH_UNITS.values());
	public final Unsigned8 Reserved = new Unsigned8();

	public int getFlags() {
		// TODO make enum
		return Flags.get();
	}
}
