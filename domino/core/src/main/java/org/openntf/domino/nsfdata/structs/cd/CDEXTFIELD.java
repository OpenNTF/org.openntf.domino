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

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * New field attributes have been added in Release 4.0 of Notes. To preserve compatibility with existing applications, the new attributes
 * have been placed in this extension to the CDFIELD record. This record is optional, and may not be present in the $Body item of the form
 * note. (editods.h)
 * 
 * @since Lotus Notes 4.0
 */
@SuppressWarnings("nls")
public class CDEXTFIELD extends CDRecord {
	/**
	 * These identifiers indicate the type of helper in use by the Keyword and the Name helper/pickers.
	 * 
	 * @since Lotus Notes 4.0
	 *
	 */
	public static enum FieldHelper {
		NONE, ADDRDLG, ACLDLG, VIEWDLG
	}

	public final WSIG Header = inner(new WSIG());
	// TODO make enum
	public final Unsigned32 Flags1 = new Unsigned32();
	// TODO make enum
	public final Unsigned32 Flags2 = new Unsigned32();
	public final Enum16<FieldHelper> EntryHelper = new Enum16<FieldHelper>(FieldHelper.values());
	public final Unsigned16 EntryDBNameLen = new Unsigned16();
	public final Unsigned16 EntryViewNameLen = new Unsigned16();
	public final Unsigned16 EntryColumnNumber = new Unsigned16();

	static {
		addVariableString("EntryDBName", "EntryDBNameLen");
		addVariableString("EntryViewName", "EntryViewNameLen");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	public String getEntryHelperDBName() {
		return (String) getVariableElement("EntryDBName");
	}

	public String getEntryHelperViewName() {
		return (String) getVariableElement("EntryViewName");
	}
}
