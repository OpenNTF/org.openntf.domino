package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.openntf.domino.nsfdata.NSFCompiledFormula;
import org.openntf.domino.nsfdata.structs.FONTID;
import org.openntf.domino.nsfdata.structs.LIST;
import org.openntf.domino.nsfdata.structs.NFMT;
import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.TFMT;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This defines the structure of a CDFIELD record in the $Body item of a form note. Each CDFIELD record defines the attributes of one field
 * in the form. (editods.h)
 *
 */
public class CDFIELD extends CDRecord {
	public static enum ListDelim {
		SPACE((short) 0x0001), COMMA((short) 0x0002), SEMICOLON((short) 0x0004), NEWLINE((short) 0x0008), BLANKLINE((short) 0x0010),
		D_SPACE((short) 0x1000), D_COMMA((short) 0x2000), D_SEMICOLON((short) 0x3000), D_NEWLINE((short) 0x4000),
		D_BLANKLINE((short) 0x5000);

		public static final int LD_MASK = 0x0fff;
		public static final int LDD_MASK = 0xf000;

		private final short value_;

		private ListDelim(final short value) {
			value_ = value;
		}

		public short getValue() {
			return value_;
		}

		public static Set<ListDelim> valuesOf(final int flags) {
			Set<ListDelim> result = EnumSet.noneOf(ListDelim.class);
			for (ListDelim flag : values()) {
				if ((flag.getValue() & flags) > 0) {
					result.add(flag);
				}
			}
			return result;
		}
	}

	public static enum FieldType {
		ERROR((short) 0), NUMBER((short) 1), TIME((short) 2), RICH_TEXT((short) 3), AUTHORS((short) 4), READERS((short) 5),
		NAMES((short) 6), KEYWORDS((short) 7), TEXT((short) 8), SECTION((short) 9), PASSWORD((short) 10), FORMULA((short) 11),
		TIMEZONE((short) 12);

		private final short value_;

		private FieldType(final short value) {
			value_ = (short) (value + 1280);
		}

		public short getValue() {
			return value_;
		}

		public static FieldType valueOf(final short typeCode) {
			for (FieldType type : values()) {
				if (type.getValue() == typeCode) {
					return type;
				}
			}
			throw new IllegalArgumentException("No matching FieldType found for type code " + typeCode);
		}
	}

	public static enum Flag {
		/**
		 * Field contains read/writers
		 */
		READWRITERS((short) 0x0001),
		/**
		 * Field is editable, not read only
		 */
		EDITABLE((short) 0x0002),
		/**
		 * Field contains distinguished names
		 */
		NAMES((short) 0x0004),
		/**
		 * Store DV, even if not spec'ed by user
		 */
		STOREDV((short) 0x0008),
		/**
		 * Field contains document readers
		 */
		READERS((short) 0x0010),
		/**
		 * Field contains a section
		 */
		SECTION((short) 0x0020),
		/**
		 * can be assumed to be clear in memory, V3 & later
		 */
		SPARE3((short) 0x0040),
		/**
		 * IF CLEAR, CLEAR AS ABOVE
		 */
		V3FAB((short) 0x0080),
		/**
		 * Field is a computed field
		 */
		COMPUTED((short) 0x0100),
		/**
		 * Field is a keywords field
		 */
		KEYWORDS((short) 0x0200),
		/**
		 * Field is protected
		 */
		PROTECTED((short) 0x0400),
		/**
		 * Field name is simply a reference to a shared field note
		 */
		REFERENCE((short) 0x0800),
		/**
		 * sign field
		 */
		SIGN((short) 0x1000),
		/**
		 * seal field
		 */
		SEAL((short) 0x2000),
		/**
		 * standard UI
		 */
		KEYWORDS_UI_STANDARD((short) 0x0000),
		/**
		 * checkbox UI
		 */
		KEYWORDS_UI_CHECKBOX((short) 0x4000),
		/**
		 * radiobutton UI
		 */
		KEYWORDS_UI_RADIOBUTTON((short) 0x8000),
		/**
		 * allow doc editor to add new values
		 */
		KEYWORDS_UI_ALLOW_NEW((short) 0xc000);

		private final short value_;

		private Flag(final short value) {
			value_ = value;
		}

		public short getValue() {
			return value_;
		}

		public static Set<Flag> valuesOf(final short flags) {
			Set<Flag> result = EnumSet.noneOf(Flag.class);
			for (Flag flag : values()) {
				if ((flag.getValue() & flags) > 0) {
					result.add(flag);
				}
			}
			return result;
		}
	}

