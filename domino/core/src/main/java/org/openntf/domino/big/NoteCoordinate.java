package org.openntf.domino.big;

import java.io.Externalizable;

import org.openntf.domino.Document;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.utils.DominoUtils;

import com.google.common.base.Strings;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Longs;
import com.google.common.primitives.UnsignedLongs;

public interface NoteCoordinate extends Externalizable, Comparable<NoteCoordinate> {
	public static enum Utils {
		;
		public static boolean isNoteCoordinate(final CharSequence chars) {
			return chars.length() == 48 && DominoUtils.isHex(chars);
		}

		public static String getUnidFromBytes(final byte[] bytes) {
			if (bytes.length >= 16) {
				long first = Longs.fromBytes(bytes[0], bytes[1], bytes[2], bytes[3], bytes[4], bytes[5], bytes[6], bytes[7]);
				long last = Longs.fromBytes(bytes[8], bytes[9], bytes[10], bytes[11], bytes[12], bytes[13], bytes[14], bytes[15]);
				return getUnidFromLongs(first, last);
			} else {
				//TODO NTF Something here...
				throw new IllegalArgumentException("Cannot convert a byte array of length " + bytes.length + " to a unid");
			}
		}

		public static String getUnidFromLongs(final long first, final long last) {
			String fStr = Long.toHexString(first).toUpperCase();
			String lStr = Long.toHexString(last).toUpperCase();
			return Strings.padStart(fStr, 16, '0') + Strings.padStart(lStr, 16, '0');
		}

		public static String getReplidFromBytes(final byte[] bytes) {
			if (bytes.length >= 8) {
				long l = Longs.fromByteArray(bytes);
				return getReplidFromLong(l);
			} else {
				//TODO NTF Something here...
				throw new IllegalArgumentException("Cannot convert a byte array of length " + bytes.length + " to a replica id");
			}
		}

		public static String getReplidFromLong(final long l) {
			String c = Long.toHexString(l).toUpperCase();
			return Strings.padStart(c, 16, '0');
		}

		public static byte[] getBytesFromDocument(final Document doc) {
			byte[] result = new byte[24];
			byte[] replbytes = getBytesFromReplid(doc.getAncestorDatabase().getReplicaID());
			byte[] unidbytes = getBytesFromUnid(doc.getUniversalID());
			System.arraycopy(replbytes, 0, result, 0, 8);
			System.arraycopy(unidbytes, 0, result, 8, 16);
			return result;
		}

		public static byte[] getBytesFromUnid(final CharSequence unid) {
			if (unid == null)
				return null;
			if (DominoUtils.isUnid(unid)) {
				String first = "0x" + unid.subSequence(0, 16);
				long flong = UnsignedLongs.decode(first);
				byte[] fbytes = Longs.toByteArray(flong);
				String last = "0x" + unid.subSequence(16, 32);
				long llong = UnsignedLongs.decode(last);
				byte[] lbytes = Longs.toByteArray(llong);
				return Bytes.concat(fbytes, lbytes);
			} else {
				throw new IllegalArgumentException("Cannot convert a String of length " + unid.length() + ": " + unid);
			}
		}

		public static byte[] getBytesFromReplid(final CharSequence replid) throws IllegalArgumentException {
			long l = getLongFromReplid(replid);
			return Longs.toByteArray(l);
		}

		public static long getLongFromReplid(final CharSequence replid) throws IllegalArgumentException {
			if (replid == null)
				throw new IllegalArgumentException("null is not a valid replica id");
			if (DominoUtils.isReplicaId(replid)) {
				String decode = "0x" + replid.toString();
				long result = UnsignedLongs.decode(decode);
				return result;
			} else {
				throw new IllegalArgumentException("Cannot convert a String of length " + replid.length() + ": " + replid);
			}
		}

		public static long[] getLongsFromUnid(final CharSequence unid) throws IllegalArgumentException {
			if (unid == null)
				throw new IllegalArgumentException("null is not a valid unid");
			if (DominoUtils.isUnid(unid)) {
				long[] result = new long[2];
				String first = "0x" + unid.subSequence(0, 16);
				result[0] = UnsignedLongs.decode(first);
				String last = "0x" + unid.subSequence(16, 32);
				result[1] = UnsignedLongs.decode(last);
				return result;
			} else {
				throw new IllegalArgumentException("Cannot convert a String of length " + unid.length() + ": " + unid);
			}
		}

		public static void insertByteArray(long value, final byte[] bytes, final int start) {
			for (int i = 7; i >= 0; --i) {
				bytes[i + start] = (byte) (int) (value & 0xFF);
				value >>= 8;
			}
		}

		public static org.openntf.domino.big.impl.NoteCoordinate getNoteCoordinate(final NoteCollection notecoll, final String nid) {
			return new org.openntf.domino.big.impl.NoteCoordinate(notecoll, nid);
		}

		public static org.openntf.domino.big.impl.NoteCoordinate getNoteCoordinate(final Document doc) {
			return new org.openntf.domino.big.impl.NoteCoordinate(doc);
		}

		public static org.openntf.domino.big.impl.NoteCoordinate getNoteCoordinate(final CharSequence replicaid, final CharSequence unid) {
			return new org.openntf.domino.big.impl.NoteCoordinate(replicaid, unid);
		}

		public static org.openntf.domino.big.impl.NoteCoordinate getNoteCoordinate(final CharSequence metaversalid) {
			if (metaversalid.length() == 48) {
				return new org.openntf.domino.big.impl.NoteCoordinate(metaversalid);
			} else {
				return null;
			}
		}

		public static org.openntf.domino.big.impl.NoteCoordinate getNoteCoordinate(final byte[] bytes) {
			return new org.openntf.domino.big.impl.NoteCoordinate(bytes);
		}

		public static org.openntf.domino.big.impl.NoteCoordinate getNoteCoordinate(final long db, final long x, final long y) {
			return new org.openntf.domino.big.impl.NoteCoordinate(db, x, y);
		}
	}

	public String getReplicaId();

	public Long getReplicaLong();

	public long getX();

	public long getY();

	public String getUNID();

	public Document getDocument();

	public Document getDocument(final String serverName);

	public Object get(final String key);

	public boolean isView();

	public boolean isIcon();

	public int insertToByteArray(final byte[] bytes, final int pos);

	public byte[] toByteArray();
}
