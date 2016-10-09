package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
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
	public final WSIG Header = inner(new WSIG());
	/**
	 * Use getFlags for access.
	 */
	@Deprecated
	public final Unsigned16 Flags = new Unsigned16();
	// TODO make enum - this is weirder than it seems at first blush
	//	public final Enum16<FieldType> DataType = new Enum16<FieldType>(FieldType.values());
	public final Unsigned16 DataType = new Unsigned16();
	/**
	 * Use getListDelim for access.
	 */
	@Deprecated
	public final Unsigned16 ListDelim = new Unsigned16();
	public final NFMT NumberFormat = inner(new NFMT());
	public final TFMT TimeFormat = inner(new TFMT());
	public final FONTID FontID = inner(new FONTID());
	public final Unsigned16 DVLength = new Unsigned16();
	public final Unsigned16 ITLength = new Unsigned16();
	public final Unsigned16 TabOrder = new Unsigned16();
	public final Unsigned16 IVLength = new Unsigned16();
	public final Unsigned16 NameLength = new Unsigned16();
	public final Unsigned16 DescLength = new Unsigned16();
	public final Unsigned16 TextValueLength = new Unsigned16();

	static {
		addVariableData("DV", "DVLength");
		addVariableData("IT", "ITLength");
		addVariableData("IV", "IVLength");
		addVariableString("Name", "NameLength");
		addVariableString("Desc", "DescLength");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	public Set<FieldFlag> getFlags() {
		return FieldFlag.valuesOf((short) Flags.get());
	}

	public Set<ListDelimiter> getListDelim() {
		// TODO make this properly distinguish between display and input formats
		return ListDelimiter.valuesOf((short) ListDelim.get());
	}

	public NSFCompiledFormula getDefaultValueFormula() {
		int length = DVLength.get();
		if (length > 0) {
			return new NSFCompiledFormula((byte[]) getVariableElement("DV"));
		} else {
			return null;
		}
	}

	public NSFCompiledFormula getInputTranslationFormula() {
		int length = ITLength.get();

		if (length > 0) {
			return new NSFCompiledFormula((byte[]) getVariableElement("IT"));
		} else {
			return null;
		}
	}

	public NSFCompiledFormula getInputValidationFormula() {
		int length = IVLength.get();

		if (length > 0) {
			return new NSFCompiledFormula((byte[]) getVariableElement("IV"));
		} else {
			return null;
		}
	}

	public String getItemName() {
		return (String) getVariableElement("Name");
	}

	public String getDescription() {
		return (String) getVariableElement("Desc");
	}

	/**
	 * @return For non-keyword fields, null; for static-keyword fields, a List<String>; for formula-keyword fields, an NSFCompiledFormula
	 */
	public Object getTextValues() {
		int totalLength = TextValueLength.get();

		if (totalLength > 0) {
			// TODO see how this works with actual values
			int preceding = DVLength.get() + ITLength.get() + IVLength.get() + (NameLength.get() & 0xFFFF) + DescLength.get();

			ByteBuffer data = getData().duplicate();
			data.order(ByteOrder.LITTLE_ENDIAN);
			data.position(data.position() + 32 + preceding);
			data.limit(data.position() + 2);
			LIST list = new LIST();
			list.init(data);
			int listEntries = list.ListEntries.get();

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

	//	@Override
	//	public String toString() {
	//		return "[" + getClass().getSimpleName() + ": Flags=" + getFlags() + ", DataType=" + getDataType() + ", ListDelim=" + getListDelim()
	//				+ ", NumberFormat=" + getNumberFormat() + ", TimeFormat=" + getTimeFormat() + ", FontID=" + getFontId() + ", DVLength="
	//				+ getDVLength() + ", ITLength=" + getITLength() + ", TabOrder=" + getTabOrder() + ", DefaultValueFormula="
	//				+ getDefaultValueFormula() + ", InputTranslationFormula=" + getInputTranslationFormula() + ", InputValidationFormula="
	//				+ getInputValidationFormula() + ", ItemName=" + getItemName() + ", Description=" + getDescription() + ", TextValues="
	//				+ getTextValues() + "]";
	//	}
}
