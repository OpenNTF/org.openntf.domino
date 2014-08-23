package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import org.openntf.domino.nsfdata.NSFCompiledFormula;
import org.openntf.domino.nsfdata.structs.LIST;
import org.openntf.domino.nsfdata.structs.NFMT;
import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.TFMT;

/**
 * This defines the structure of a CDFIELD record in the $Body item of a form note. Each CDFIELD record defines the attributes of one field
 * in the form. (editods.h)
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
	 * This is a WORD - unsigned short - so upgrade it to int for Java's sake
	 * 
	 * @return Default Value Formula length
	 */
	public int getDVLength() {
		return getData().getShort(getData().position() + 18) & 0xFFFF;
	}

	/**
	 * This is a WORD - unsigned short - so upgrade it to int for Java's sake
	 * 
	 * @return Input Translation Formula length
	 */
	public int getITLength() {
		return getData().getShort(getData().position() + 20) & 0xFFFF;
	}

	/**
	 * This is a WORD - unsigned short - so upgrade it to int for Java's sake
	 * 
	 * @return Order in tabbing sequence
	 */
	public int getTabOrder() {
		return getData().getShort(getData().position() + 22) & 0xFFFF;
	}

	/**
	 * This is a WORD - unsigned short - so upgrade it to int for Java's sake
	 * 
	 * @return Input Validity Check Formula length
	 */
	public int getIVLength() {
		return getData().getShort(getData().position() + 24) & 0xFFFF;
	}

	/**
	 * This is a WORD - unsigned short - so upgrade it to int for Java's sake
	 * 
	 * @return NSF Item Name
	 */
	public int getNameLength() {
		return getData().getShort(getData().position() + 26) & 0xFFFF;
	}

	/**
	 * This is a WORD - unsigned short - so upgrade it to int for Java's sake
	 * 
	 * @return Length of the description of the item
	 */
	public int getDescLength() {
		return getData().getShort(getData().position() + 28) & 0xFFFF;
	}

	/**
	 * This is a WORD - unsigned short - so upgrade it to int for Java's sake
	 * 
	 * @return Length of the text list of valid text values
	 */
	public int getTextValueLength() {
		return getData().getShort(getData().position() + 30) & 0xFFFF;
	}

	public NSFCompiledFormula getDefaultValueFormula() {
		int length = getDVLength();
		if (length > 0) {
			ByteBuffer data = getData().duplicate();
			data.position(data.position() + 32);
			data.limit(data.position() + length);
			return new NSFCompiledFormula(data);
		} else {
			return null;
		}
	}

	public NSFCompiledFormula getInputTranslationFormula() {
		int length = getITLength();

		if (length > 0) {
			int preceding = getDVLength();

			ByteBuffer data = getData().duplicate();
			data.position(data.position() + 32 + preceding);
			data.limit(data.position() + length);
			return new NSFCompiledFormula(data);
		} else {
			return null;
		}
	}

	public NSFCompiledFormula getInputValidationFormula() {
		int length = getIVLength();

		if (length > 0) {
			int preceding = getDVLength() + getITLength();

			ByteBuffer data = getData().duplicate();
			data.position(data.position() + 32 + preceding);
			data.limit(data.position() + length);
			return new NSFCompiledFormula(data);
		} else {
			return null;
		}
	}

	public String getItemName() {
		int preceding = getDVLength() + getITLength() + getIVLength();

		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 32 + preceding);
		data.limit(data.position() + getNameLength());
		return ODSUtils.fromLMBCS(data);
	}

	public String getDescription() {
		int preceding = getDVLength() + getITLength() + getIVLength() + getNameLength();

		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 32 + preceding);
		data.limit(data.position() + getDescLength());
		return ODSUtils.fromLMBCS(data);
	}

	/**
	 * @return For non-keyword fields, null; for static-keyword fields, a List<String>; for formula-keyword fields, an NSFCompiledFormula
	 */
	public Object getTextValues() {
		int totalLength = getTextValueLength();

		if (totalLength > 0) {
			// TODO see how this works with actual values
			int preceding = getDVLength() + getITLength() + getIVLength() + (getNameLength() & 0xFFFF) + getDescLength();

			ByteBuffer data = getData().duplicate();
			data.order(ByteOrder.LITTLE_ENDIAN);
			data.position(data.position() + 32 + preceding);
			data.limit(data.position() + 2);
			LIST list = new LIST(data);
			int listEntries = list.getListEntries() & 0xFFFF;

			data = getData().duplicate();
			data.order(ByteOrder.LITTLE_ENDIAN);
			data.position(data.position() + 32 + preceding + 2);
			if (listEntries == 0 && totalLength > 2) {
				return new NSFCompiledFormula(data);
			} else {
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
		} else {
			return null;
		}
	}
}
