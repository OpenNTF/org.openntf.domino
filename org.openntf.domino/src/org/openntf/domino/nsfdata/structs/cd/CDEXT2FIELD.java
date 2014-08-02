package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * New field attributes have been added in Release 5.0 of Domino. To preserve compatibility with existing applications, the new attributes
 * have been placed in this second extension to the CDFIELD record. This record will be present in the $Body item of the form note for each
 * field defined. (editods.h)
 * 
 * @author jgallagher
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDEXT2FIELD extends CDRecord {

	public CDEXT2FIELD(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return NPREF_xxx
	 */
	public byte getNumSymPref() {
		// TODO create enum
		return getData().get(getData().position() + 0);
	}

	/**
	 * @return NNUMSYM_xxx
	 */
	public byte getNumSymFlags() {
		// TODO create enum
		return getData().get(getData().position() + 1);
	}

	public int getDecimalSymLength() {
		return getData().getInt(getData().position() + 2);
	}

	public int getMilliSepSymLength() {
		return getData().getInt(getData().position() + 6);
	}

	public int getNegativeSymLength() {
		return getData().getInt(getData().position() + 10);
	}

	public short getMilliGroupSize() {
		return getData().getShort(getData().position() + 14);
	}

	/**
	 * @return Extra vertical spacing (%)
	 */
	public short getVerticalSpacing() {
		return getData().getShort(getData().position() + 16);
	}

	/**
	 * @return Extra horizontal spacing (%)
	 */
	public short getHorizontalSpacing() {
		return getData().getShort(getData().position() + 18);
	}

	public short getUnused2() {
		return getData().getShort(getData().position() + 20);
	}

	public short getFirstFieldLimitType() {
		return getData().getShort(getData().position() + 22);
	}

	/**
	 * @return NPREF_xxx
	 */
	public byte getCurrencyPref() {
		// TODO create enum
		return getData().get(getData().position() + 24);
	}

	/**
	 * @return NCURFMT_xxx
	 */
	public byte getCurrencyType() {
		// TODO create enum
		return getData().get(getData().position() + 25);
	}

	/**
	 * @return NCURFMT_xxx
	 */
	public byte getCurrencyFlags() {
		// TODO create enum
		return getData().get(getData().position() + 26);
	}

	public int getCurrencySymLength() {
		return getData().getInt(getData().position() + 27);
	}

	public int getISOCountry() {
		return getData().getInt(getData().position() + 31);
	}

	/**
	 * @since IBM Lotus Notes/Domino 8.0
	 */
	public short getThumbnailImageWidth() {
		return getData().getShort(getData().position() + 35);
	}

	/**
	 * @since IBM Lotus Notes/Domino 8.0
	 */
	public short getThumbnailImageHeight() {
		return getData().getShort(getData().position() + 37);
	}

	/**
	 * @since IBM Lotus Notes/Domino 8.0
	 */
	public short getThumbnailImageFileNameLength() {
		return getData().getShort(getData().position() + 39);
	}

	public short getIMOnlineNameFormulaLength() {
		return getData().getShort(getData().position() + 41);
	}

	/**
	 * @return NPREF_xxx
	 */
	public byte getDTPref() {
		// TODO create enum, shared with above
		return getData().get(getData().position() + 43);
	}

	/**
	 * @return DT_xxx
	 */
	public int getDTFlags() {
		// TODO create enum
		return getData().getInt(getData().position() + 44);
	}

	/**
	 * @return DT_xxx
	 */
	public int getDTFlags2() {
		// TODO create enum, shared with above
		return getData().getInt(getData().position() + 44);
	}

	/**
	 * @return DT_WFMT_xxx
	 */
	public byte getDTDOWFmt() {
		// TODO create enum
		return getData().get(getData().position() + 48);
	}

	/**
	 * @return DT_YFMT_xxx
	 */
	public byte getDTYearFmt() {
		// TODO create enum
		return getData().get(getData().position() + 49);
	}

	/**
	 * @return DT_MFMT_xxx
	 */
	public byte getDTMonthFmt() {
		// TODO create enum
		return getData().get(getData().position() + 50);
	}

	/**
	 * @return DT_DFMT_xxx
	 */
	public byte getDTDayFmt() {
		// TODO create enum
		return getData().get(getData().position() + 51);
	}

	public byte getDTDSep1Len() {
		return getData().get(getData().position() + 52);
	}

	public byte getDTDSep2Len() {
		return getData().get(getData().position() + 53);
	}

	public byte getDTDSep3Len() {
		return getData().get(getData().position() + 54);
	}

	public byte getDTTSepLen() {
		return getData().get(getData().position() + 55);
	}

	/**
	 * @return DT_DSHOW_xxx
	 */
	public byte getDTDShow() {
		// TODO create enum
		return getData().get(getData().position() + 56);
	}

	/**
	 * @return DT_DSPEC_xxx
	 */
	public byte getDTDSpecial() {
		// TODO create enum
		return getData().get(getData().position() + 57);
	}

	/**
	 * @return DT_TSHOW_xxx
	 */
	public byte getDTTShow() {
		// TODO create enum
		return getData().get(getData().position() + 58);
	}

	/**
	 * @return TZFMT_xxx
	 */
	public byte getDTTZone() {
		// TODO create enum
		return getData().get(getData().position() + 59);
	}

	public int getUnused5() {
		return getData().getInt(getData().position() + 60);
	}

	public byte getECFlags() {
		return getData().get(getData().position() + 64);
	}

	public byte getUnused612() {
		return getData().get(getData().position() + 65);
	}

	/**
	 * @return Number of characters if proportional width
	 */
	public short getCharacters() {
		return getData().getShort(getData().position() + 66);
	}

	public short getInputEnabledLen() {
		return getData().getShort(getData().position() + 68);
	}

	public short getIMGroupFormulaLen() {
		return getData().getShort(getData().position() + 70);
	}

	public String getDecimalSymbol() {
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 72);
		data.limit(data.position() + getDecimalSymLength());
		return ODSUtils.fromLMBCS(data);
	}

	public String getMilliSepSymbol() {
		int preceding = getDecimalSymLength();

		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 72 + preceding);
		data.limit(data.position() + getMilliSepSymLength());
		return ODSUtils.fromLMBCS(data);
	}

	public String getNegativeSymbol() {
		int preceding = getDecimalSymLength() + getMilliSepSymLength();

		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 72 + preceding);
		data.limit(data.position() + getNegativeSymLength());
		return ODSUtils.fromLMBCS(data);
	}

	public String getCurrencySymbol() {
		int preceding = getDecimalSymLength() + getMilliSepSymLength() + getNegativeSymLength();

		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 72 + preceding);
		data.limit(data.position() + getCurrencySymLength());
		return ODSUtils.fromLMBCS(data);
	}

	public String getThumbnailImageFileName() {
		int preceding = getDecimalSymLength() + getMilliSepSymLength() + getNegativeSymLength() + getCurrencySymLength();

		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 72 + preceding);
		data.limit(data.position() + getThumbnailImageFileNameLength());
		return ODSUtils.fromLMBCS(data);
	}

	public String getIMOnlineNameFormula() {
		int preceding = getDecimalSymLength() + getMilliSepSymLength() + getNegativeSymLength() + getCurrencySymLength()
				+ getThumbnailImageFileNameLength();

		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 72 + preceding);
		data.limit(data.position() + getThumbnailImageFileNameLength());
		return ODSUtils.fromLMBCS(data);
	}

	public String getDTDSep1() {
		int preceding = getDecimalSymLength() + getMilliSepSymLength() + getNegativeSymLength() + getCurrencySymLength()
				+ getThumbnailImageFileNameLength() + getIMOnlineNameFormulaLength();

		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 72 + preceding);
		data.limit(data.position() + getDTDSep1Len());
		return ODSUtils.fromLMBCS(data);
	}

	public String getDTDSep2() {
		int preceding = getDecimalSymLength() + getMilliSepSymLength() + getNegativeSymLength() + getCurrencySymLength()
				+ getThumbnailImageFileNameLength() + getIMOnlineNameFormulaLength() + getDTDSep1Len();

		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 72 + preceding);
		data.limit(data.position() + getDTDSep2Len());
		return ODSUtils.fromLMBCS(data);
	}

	public String getDTDSep3() {
		int preceding = getDecimalSymLength() + getMilliSepSymLength() + getNegativeSymLength() + getCurrencySymLength()
				+ getThumbnailImageFileNameLength() + getIMOnlineNameFormulaLength() + getDTDSep1Len() + getDTDSep2Len();

		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 72 + preceding);
		data.limit(data.position() + getDTDSep3Len());
		return ODSUtils.fromLMBCS(data);
	}

	public String getDTTSep() {
		int preceding = getDecimalSymLength() + getMilliSepSymLength() + getNegativeSymLength() + getCurrencySymLength()
				+ getThumbnailImageFileNameLength() + getIMOnlineNameFormulaLength() + getDTDSep1Len() + getDTDSep2Len() + getDTDSep3Len();

		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 72 + preceding);
		data.limit(data.position() + getDTTSepLen());
		return ODSUtils.fromLMBCS(data);
	}

	public String getInputEnabledFormula() {
		int preceding = getDecimalSymLength() + getMilliSepSymLength() + getNegativeSymLength() + getCurrencySymLength()
				+ getThumbnailImageFileNameLength() + getIMOnlineNameFormulaLength() + getDTDSep1Len() + getDTDSep2Len() + getDTDSep3Len()
				+ getDTTSepLen();

		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 72 + preceding);
		data.limit(data.position() + getInputEnabledLen());
		return ODSUtils.fromLMBCS(data);
	}

	public String getIMGroupFormula() {
		int preceding = getDecimalSymLength() + getMilliSepSymLength() + getNegativeSymLength() + getCurrencySymLength()
				+ getThumbnailImageFileNameLength() + getIMOnlineNameFormulaLength() + getDTDSep1Len() + getDTDSep2Len() + getDTDSep3Len()
				+ getDTTSepLen() + getInputEnabledLen();

		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 72 + preceding);
		data.limit(data.position() + getIMGroupFormulaLen());
		return ODSUtils.fromLMBCS(data);
	}
}
