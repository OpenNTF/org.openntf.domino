package org.openntf.domino.nsfdata.structs;

/**
 * @since Lotus Notes/Domino 6.0
 *
 */
public class VIEW_COLUMN_FORMAT3 extends AbstractStruct {
	public final Unsigned16 Signature = new Unsigned16();
	// TODO make enum
	public final Unsigned8 DTPref = new Unsigned8();
	// TODO make enum
	public final Unsigned32 DTFlags = new Unsigned32();
	// TODO make enum
	public final Unsigned32 DTFlags2 = new Unsigned32();
	// TODO make enum
	public final Unsigned8 DTDOWFmt = new Unsigned8();
	// TODO make enum
	public final Unsigned8 DTYearFmt = new Unsigned8();
	// TODO make enum
	public final Unsigned8 DTMonthFmt = new Unsigned8();
	// TODO make enum
	public final Unsigned8 DTDayFmt = new Unsigned8();
	public final Unsigned8 DTDsep1Len = new Unsigned8();
	public final Unsigned8 DTDsep2Len = new Unsigned8();
	public final Unsigned8 DTDsep3Len = new Unsigned8();
	public final Unsigned8 DTTsepLen = new Unsigned8();
	// TODO make enum
	public final Unsigned8 DTDShow = new Unsigned8();
	// TODO make enum
	public final Unsigned8 DTDSpecial = new Unsigned8();
	// TODO make enum
	public final Unsigned8 DTTShow = new Unsigned8();
	// TODO make enum
	public final Unsigned8 DTTZone = new Unsigned8();
	// TODO make enum?
	public final Unsigned16 DatePreference = new Unsigned16();
	public final Unsigned8 bUnused = new Unsigned8();
	public final Unsigned32 Unused = new Unsigned32();

	static {
		addVariableAsciiString("DTDsep1", "DTDsep1Len");
		addVariableAsciiString("DTDsep2", "DTDsep2Len");
		addVariableAsciiString("DTDsep3", "DTDsep3Len");
		addVariableAsciiString("DTTsep", "DTTsepLen");
	}

	public String getDTDSep1() {
		return (String) getVariableElement("DTDsep1");
	}

	public String getDTDSep2() {
		return (String) getVariableElement("DTDsep2");
	}

	public String getDTDSep3() {
		return (String) getVariableElement("DTDsep3");
	}

	public String getDTTSep() {
		return (String) getVariableElement("DTTsep");
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Signature=" + Signature.get() + ", DTPref=" + DTPref.get() + ", DTFlags="
				+ DTFlags.get() + ", DTFlags2=" + DTFlags2.get() + ", DTDOWFmt=" + DTDOWFmt.get() + ", DTYearFmt=" + DTYearFmt.get()
				+ ", DTMonthFmt=" + DTMonthFmt.get() + ", DTDayFmt=" + DTDayFmt.get() + ", DTDsep1=" + getDTDSep1() + ", DTDSep2="
				+ getDTDSep2() + ", DTDSep3=" + getDTDSep3() + ", DTTSep=" + getDTTSep() + ", DTDShow=" + DTDShow.get() + ", DTDSpecial="
				+ DTDSpecial.get() + ", DTTShow=" + DTTShow.get() + ", DTTZone=" + DTTZone.get() + ", DatePreference="
				+ DatePreference.get() + "]";
	}
}
