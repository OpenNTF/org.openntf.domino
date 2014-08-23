package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.NSFCompiledFormula;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * New field attributes have been added in Release 5.0 of Domino. To preserve compatibility with existing applications, the new attributes
 * have been placed in this second extension to the CDFIELD record. This record will be present in the $Body item of the form note for each
 * field defined. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDEXT2FIELD extends CDRecord {

	static {
		addFixed("NumSymPref", Byte.class);
		addFixed("NumSymFlags", Byte.class);
		addFixedUnsigned("DecimalSymLength", Integer.class);
		addFixedUnsigned("MilliSepSymLength", Integer.class);
		addFixedUnsigned("NegativeSymLength", Integer.class);
		addFixedUnsigned("MilliGroupSize", Short.class);
		addFixedUnsigned("VerticalSpacing", Short.class);
		addFixedUnsigned("HorizontalSpacing", Short.class);
		addFixed("Unused2", Short.class);
		addFixed("FirstFieldLimitType", Short.class);
		addFixed("CurrencyPref", Byte.class);
		addFixed("CurrencyType", Byte.class);
		addFixed("CurrencyFlags", Byte.class);
		addFixedUnsigned("CurrencySymLength", Integer.class);
		addFixed("ISOCountry", Integer.class);
		addFixedUnsigned("ThumbnailImageWidth", Short.class);
		addFixedUnsigned("ThumbnailImageHeight", Short.class);
		addFixedUnsigned("wThumbnailImageFileNameLength", Short.class);
		addFixedUnsigned("wIMOnlineNameFormulaLen", Short.class);
		addFixed("DTPref", Byte.class);
		addFixed("DTFlags", Integer.class);
		addFixed("DTFlags2", Integer.class);
		addFixed("DTDOWFmt", Byte.class);
		addFixed("DTYearFmt", Byte.class);
		addFixed("DTMonthFmt", Byte.class);
		addFixed("DTDayFmt", Byte.class);
		addFixedUnsigned("DTDsep1Len", Byte.class);
		addFixedUnsigned("DTDsep2Len", Byte.class);
		addFixedUnsigned("DTDsep3Len", Byte.class);
		addFixedUnsigned("DTTsepLen", Byte.class);
		addFixed("DTDShow", Byte.class);
		addFixed("DTDSpecial", Byte.class);
		addFixed("DTTShow", Byte.class);
		addFixed("DTTZone", Byte.class);
		addFixed("Unused5", Integer.class);
		addFixed("ECFlags", Byte.class);
		addFixed("Unused612", Byte.class);
		addFixedUnsigned("wCharacters", Short.class);
		addFixedUnsigned("wInputEnabledLen", Short.class);
		addFixedUnsigned("wIMGroupFormulaLen", Short.class);

		addVariableString("DecimalSymbol", "getDecimalSymLength");
		addVariableString("MilliSepSymbol", "getMilliSepSymLength");
		addVariableString("NegativeSymbol", "getNegativeSymLength");
		addVariableString("CurrencySymbol", "getCurrencySymLength");
		addVariableString("ThumbnailImageFileName", "getThumbnailImageFileNameLength");
		addVariableData("IMOnlineNameFormula", "getIMOnlineNameFormulaLength");
		addVariableString("DTDsep1", "getDTDSep1Len");
		addVariableString("DTDsep2", "getDTDSep2Len");
		addVariableString("DTDsep3", "getDTDSep3Len");
		addVariableString("DTTsep", "getDTTSepLen");
		addVariableData("InputEnabled", "getInputEnabledLen");
		addVariableData("IMGroupFormula", "getIMGroupFormulaLen");
	}

	public CDEXT2FIELD(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return NPREF_xxx
	 */
	public byte getNumSymPref() {
		// TODO create enum
		return (Byte) getStructElement("NumSymPref");
	}

	/**
	 * @return NNUMSYM_xxx
	 */
	public byte getNumSymFlags() {
		// TODO create enum
		return (Byte) getStructElement("NumSymFlags");
	}

	public int getDecimalSymLength() {
		return ((Long) getStructElement("DecimalSymLength")).intValue();
	}

	public int getMilliSepSymLength() {
		return ((Long) getStructElement("MilliSepSymLength")).intValue();
	}

	public int getNegativeSymLength() {
		return ((Long) getStructElement("NegativeSymLength")).intValue();
	}

	public int getMilliGroupSize() {
		return (Integer) getStructElement("MilliGroupSize");
	}

	/**
	 * @return Extra vertical spacing (%)
	 */
	public int getVerticalSpacing() {
		return (Integer) getStructElement("VerticalSpacing");
	}

	/**
	 * @return Extra horizontal spacing (%)
	 */
	public int getHorizontalSpacing() {
		return (Integer) getStructElement("HorizontalSpacing");
	}

	public short getUnused2() {
		return (Short) getStructElement("Unused2");
	}

	public short getFirstFieldLimitType() {
		// TODO make enum
		return (Short) getStructElement("FirstFieldLimitType");
	}

	/**
	 * @return NPREF_xxx
	 */
	public byte getCurrencyPref() {
		// TODO create enum
		return (Byte) getStructElement("CurrencyPref");
	}

	/**
	 * @return NCURFMT_xxx
	 */
	public byte getCurrencyType() {
		// TODO create enum
		return (Byte) getStructElement("CurrencyType");
	}

	/**
	 * @return NCURFMT_xxx
	 */
	public byte getCurrencyFlags() {
		// TODO create enum
		return (Byte) getStructElement("CurrencyFlags");
	}

	public int getCurrencySymLength() {
		return ((Long) getStructElement("CurrencySymLength")).intValue();
	}

	public int getISOCountry() {
		return (Integer) getStructElement("ISOCountry");
	}

	/**
	 * @since IBM Lotus Notes/Domino 8.0
	 */
	public int getThumbnailImageWidth() {
		return (Integer) getStructElement("ThumbnailImageWidth");
	}

	/**
	 * @since IBM Lotus Notes/Domino 8.0
	 */
	public int getThumbnailImageHeight() {
		return (Integer) getStructElement("ThumbnailImageHeight");
	}

	/**
	 * @since IBM Lotus Notes/Domino 8.0
	 */
	public int getThumbnailImageFileNameLength() {
		return (Integer) getStructElement("wThumbnailImageFileNameLength");
	}

	public int getIMOnlineNameFormulaLength() {
		return (Integer) getStructElement("wIMOnlineNameFormulaLen");
	}

	/**
	 * @return NPREF_xxx
	 */
	public byte getDTPref() {
		// TODO create enum, shared with above
		return (Byte) getStructElement("DTPref");
	}

	/**
	 * @return DT_xxx
	 */
	public int getDTFlags() {
		// TODO create enum
		return (Integer) getStructElement("DTFlags");
	}

	/**
	 * @return DT_xxx
	 */
	public int getDTFlags2() {
		// TODO create enum, shared with above
		return (Integer) getStructElement("DTFlags2");
	}

	/**
	 * @return DT_WFMT_xxx
	 */
	public byte getDTDOWFmt() {
		// TODO create enum
		return (Byte) getStructElement("DTDOWFmt");
	}

	/**
	 * @return DT_YFMT_xxx
	 */
	public byte getDTYearFmt() {
		// TODO create enum
		return (Byte) getStructElement("DTYearFmt");
	}

	/**
	 * @return DT_MFMT_xxx
	 */
	public byte getDTMonthFmt() {
		// TODO create enum
		return (Byte) getStructElement("DTMonthFmt");
	}

	/**
	 * @return DT_DFMT_xxx
	 */
	public byte getDTDayFmt() {
		// TODO create enum
		return (Byte) getStructElement("DTDayFmt");
	}

	public int getDTDSep1Len() {
		return (Short) getStructElement("DTDsep1Len");
	}

	public int getDTDSep2Len() {
		return (Short) getStructElement("DTDsep2Len");
	}

	public int getDTDSep3Len() {
		return (Short) getStructElement("DTDsep3Len");
	}

	public int getDTTSepLen() {
		return (Short) getStructElement("DTTsepLen");
	}

	/**
	 * @return DT_DSHOW_xxx
	 */
	public byte getDTDShow() {
		// TODO create enum
		return (Byte) getStructElement("DTDShow");
	}

	/**
	 * @return DT_DSPEC_xxx
	 */
	public byte getDTDSpecial() {
		// TODO create enum
		return (Byte) getStructElement("DTDSpecial");
	}

	/**
	 * @return DT_TSHOW_xxx
	 */
	public byte getDTTShow() {
		// TODO create enum
		return (Byte) getStructElement("DTTShow");
	}

	/**
	 * @return TZFMT_xxx
	 */
	public byte getDTTZone() {
		// TODO create enum
		return (Byte) getStructElement("DTTZone");
	}

	public int getUnused5() {
		return (Integer) getStructElement("Unused5");
	}

	public byte getECFlags() {
		return (Byte) getStructElement("ECFlags");
	}

	public byte getUnused612() {
		return (Byte) getStructElement("Unused612");
	}

	/**
	 * @return Number of characters if proportional width
	 */
	public int getCharacters() {
		return (Integer) getStructElement("wCharacters");
	}

	public int getInputEnabledLen() {
		return (Integer) getStructElement("wInputEnabledLen");
	}

	public int getIMGroupFormulaLen() {
		return (Integer) getStructElement("wIMGroupFormulaLen");
	}

	public String getDecimalSymbol() {
		return (String) getStructElement("DecimalSymbol");
	}

	public String getMilliSepSymbol() {
		return (String) getStructElement("MilliSepSymbol");
	}

	public String getNegativeSymbol() {
		return (String) getStructElement("NegativeSymbol");
	}

	public String getCurrencySymbol() {
		return (String) getStructElement("CurrencySymbol");
	}

	public String getThumbnailImageFileName() {
		return (String) getStructElement("ThumbnailImageFileName");
	}

	public NSFCompiledFormula getIMOnlineNameFormula() {
		return new NSFCompiledFormula((byte[]) getStructElement("IMOnlineNameFormula"));
	}

	public String getDTDSep1() {
		return (String) getStructElement("DTDsep1");
	}

	public String getDTDSep2() {
		return (String) getStructElement("DTDsep2");
	}

	public String getDTDSep3() {
		return (String) getStructElement("DTDsep3");
	}

	public String getDTTSep() {
		return (String) getStructElement("DTTsep");
	}

	public NSFCompiledFormula getInputEnabledFormula() {
		return new NSFCompiledFormula((byte[]) getStructElement("InputEnabled"));
	}

	public NSFCompiledFormula getIMGroupFormula() {
		return new NSFCompiledFormula((byte[]) getStructElement("IMGroupFormula"));
	}
}
