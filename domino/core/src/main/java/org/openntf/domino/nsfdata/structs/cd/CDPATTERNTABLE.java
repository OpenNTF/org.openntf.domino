package org.openntf.domino.nsfdata.structs.cd;

import java.awt.Color;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import org.openntf.domino.nsfdata.structs.LSIG;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * A pattern table is one of the optional records following a CDBITMAPHEADER record. The pattern table is used to compress repetitive bitmap
 * data. (editods.h)
 *
 */
public class CDPATTERNTABLE extends CDRecord {

	public final LSIG Header = inner(new LSIG());

	@Override
	public SIG getHeader() {
		return Header;
	}

	public int getPatternCount() {
		return (int) ((Header.getRecordLength() - Header.size()) / 24);
	}

	public Pattern[] getPatterns() {
		int count = getPatternCount();
		Pattern[] result = new Pattern[count];
		for (int i = 0; i < count; i++) {
			ByteBuffer data = getData().duplicate();
			data.order(ByteOrder.LITTLE_ENDIAN);
			data.position(data.position() + size());
			data.position(data.position() + (i * 24));
			data.limit(data.position() + 24);
			result[i] = new Pattern(data);
		}
		return result;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ", Patterns: " + Arrays.asList(getPatterns()) + "]";
	}

	/**
	 * The pattern table consists of a record header, which identifies this as a Pattern Table record, followed by up to 64 pattern table
	 * entries. The number of pattern table entries is specified in the PatternCount field of the CDBITMAPHEADER record.
	 * 
	 * Each pattern table entry consists of a fixed number of bytes; the number of bytes in each entry that is actually used will depend on
	 * the color representation used for the bitmap. There is space in each entry for up to three bytes for each picture element. Since the
	 * constant PELS_PER_PATTERN is currently defined to be 8, this means that each entry in the pattern table will occupy 24 bytes.
	 * 
	 * If the bitmap uses the 8-bit mapped color representation, the bitmap contains less than 256 different colors. Each picture element is
	 * represented as a single byte containing an index into the color table. In this case, only the first 8 bytes of each 24-byte pattern
	 * table entry will be occupied, and the remaining 16 bytes are ignored.
	 * 
	 * If the bitmap uses an RGB color representation, each picture element is represented by 3 bytes, Red (byte 0), Green (byte 1), and
	 * Blue (byte 2). Each byte is a color value in the range 0 to 255. (This is the same color entry format used in a CDCOLORTABLE). In
	 * this case, all 24 bytes of each pattern table entry will be used. (editods.h)
	 * 
	 * @author jgallagher
	 *
	 */
	public static class Pattern implements Externalizable {
		private byte[] data_;

		public Pattern(final byte[] data) {
			data_ = Arrays.copyOf(data, data.length);
		}

		public Pattern(final ByteBuffer data) {
			ByteBuffer localData = data.duplicate();
			int length = localData.limit() - localData.position();
			data_ = new byte[length];
			localData.get(data_);
		}

		public byte[] getData() {
			return Arrays.copyOf(data_, data_.length);
		}

		public int[] getPixelsBit() {
			int[] result = new int[8];
			for (int i = 0; i < 8; i++) {
				result[i] = data_[i] & 0xFF;
			}
			return result;
		}

		public Color[] getPixels24Bit() {
			Color[] result = new Color[8];
			ByteBuffer data = ByteBuffer.wrap(data_);
			data.order(ByteOrder.LITTLE_ENDIAN);
			for (int i = 0; i < 8; i++) {
				int r = data.getInt();
				int g = data.getInt();
				int b = data.getInt();
				result[i] = new Color(r, g, b);
			}
			return result;
		}

		@Override
		public String toString() {
			return "[" + getClass().getSimpleName() + "]";
		}

		@Override
		public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
			int length = in.readInt();
			data_ = new byte[length];
			in.read(data_);
		}

		@Override
		public void writeExternal(final ObjectOutput out) throws IOException {
			out.write(data_.length);
			out.write(data_);
		}
	}
}
