/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.nsfdata.structs.cd;

import java.util.Set;

import org.openntf.domino.nsfdata.NSFCompiledFormula;
import org.openntf.domino.nsfdata.structs.FONTID;
import org.openntf.domino.nsfdata.structs.NFMT;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.TFMT;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This item is an obsolete definition of the attributes of a field in a form note. It is included in this reference for compatibility with
 * forms created with pre-V1 releases of Notes. (editods.h)
 * 
 * @since prehistory
 *
 */
@SuppressWarnings("nls")
public class CDFIELD_PRE_36 extends CDRecord {

	public final WSIG Header = inner(new WSIG());
	/**
	 * Use getFlags() for access
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
	public final Unsigned16 Unused1 = new Unsigned16();
	public final Unsigned16 IVLength = new Unsigned16();
	public final Unsigned16 NameLength = new Unsigned16();
	public final Unsigned16 DescLength = new Unsigned16();

	static {
		addVariableData("DV", "DVLength");
		addVariableData("IT", "ITLength");
		addVariableData("IV", "IVLength");
		addVariableAsciiString("Name", "NameLength");
		addVariableAsciiString("Desc", "DescLength");
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
}
