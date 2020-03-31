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
 * This structure defines the file segment data of a Cascading Style Sheet (CSS) and follows a CDFILEHEADER structure. The number of
 * segments in the file is specified in the CDFILEHEADER record. (editods.h)
 * 
 * @since Lotus Notes/Domino 6.0
 *
 */
public class CDFILESEGMENT extends CDRecord {

	public final LSIG Header = inner(new LSIG());
	public final Unsigned16 DataSize = new Unsigned16();
	public final Unsigned16 SegSize = new Unsigned16();
	public final Unsigned32 Flags = new Unsigned32();
	public final Unsigned32 Reserved = new Unsigned32();

	static {
		addVariableData("FileData", "DataSize");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	//	@Override
	//	public int getExtraLength() {
	//		return SegSize.get() - DataSize.get();
	//	}

	/**
	 * @return File bits for this segment
	 */
	public byte[] getFileData() {
		return (byte[]) getVariableElement("FileData");
	}

	public void setFileData(final byte[] fileData) {
		setVariableElement("FileData", fileData);
	}
}
