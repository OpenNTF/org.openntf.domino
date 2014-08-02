package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openntf.domino.nsfdata.structs.LIST;
import org.openntf.domino.nsfdata.structs.NFMT;
import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.TFMT;

/**
 * This defines the structure of a CDFIELD record in the $Body item of a form note. Each CDFIELD record defines the attributes of one field
 * in the form. (editods.h)
 * 
 * @author jgallagher
 *
 */
public class CDFIELD extends CDRecord {

	public CDFIELD(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return Field Flags (see Fxxx)
	 */
	public short getFlags() {
		// TODO create enum
		return getData().getShort(getData().position() + 0);
	}

	/**
	 * @return Alleged NSF Data Type
	 */
	public short getDataType() {
		// TODO create enum
		return getData().getShort(getData().position() + 2);
	}

	/**
	 * @return List Delimiters (LDELIM_xxx and LDDELIM_xxx)
	 */
	public short getListDelim() {
		// TODO create enum
		return getData().getShort(getData().position() + 4);
	}

	/**
	 * @return Number format, if applicable
	 */
	public NFMT getNumberFormat() {
		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 6);
		data.limit(data.position() + 4);
		return new NFMT(data);
	}

	/**
	 * @return Time format, if applicable
	 */
	public TFMT getTimeFormat() {
		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 10);
		data.limit(data.position() + 4);
		return new TFMT(data);
	}

	/**
	 * @return Displayed font
	 */
	public int getFontId() {
		// TODO map to font
		return getData().getInt(getData().position() + 14);
	}

	/**
	 * @return Default Value Formula length
	 */
	public short getDVLength() {
		return getData().getShort(getData().position() + 18);
	}

	/**
	 * @return Input Translation Formula length
	 */
	public short getITLength() {
		return getData().getShort(getData().position() + 20);
	}

	/**
	 * @return Order in tabbing sequence
	 */
	public short getTabOrder() {
		return getData().getShort(getData().position() + 22);
	}

	/**
	 * @return Input Validity Check Formula length
	 */
	public short getIVLength() {
		return getData().getShort(getData().position() + 24);
	}

	/**
	 * @return NSF Item Name
	 */
	public short getNameLength() {
		return getData().getShort(getData().position() + 26);
	}

	/**
	 * @return Length of the description of the item
	 */
	public short getDescLength() {
		return getData().getShort(getData().position() + 28);
	}

	/**
	 * @return Length of the text list of valid text values
	 */
	public short getTextValueLength() {
		return getData().getShort(getData().position() + 30);
	}

	// TODO check whether these are compiled or decompiled
	public String getDefaultValueFormula() {
		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 32);
		data.limit(data.position() + getDVLength());
		return ODSUtils.fromLMBCS(data);
	}

	public String getInputTranslationFormula() {
		int preceding = getDVLength();

		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 32 + preceding);
		data.limit(data.position() + getITLength());
		return ODSUtils.fromLMBCS(data);
	}

	public String getInputValidationFormula() {
		int preceding = (getDVLength() & 0xFF) + (getITLength() & 0xFF);

		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 32 + preceding);
		data.limit(data.position() + (getIVLength() & 0xFF));
		return ODSUtils.fromLMBCS(data);
	}

	public String getItemName() {
		int preceding = getDVLength() + getITLength() + getIVLength();

		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 32 + preceding);
		data.limit(data.position() + (getNameLength() & 0xFF));
		return ODSUtils.fromLMBCS(data);
	}

	public String getDescription() {
		int preceding = getDVLength() + getITLength() + getIVLength() + (getNameLength() & 0xFF);

		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 32 + preceding);
		data.limit(data.position() + getDescLength());
		return ODSUtils.fromLMBCS(data);
	}

	public List<String> getTextValues() {
		int totalLength = getTextValueLength();

		int preceding = getDVLength() + getITLength() + getIVLength() + (getNameLength() & 0xFF) + getDescLength();

		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 32 + preceding);
		data.limit(data.position() + 2);
		LIST list = new LIST(data);
		int listEntries = list.getListEntries() & 0xFF;

		data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 32 + preceding + 2);
		if (listEntries == 0 && totalLength > 2) {
			// Then we want to read a formula
			return Arrays.asList(ODSUtils.fromLMBCS(data));
		} else {
			System.out.println("list entrieS: " + listEntries);
			List<String> result = new ArrayList<String>(listEntries);

			short[] lengths = new short[listEntries];
			for (int i = 0; i < listEntries; i++) {
				lengths[i] = data.getShort();
			}

			for (int i = 0; i < listEntries; i++) {
				byte[] stringData = new byte[lengths[i]];
				data.get(stringData);
				result.add(ODSUtils.fromLMBCS(stringData));
			}

			return result;
		}
	}
}
