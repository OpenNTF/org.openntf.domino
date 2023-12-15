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
package org.openntf.domino.nsfdata.structs.obj;

import java.nio.ByteBuffer;
import java.util.Iterator;

import org.openntf.domino.nsfdata.structs.cd.CDFILEHEADER;
import org.openntf.domino.nsfdata.structs.cd.CDFILESEGMENT;
import org.openntf.domino.nsfdata.structs.cd.CDRecord;
import org.openntf.domino.nsfdata.structs.cd.CDSignature;

/**
 * This is a convenience class that wrapped a composite data file - one CDFILEHEADER record and zero or more CDFILESEGMENT records - into a
 * single entity.
 * 
 * @author jgallagher
 * 
 */
public class CDResourceFile extends CDObject {

	// It appears that segments cap out at 10240 bytes of data
	public static final int SEGMENT_SIZE_CAP = 10240;

	public CDFILEHEADER header_;
	public byte[] fileData_;

	private transient CDFILESEGMENT[] segments_;
	private int totalSize = 0;

	//	/**
	//	 * Creates a CDResourceFile wrapping the underlying byte array. Until the file is modified, it does <b>not</b> copy the bytes, so any
	//	 * modifications to the underlying array will also modify this data.
	//	 * 
	//	 * @param data
	//	 *            A byte array representing a valid CD file record (i.e. one CDFILEHEADER and zero or more CDFILESEGMENTs).
	//	 */
	//	public CDResourceFile(final byte[] data) {
	//		CData cdata = new CData(ByteBuffer.wrap(data));
	//		Iterator<CDRecord> iter = cdata.iterator();
	//		header_ = (CDFILEHEADER) iter.next();
	//		fileData_ = new byte[(int) header_.FileDataSize.get()];
	//		int ofs = 0;
	//		for (int i = 0; i < header_.SegCount.get(); i++) {
	//			CDFILESEGMENT segment = (CDFILESEGMENT) iter.next();
	//			ofs += segment.getPayload(fileData_, ofs);
	//		}
	//	}

	public CDResourceFile(final String fileExt) {
		header_ = new CDFILEHEADER();
		header_.init();
		header_.Header.setSigIdentifier(CDSignature.FILEHEADER.getBaseValue());
		header_.setFileExt(fileExt);
		header_.Header.setRecordLength(header_.size() + header_.getVariableSize());
		header_.FileDataSize.set(0);
		header_.SegCount.set(0);
		fileData_ = new byte[0];
	}

	public CDResourceFile(final CDFILEHEADER header, final Iterator<CDRecord> records) {
		header_ = header;
		fileData_ = new byte[(int) header_.FileDataSize.get()];
		int ofs = 0;
		for (int i = 0; i < header_.SegCount.get(); i++) {
			CDFILESEGMENT segment = (CDFILESEGMENT) records.next();
			ofs += segment.getPayload(fileData_, ofs);
		}
	}

	public void setFileData(final byte[] fileData) {
		header_.FileDataSize.set(fileData.length);
		fileData_ = fileData;
		segments_ = null;

	}

	public ByteBuffer getData() {
		// Determine how many file segments will be needed based on the inferred size cap
		if (segments_ == null) {
			int chunks = fileData_.length / SEGMENT_SIZE_CAP;
			if (fileData_.length % SEGMENT_SIZE_CAP > 0) {
				chunks++;
			}
			header_.SegCount.set(chunks);
			segments_ = new CDFILESEGMENT[chunks];
			totalSize = (int) header_.getTotalSize();
			for (int i = 0; i < chunks; i++) {
				// Each chunk begins with a CDFILESEGMENT

				// Figure out our data and segment sizes
				int dataOffset = SEGMENT_SIZE_CAP * i;
				short dataSize = (short) ((fileData_.length - dataOffset) > SEGMENT_SIZE_CAP ? SEGMENT_SIZE_CAP
						: (fileData_.length - dataOffset));
				short segSize = (short) (dataSize % 2 == 0 ? dataSize : dataSize + 1);

				segments_[i] = new CDFILESEGMENT();
				segments_[i].init();
				segments_[i].Header.setSigIdentifier(CDSignature.FILESEGMENT.getBaseValue());
				segments_[i].SegSize.set(segSize);
				segments_[i].setFileData(fileData_);
				segments_[i].Header.setRecordLength(segments_[i].size() + segments_[i].getVariableSize());
				totalSize += segments_[i].getTotalSize();
			}
		}

		ByteBuffer result = ByteBuffer.allocate(totalSize);

		//		ByteBuffer headerBuffer = header_.getByteBuffer();
		//		headerBuffer.position(0);
		result.put(header_.getBytes());
		for (CDFILESEGMENT seg : segments_) {
			//			ByteBuffer segBuffer = seg.getByteBuffer();
			//			segBuffer.position(0);
			byte[] segBytes = seg.getBytes();
			result.put(segBytes);
		}
		result.position(0);
		return result;
	}

	public byte[] getFileData() {
		return fileData_;
	}
}
