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

import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.BSIG;
import org.openntf.domino.nsfdata.structs.COLOR_VALUE;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This defines the structure of the document information field in a form note. A document information field is an item with name $INFO
 * (ITEM_NAME_DOCUMENT) and data type TYPE_COMPOSITE. The document information field defines attributes of documents created with that form.
 * (editods.h)
 *
 */
public class CDDOCUMENT extends CDRecord {
	public static enum FormFlag1 {
		REFERENCE(0x0001), MAIL(0x0002), NOTEREF(0x0004), NOTEREF_MAIN(0x0008), RECALC(0x0010), BOILERPLATE(0x0020), FGCOLOR(0x0040),
		SPARESOK(0x0080), ACTIVATE_OBJECT_COMP(0x0100), ACTIVATE_OBJECT_EDIT(0x0200), ACTIVATE_OBJECT_READ(0x0400),
		SHOW_WINDOW_COMPOSE(0x0800), SHOW_WINDOW_EDIT(0x1000), SHOW_WINDOW_READ(0x2000), UPDATE_RESPONSE(0x4000), UPDATE_PARENT(0x8000);

		private final short value_;

		private FormFlag1(final int value) {
			value_ = (short) value;
		}

		public short getValue() {
			return value_;
		}

		public static Set<FormFlag1> valuesOf(final short flags) {
			Set<FormFlag1> result = EnumSet.noneOf(FormFlag1.class);
			for (FormFlag1 flag : values()) {
				if ((flag.getValue() & flags) > 0) {
					result.add(flag);
				}
			}
			return result;
		}
	}

	public final BSIG Header = inner(new BSIG());
	/**
	 * Use getPaperColor for access.
	 */
	@Deprecated
	public final Unsigned16 PaperColor = new Unsigned16();
	/**
	 * Use getFormFlags for access.
	 */
	@Deprecated
	public final Unsigned16 FormFlags = new Unsigned16();
	/**
	 * Use getNotePrivileges for access.
	 */
	@Deprecated
	public final Unsigned16 NotePrivileges = new Unsigned16();
	/**
	 * Use getFormFlags2 for access.
	 */
	@Deprecated
	public final Unsigned16 FormFlags2 = new Unsigned16();
	public final Unsigned16 InherFieldNameLength = new Unsigned16();
	/**
	 * Use getPaperColorExt for access.
	 */
	@Deprecated
	public final Unsigned16 PaperColorExt = new Unsigned16();
	public final COLOR_VALUE PaperColorValue = inner(new COLOR_VALUE());
	/**
	 * Use getFormFlags3 for access.
	 */
	@Deprecated
	public final Unsigned16 FormFlags3 = new Unsigned16();
	public final Unsigned16[] Spare = array(new Unsigned16[1]);

	static {
		addVariableString("InherFieldName", "InherFieldNameLength");
		addVariableString("FieldName", "getFieldNameLength");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	/**
	 * @return Color of the paper being used
	 */
	public short getPaperColor() {
		// TODO make an enum
		return (short) PaperColor.get();
	}

	public Set<FormFlag1> getFormFlags() {
		return FormFlag1.valuesOf((short) FormFlags.get());
	}

	/**
	 * @return Privs for notes created when using form
	 */
	public short getNotePrivileges() {
		// TODO make an enum
		return (short) NotePrivileges.get();
	}

	/**
	 * @return more Form Flags
	 */
	public short getFormFlags2() {
		// TODO make an enum
		return (short) FormFlags2.get();
	}

	/**
	 * @return Palette Color of the paper being used. Index into 240 color table.
	 * @since Lotus Notes 4.0
	 */
	public short getPaperColorExt() {
		// TODO make an enum
		return (short) PaperColorExt.get();
	}

	public short getFormFlags3() {
		// TODO make an enum
		return (short) FormFlags3.get();
	}

	public String getInheritFieldName() {
		// In builds prior to 100 (somewhere in R3), data past NotePrivileges was junk,
		// so don't try to get variable data
		if (getFormFlags().contains(FormFlag1.SPARESOK)) {
			return (String) getVariableElement("InherFieldName");
		} else {
			return "";
		}
	}

	public int getFieldNameLength() {
		return (int) (Header.getRecordLength() - size() - InherFieldNameLength.get());
	}

	/**
	 * @return string indicating which field to append version number to
	 */
	public String getFieldName() {
		return (String) getVariableElement("FieldName");
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": PaperColor=" + PaperColor.get() + ", FormFlags=" + getFormFlags()
				+ ", NotePrivileges=" + getNotePrivileges() + ", FormFlags2=" + getFormFlags2() + ", InheritFieldName="
				+ getInheritFieldName() + ", PaperColorExt=" + getPaperColorExt() + ", PaperColorValue=" + PaperColorValue
				+ ", FormFlags3=" + getFormFlags3() + "]";
	}
}
