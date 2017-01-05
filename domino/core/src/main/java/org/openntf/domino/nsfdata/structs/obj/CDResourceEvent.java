/*
 * Copyright 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package org.openntf.domino.nsfdata.structs.obj;

import java.util.Iterator;

import org.openntf.domino.nsfdata.structs.cd.CDBLOBPART;
import org.openntf.domino.nsfdata.structs.cd.CDEVENT;
import org.openntf.domino.nsfdata.structs.cd.CDRecord;

public class CDResourceEvent extends CDObject {
	private CDEVENT event_;

	private byte[] fileData_;
	private byte[] sigData_;

	public CDResourceEvent(final CDEVENT event, final Iterator<CDRecord> records) {
		event_ = event;
		fileData_ = new byte[(int) event_.ActionLength.get()];
		sigData_ = new byte[event_.SignatureLength.get()];

		int ofs = 0;
		int sofs = 0;
		int len = 0;
		while (ofs < fileData_.length) {
			CDBLOBPART nextRecord = (CDBLOBPART) records.next();
			byte[] blobData = nextRecord.getBlobData();
			len = blobData.length;
			if (ofs + len >= fileData_.length) {
				len = fileData_.length - ofs;
				sofs = Math.min(blobData.length - len, event_.SignatureLength.get());
				System.arraycopy(blobData, 0, fileData_, ofs, len);
				System.arraycopy(blobData, len, sigData_, 0, sofs);
			} else {
				System.arraycopy(blobData, 0, fileData_, ofs, len);
			}
			ofs += len;
		}

		while (sofs < sigData_.length) {
			CDBLOBPART nextRecord = (CDBLOBPART) records.next();
			byte[] blobData = nextRecord.getBlobData();
			len = blobData.length;
			System.arraycopy(blobData, 0, sigData_, sofs, len);
			ofs += len;
		}
	}

	public byte[] getFileData() {
		return fileData_;
	}
}