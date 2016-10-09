package org.openntf.domino.nsfdata.structs;

/**
 * This structure holds the format for character text number strings. You set up this structure based on the number format you want to use.
 * Definitions for the various fields of this structure are found in NFMT_xxx and NATTR_xxx. (misc.h)
 *
 */
public class NFMT extends AbstractStruct {
	public static enum NumFormat {
		GENERAL, FIXED, SCIENTIFIC, CURRENCY, BYTES
	}

	/**
	 * Number of decimal digits
	 */
	public final Unsigned8 Digits = new Unsigned8();
	/**
	 * Display Format
	 */
	// TODO Figure out why this doesn't work as an enum (Nifty 50 FORMROUT.NSF contains a value 58 here)
	//	public final Enum8<NumFormat> Format = new Enum8<NumFormat>(NumFormat.values());
	public final Unsigned8 Format = new Unsigned8();
	/**
	 * Display Attributes
	 */
	public final Unsigned8 Attributes = new Unsigned8();
	public final Unsigned8 Unused = new Unsigned8();

	public short getAttributes() {
		// TODO make enum
		return Attributes.get();
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ", Digits: " + Digits.get() + ", Format: " + Format.get() + ", Attributes: "
				+ getAttributes() + "]";
	}
}