	static {
		addFixed("Flags", Short.class);
		addFixed("DataType", Short.class);
		addFixed("ListDelim", Short.class);
		addFixed("NumberFormat", NFMT.class);
		addFixed("TimeFormat", TFMT.class);
		addFixed("FontID", FONTID.class);
		addFixedUnsigned("DVLength", Short.class);
		addFixedUnsigned("ITLength", Short.class);
		addFixedUnsigned("TabOrder", Short.class);
		addFixedUnsigned("IVLength", Short.class);
		addFixedUnsigned("NameLength", Short.class);
		addFixedUnsigned("DescLength", Short.class);
		addFixedUnsigned("TextValueLength", Short.class);

		addVariableData("DV", "getDVLength");
		addVariableData("IT", "getITLength");
		addVariableData("IV", "getIVLength");
		addVariableString("Name", "getNameLength");
		addVariableString("Desc", "getDescLength");
	}

	public static final int SIZE = getFixedStructSize();

	public CDFIELD(final CDSignature cdSig) {
		super(new WSIG(cdSig, cdSig.getSize() + SIZE), ByteBuffer.wrap(new byte[SIZE]));
	}

	public CDFIELD(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return Field Flags (see Fxxx)
	 */
	public Set<Flag> getFlags() {
		return Flag.valuesOf((Short) getStructElement("Flags"));
	}

	/**
	 * @return Alleged NSF Data Type
	 */
	public short getDataType() {
		// TODO Fix this
		//		return FieldType.valueOf((Short) getStructElement("DataType"));
		return (Short) getStructElement("DataType");
	}

	/**
	 * @return List Delimiters
	 */
	public Set<ListDelim> getListDelim() {
		// TODO make this properly distinguish betwee display and input formats
		return ListDelim.valuesOf((Short) getStructElement("ListDelim"));
	}

	/**
	 * @return Number format, if applicable
	 */
	public NFMT getNumberFormat() {
		return (NFMT) getStructElement("NumberFormat");
	}

	/**
	 * @return Time format, if applicable
	 */
	public TFMT getTimeFormat() {
		return (TFMT) getStructElement("TimeFormat");
	}

	/**
	 * @return Displayed font
	 */
	public FONTID getFontId() {
		return (FONTID) getStructElement("FontID");
	}

	/**
	 * @return Default Value Formula length
	 */
	public int getDVLength() {
		return (Integer) getStructElement("DVLength");
	}

	/**
	 * @return Input Translation Formula length
	 */
	public int getITLength() {
		return (Integer) getStructElement("ITLength");
	}

	/**
	 * @return Order in tabbing sequence
	 */
	public int getTabOrder() {
		return (Integer) getStructElement("TabOrder");
	}

	/**
	 * @return Input Validity Check Formula length
	 */
	public int getIVLength() {
		return (Integer) getStructElement("IVLength");
	}

	/**
	 * @return NSF Item Name
	 */
	public int getNameLength() {
		return (Integer) getStructElement("NameLength");
	}

	/**
	 * @return Length of the description of the item
	 */
	public int getDescLength() {
		return (Integer) getStructElement("DescLength");
	}

	/**
	 * @return Length of the text list of valid text values
	 */
	public int getTextValueLength() {
		return (Integer) getStructElement("TextValueLength");
	}

	public NSFCompiledFormula getDefaultValueFormula() {
		int length = getDVLength();
		if (length > 0) {
			return new NSFCompiledFormula((byte[]) getStructElement("DV"));
		} else {
			return null;
		}
	}

	public NSFCompiledFormula getInputTranslationFormula() {
		int length = getITLength();

		if (length > 0) {
			return new NSFCompiledFormula((byte[]) getStructElement("IT"));
		} else {
			return null;
		}
	}

	public NSFCompiledFormula getInputValidationFormula() {
		int length = getIVLength();

		if (length > 0) {
			return new NSFCompiledFormula((byte[]) getStructElement("IV"));
		} else {
			return null;
		}
	}

	public String getItemName() {
		return (String) getStructElement("Name");
	}

	public String getDescription() {
		return (String) getStructElement("Desc");
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

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Flags=" + getFlags() + ", DataType=" + getDataType() + ", ListDelim=" + getListDelim()
				+ ", NumberFormat=" + getNumberFormat() + ", TimeFormat=" + getTimeFormat() + ", FontID=" + getFontId() + ", DVLength="
				+ getDVLength() + ", ITLength=" + getITLength() + ", TabOrder=" + getTabOrder() + ", DefaultValueFormula="
				+ getDefaultValueFormula() + ", InputTranslationFormula=" + getInputTranslationFormula() + ", InputValidationFormula="
				+ getInputValidationFormula() + ", ItemName=" + getItemName() + ", Description=" + getDescription() + ", TextValues="
				+ getTextValues() + "]";
	}
}
