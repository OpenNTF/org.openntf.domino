/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This structure specifies the start of an OLE Object. (editods.h)
 *
 */
@SuppressWarnings("nls")
public class CDOLEBEGIN extends CDRecord {

	/**
	 * These flags are used to define the type of OLE object a note contains. These flags are used by the Flags member of the CDOLEBEGIN
	 * data structure.
	 *
	 */
	public static enum Flag {
		/**
		 * The data is an OLE embedded object.
		 */
		OBJECT(0x01),
		/**
		 * The data is an OLE link.
		 */
		LINK(0x02),
		/**
		 * If this is a link, it is an Automatic (hot) link.
		 */
		AUTOLINK(0x04),
		/**
		 * If this is a link, it is a Manual (warm) link.
		 */
		MANUALLINK(0x08),
		/**
		 * This is a new object that was just inserted into the note.
		 */
		NEWOBJECT(0x10),
		/**
		 * This is a new object that was just pasted into the note.
		 */
		PASTED(0x20),
		/**
		 * This object came from a form and should be saved every time it changes in the server.
		 */
		SAVEOBJWHENCHANGED(0x40),
		/**
		 * This object was inherited from a form, or object incapable of rendering itself, so don't visualize it.
		 */
		NOVISUALIZE(0x80);

		private final int value_;

		private Flag(final int value) {
			value_ = value;
		}

		public int getValue() {
			return value_;
		}

		public static Set<Flag> valuesOf(final int flags) {
			Set<Flag> result = EnumSet.noneOf(Flag.class);
			for (Flag flag : values()) {
				if ((flag.getValue() & flags) > 0) {
					result.add(flag);
				}
			}
			return result;
		}
	}

	/**
	 * The symbols defined below specify which implementation of OLE was in use when the OLE object was created. (editods.h)
	 * 
	 * @since Lotus Notes 4.0
	 *
	 */
	public static enum OLEVersion {
		UNUSED0, VERSION1, VERSION2
	}

	public final WSIG Header = inner(new WSIG());
	public final Enum16<OLEVersion> Version = new Enum16<OLEVersion>(OLEVersion.values());
	/**
	 * Use getFlags for access.
	 */
	@Deprecated
	public final Unsigned32 Flags = new Unsigned32();
	public final Enum16<DDEFormat> ClipFormat = new Enum16<DDEFormat>(DDEFormat.values());
	//	public final Unsigned16 ClipFormat = new Unsigned16();
	public final Unsigned16 AttachNameLength = new Unsigned16();
	public final Unsigned16 ClassNameLength = new Unsigned16();
	public final Unsigned16 TemplateNameLength = new Unsigned16();

	static {
		addVariableString("AttachName", "AttachNameLength");
		addVariableString("ClassName", "ClassNameLength");
		addVariableString("TemplateName", "TemplateNameLength");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf((int) Flags.get());
	}

	/**
	 * @return Name of the attached OLE object file.
	 */
	public String getAttachName() {
		return (String) getVariableElement("AttachName");
	}

	/**
	 * @return The object's readable OLE class name.
	 */
	public String getClassName() {
		return (String) getVariableElement("ClassName");
	}

	/**
	 * @return The object's template class name.
	 */
	public String getTemplateName() {
		return (String) getVariableElement("TemplateName");
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Version=" + Version.get() + ", Flags=" + getFlags() + ", ClipFormat="
				+ ClipFormat.get() + ", AttachName=" + getAttachName() + ", ClassName=" + getClassName() + ", TemplateName="
				+ getTemplateName() + "]";
	}
}
