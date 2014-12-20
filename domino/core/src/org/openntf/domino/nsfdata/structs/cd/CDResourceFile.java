package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.util.Iterator;

/**
 * This is a convenience class that wrapped a composite data file - one CDFILEHEADER record and zero or more CDFILESEGMENT records - into a
 * single entity.
 * 
 * @author jgallagher
 *
 */
public class CDResourceFile {

	// It appears that segments cap out at 10240 bytes of data
	public static final int SEGMENT_SIZE_CAP = 10240;

	public CDFILEHEADER header_;
	public CDFILESEGMENT[] segments_;

	/**
	 * @param data
	 *            A byte array representing a valid CD file record (i.e. one CDFILEHEADER and zero or more CDFILESEGMENTs).
	 */
	public CDResourceFile(final byte[] data) {
		CData cdata = new CData(ByteBuffer.wrap(data));
		Iterator<CDRecord> iter = cdata.iterator();
		header_ = (CDFILEHEADER) iter.next();
		segments_ = new CDFILESEGMENT[(int) header_.SegCount.get()];
		for (int i = 0; i < header_.SegCount.get(); i++) {
			segments_[i] = (CDFILESEGMENT) iter.next();
		}
	}

	public CDResourceFile(final String fileExt) {
		header_ = new CDFILEHEADER();
		header_.setFileExt(fileExt);
		header_.FileDataSize.set(0);
		header_.SegCount.set(0);
	}

	public void setFileData(final byte[] fileData) {
		header_.FileDataSize.set(fileData.length);

		// Determine how many file segments will be needed based on the inferred size cap
		int chunks = fileData.length / SEGMENT_SIZE_CAP;
		if (fileData.length % 10240 > 0) {
			chunks++;
		}
		header_.SegCount.set(chunks);
		segments_ = new CDFILESEGMENT[chunks];

		for (int i = 0; i < chunks; i++) {
			// Each chunk begins with a CDFILESEGMENT

			// Figure out our data and segment sizes
			int dataOffset = SEGMENT_SIZE_CAP * i;
			short dataSize = (short) ((fileData.length - dataOffset) > 10240 ? 10240 : (fileData.length - dataOffset));
			short segSize = (short) (dataSize % 2 == 0 ? dataSize : dataSize + 1);

			segments_[i] = new CDFILESEGMENT();
			segments_[i].DataSize.set(dataSize);
			segments_[i].SegSize.set(segSize);
			segments_[i].setFileData(fileData);
		}
	}

	public ByteBuffer getBytes() {
		int totalSize = 0;
		totalSize += header_.getTotalSize();
		for (CDFILESEGMENT seg : segments_) {
			totalSize += seg.getTotalSize();
		}

		ByteBuffer result = ByteBuffer.allocate(totalSize);
		result.put(header_.getByteBuffer());
		for (CDFILESEGMENT seg : segments_) {
			result.put(seg.getByteBuffer());
		}
		return result;
	}

	public ByteBuffer getFileData() {
		ByteBuffer result = ByteBuffer.allocate((int) header_.FileDataSize.get());
		for (CDFILESEGMENT seg : segments_) {
			result.put(seg.getFileData());
		}
		return result;
	}
}
