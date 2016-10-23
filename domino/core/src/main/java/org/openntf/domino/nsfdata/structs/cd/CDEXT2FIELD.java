package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.NSFCompiledFormula;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * New field attributes have been added in Release 5.0 of Domino. To preserve compatibility with existing applications, the new attributes
 * have been placed in this second extension to the CDFIELD record. This record will be present in the $Body item of the form note for each
 * field defined. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDEXT2FIELD extends CDRecord {
	public final WSIG Header = inner(new WSIG());
	// TODO make enum
	public final Unsigned8 NumSysPref = new Unsigned8();
	// TODO make enum
	public final Unsigned8 NumSysFlags = new Unsigned8();
	public final Unsigned32 DecimalSymLength = new Unsigned32();
	public final Unsigned32 MilliSepSymLength = new Unsigned32();
	public final Unsigned32 NegativeSymLength = new Unsigned32();
	public final Unsigned16 MilliGroupSize = new Unsigned16();
	public final Signed16 VerticalSpacing = new Signed16();
	public final Signed16 HorizontalSpacing = new Signed16();
	public final Unsigned16 Unused2 = new Unsigned16();
	// TODO make enum
	public final Unsigned16 FirstFieldLimitType = new Unsigned16();
	// TODO make enum
	public final Unsigned8 CurrencyPref = new Unsigned8();
	// TODO make enum
	public final Unsigned8 CurrencyType = new Unsigned8();
	// TODO make enum
	public final Unsigned8 CurrencyFlags = new Unsigned8();
	public final Unsigned32 CurrencySymLength = new Unsigned32();
	public final Unsigned32 ISOCountry = new Unsigned32();
	public final Unsigned16 ThumbnailImageWidth = new Unsigned16();
	public final Unsigned16 ThumbnailImageHeight = new Unsigned16();
	public final Unsigned16 wThumbnailImageFileName = new Unsigned16();
	public final Unsigned16 wIMOnlineNameFormulaLen = new Unsigned16();
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
	// TODO make/reuse enum
	public final Unsigned8 DTTZone = new Unsigned8();
	public final Unsigned32 Unused5 = new Unsigned32();
	// TODO make enum
	public final Unsigned8 ECFlags = new Unsigned8();
	public final Unsigned8 Unused612 = new Unsigned8();
	public final Unsigned16 wCharacters = new Unsigned16();
	public final Unsigned16 wInputEnabledLen = new Unsigned16();
	public final Unsigned16 wIMGroupFormulaLen = new Unsigned16();

	static {
		addVariableAsciiString("DecimalSymbol", "DecimalSymLength");
		addVariableAsciiString("MilliSepSymbol", "MilliSepSymLength");
		addVariableAsciiString("NegativeSymbol", "NegativeSymLength");
		addVariableAsciiString("CurrencySymbol", "CurrencySymLength");
		addVariableAsciiString("ThumbnailImageFileName", "wThumbnailImageFileName");
		addVariableData("IMOnlineNameFormula", "wIMOnlineNameFormulaLen");
		addVariableAsciiString("DTDsep1", "DTDsep1Len");
		addVariableAsciiString("DTDsep2", "DTDsep2Len");
		addVariableAsciiString("DTDsep3", "DTDsep3Len");
		addVariableAsciiString("DTTsep", "DTTsepLen");
		addVariableData("InputEnabled", "wInputEnabledLen");
		addVariableData("IMGroupFormula", "wIMGroupFormulaLen");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	public String getDecimalSymbol() {
		return (String) getVariableElement("DecimalSymbol");
	}

	public String getMilliSepSymbol() {
		return (String) getVariableElement("MilliSepSymbol");
	}

	public String getNegativeSymbol() {
		return (String) getVariableElement("NegativeSymbol");
	}

	public String getCurrencySymbol() {
		return (String) getVariableElement("CurrencySymbol");
	}

	public String getThumbnailImageFileName() {
		return (String) getVariableElement("ThumbnailImageFileName");
	}

	public NSFCompiledFormula getIMOnlineNameFormula() {
		return new NSFCompiledFormula((byte[]) getVariableElement("IMOnlineNameFormula"));
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

	public NSFCompiledFormula getInputEnabledFormula() {
		return new NSFCompiledFormula((byte[]) getVariableElement("InputEnabled"));
	}

	public NSFCompiledFormula getIMGroupFormula() {
		return new NSFCompiledFormula((byte[]) getVariableElement("IMGroupFormula"));
	}
}
