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

import org.openntf.domino.nsfdata.structs.LSIG;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure is used to define a Cascading Style Sheet (CSS) that is part of a Domino database. CDFILESEGMENT structure(s) follow the
 * CDFILEHEADER. (editods.h)
 * 
 * @since Lotus Notes/Domino 6.0
 *
 */
@SuppressWarnings("nls")
public class CDFILEHEADER extends CDRecord {

	public final LSIG Header = inner(new LSIG());
	public final Unsigned16 FileExtLen = new Unsigned16();
	public final Unsigned32 FileDataSize = new Unsigned32();
	public final Unsigned32 SegCount = new Unsigned32();
	public final Unsigned32 Flags = new Unsigned32();
	public final Unsigned32 Reserved = new Unsigned32();

	static {
		addVariableAsciiString("FileExt", "FileExtLen");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	/**
	 * @return The file extension for the file
	 */
	public String getFileExt() {
		return (String) getVariableElement("FileExt");
	}

	public void setFileExt(final String fileExt) {
		setVariableElement("FileExt", fileExt);
	}
}
