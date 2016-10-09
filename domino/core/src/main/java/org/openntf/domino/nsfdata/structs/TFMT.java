package org.openntf.domino.nsfdata.structs;

/**
 * This structure holds the format for character time/date strings. You set up this structure based on the time/date format you want to use.
 * Definitions for the various fields of this structure are found in TDFMT_xxx, TTFMT_xxx, TZFMT_xxx, and TSFMT_xxx. (misc.h)
 *
 */
public class TFMT extends AbstractStruct {
	public static enum TDFMT {
		FULL, CPARTIAL, PARTIAL, DPARTIAL, FULL4, CPARTIAL4, DPARTIAL4
	}

	public static enum TTFMT {
		FULL, PARTIAL, HOUR
	}

	public static enum TZFMT {
		NEVER, SOMETIMES, ALWAYS
	}

	public static enum TSFMT {
		DATE, TIME, DATETIME, CDATETIME
	}

	// TODO Figure out why this sometimes fails (Nifty 50 FORMROUT.NSF contains a value 123)
	public final Unsigned8 Date = new Unsigned8();
	//	public final Enum8<TDFMT> Date = new Enum8<TDFMT>(TDFMT.values());
	// TODO FORMROUT.NSF contains 64
	public final Unsigned8 Time = new Unsigned8();
	//	public final Enum8<TTFMT> Time = new Enum8<TTFMT>(TTFMT.values());
	// TODO FORMROUT.NSF contains 105
	public final Unsigned8 Zone = new Unsigned8();
	//	public final Enum8<TZFMT> Zone = new Enum8<TZFMT>(TZFMT.values());
	// TODO FORMROUT.NSF contains 102
	public final Unsigned8 Structure = new Unsigned8();
	//	public final Enum8<TSFMT> Structure = new Enum8<TSFMT>(TSFMT.values());
}
