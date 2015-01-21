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

package org.openntf.domino.design.cd;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class CDResourceEvent {
	private CDEVENT event_;
	private CDFILESEGMENT[] segments_;

	//private String fileExt_;
	private int size_;
	private byte[] data_;

	private static final byte SIG_CD_EVENT = -7; // 249
	private static final byte SIG_CD_BLOBPART = -36; // 220

	public static CDResourceEvent fromFileData(final byte[] fileData, final String fileExt) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

		// First step is to write out a header record

		// Header.Signature, a 97 and a 0
		byteStream.write(SIG_CD_EVENT);
		byteStream.write((byte) 0);

		byte[] fileExtBytes = fileExt.getBytes();

		// Header.Length a DWORD
		// The size is the size of a CDFILEHEADER + FileExtLen, rounded
		byteStream.write(CDUtils.toWord(CDFILEHEADER.LENGTH + fileExtBytes.length + (fileExtBytes.length % 2)));

		// FileExtLen, a WORD
		byteStream.write(CDUtils.toWord((short) fileExtBytes.length));

		// FileDataSize, a DWORD
		byteStream.write(CDUtils.toWord(fileData.length));

		// SegCount, a DWORD - break up the data into 10240-byte chunks
		int chunks = fileData.length / 10240;
		if (fileData.length % 10240 > 0) {
			chunks++;
		}
		byteStream.write(CDUtils.toWord(chunks));

		// Flags, a DWORD - always 0
		byteStream.write(CDUtils.toWord(0));

		// Reserved, a DWORD - always 0
		byteStream.write(CDUtils.toWord(0));

		// Next would be the file ext with an optional padding byte
		byteStream.write(fileExtBytes);
		if (fileExtBytes.length % 2 != 0) {
			byteStream.write((byte) 0);
		}

		// Now write out the data in chunks
		for (int i = 0; i < chunks; i++) {
			// Each chunk begins with a CDFILESEGMENT

			// Figure out our data and segment sizes
			int dataOffset = 10240 * i;
			short dataSize = (short) ((fileData.length - dataOffset) > 10240 ? 10240 : (fileData.length - dataOffset));
			short segSize = (short) (dataSize % 2 == 0 ? dataSize : dataSize + 1);

			// Header.Signature, a 96 and a 0
			byteStream.write(SIG_CD_EVENT);
			byteStream.write((byte) 0);

			// Header.Length, a DWORD
			// This is the segment header size + the data size
			byteStream.write(CDUtils.toWord(dataSize + CDFILESEGMENT.LENGTH));

			// DataSize, a WORD
			byteStream.write(CDUtils.toWord(dataSize));

			// SegSize, a WORD
			byteStream.write(CDUtils.toWord(segSize));

			// Flags, a DWORD - always 0
			byteStream.write(CDUtils.toWord(0));

			// Reserved, a DWORD - always 0
			byteStream.write(CDUtils.toWord(0));

			// Now our segment of data
			for (int j = dataOffset; j < dataOffset + dataSize; j++) {
				byteStream.write(fileData[j]);
			}
			// Fill out the padding - this should be 0 or 1, but may as well loop
			for (int j = dataSize; j < segSize; j++) {
				byteStream.write((byte) 0);
			}
		}

		return new CDResourceEvent(byteStream.toByteArray());
	}

	public CDResourceEvent(final byte[] data) {
		int offset = 0;

		// The record begins with two bytes, which may be part of an LSIG, a WSIG, or a BSIG
		// The low-order byte is the signature value; the high-order determines the whole structure type
		// In our case, it's always LSIG
		int lowOrder = data[0];
		@SuppressWarnings("unused")
		int highOrder = data[1];

		if (lowOrder != SIG_CD_EVENT) {
			throw new IllegalArgumentException("CD is not a CDEVENT type");
		}

		event_ = new CDEVENT(data);

		size_ = event_.ActionLength;

		offset += CDEVENT.LENGTH;

		// The added length may be longer than the file ext - perhaps this is for a WORD-size filler byte
		offset += event_.Header.Length - CDEVENT.LENGTH;

		data_ = new byte[size_];
		int resultOffset = 0;

		//while (offset < data.length) {
		lowOrder = data[offset];
		highOrder = data[offset + 1];

		if (lowOrder != SIG_CD_BLOBPART) {
			throw new IllegalArgumentException("CD is not a CDBLOBPART type");
		}
		CDBLOBPART blobPart = new CDBLOBPART(data, offset);
		offset += CDBLOBPART.LENGTH + blobPart.Length;
		data_ = blobPart.data;
		//System.arraycopy(blobPart.data, 1, data_, resultOffset, 1000);
		resultOffset += blobPart.Length;

		//}

	}

	public int getSize() {
		return size_;
	}

	public byte[] getData() {
		return Arrays.copyOf(data_, data_.length);
	}

	public byte[] getBytes() throws IOException {
		throw new UnsupportedOperationException();
	}
}