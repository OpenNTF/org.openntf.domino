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
	 * Creates a CDResourceFile wrapping the underlying byte array. Until the file is modified, it does <b>not</b> copy the bytes, so any
	 * modifications to the underlying array will also modify this data.
	 * 
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
		header_.init();
		header_.Header.setSigIdentifier(CDSignature.FILEHEADER.getBaseValue());
		header_.setFileExt(fileExt);
		header_.Header.setRecordLength(header_.size() + header_.getVariableSize());
		header_.FileDataSize.set(0);
		header_.SegCount.set(0);
		segments_ = new CDFILESEGMENT[0];
	}

	public void setFileData(final byte[] fileData) {
		header_.FileDataSize.set(fileData.length);

		// Determine how many file segments will be needed based on the inferred size cap
		int chunks = fileData.length / SEGMENT_SIZE_CAP;
		if (fileData.length % SEGMENT_SIZE_CAP > 0) {
			chunks++;
		}
		header_.SegCount.set(chunks);
		segments_ = new CDFILESEGMENT[chunks];

		for (int i = 0; i < chunks; i++) {
			// Each chunk begins with a CDFILESEGMENT

			// Figure out our data and segment sizes
			int dataOffset = SEGMENT_SIZE_CAP * i;
			short dataSize = (short) ((fileData.length - dataOffset) > SEGMENT_SIZE_CAP ? SEGMENT_SIZE_CAP : (fileData.length - dataOffset));
			short segSize = (short) (dataSize % 2 == 0 ? dataSize : dataSize + 1);

			segments_[i] = new CDFILESEGMENT();
			segments_[i].init();
			segments_[i].Header.setSigIdentifier(CDSignature.FILESEGMENT.getBaseValue());
			segments_[i].SegSize.set(segSize);
			segments_[i].setFileData(fileData);
			segments_[i].Header.setRecordLength(segments_[i].size() + segments_[i].getVariableSize());
		}
	}

	public ByteBuffer getData() {
		int totalSize = 0;
		totalSize += header_.getTotalSize();
		for (CDFILESEGMENT seg : segments_) {
			totalSize += seg.getTotalSize();
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

	public ByteBuffer getFileData() {
		ByteBuffer result = ByteBuffer.allocate((int) header_.FileDataSize.get());
		for (CDFILESEGMENT seg : segments_) {
			result.put(seg.getFileData());
		}
		return result;
	}
}
