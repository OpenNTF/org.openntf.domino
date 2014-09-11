package org.openntf.domino.nsfdata.structs;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Formatter;

/**
 * <p>
 * The Universal Note ID (UNID) identifies all copies of the same note in different replicas of the same database universally (across all
 * servers). If one note in one database has the same UNID as another note in a replica database, then the two notes are replicas of each
 * other.
 * </p>
 * 
 * <p>
 * The UNID is used to reference a specific note from another note. Specifically, the FIELD_LINK ($REF) field of a response note contains
 * the UNID of it's parent. Similarly, Doc Links (see NOTELINK) contains the UNID of the linked-to note plus the database ID where the
 * linked-to note can be found. The important characteristic of the UNID is that it continues to reference a specific note even if the note
 * being referenced is updated.
 * </p>
 * 
 * <p>
 * The Domino replicator uses the Universal Note ID to match the notes in one database with their respective copies in replica databases.
 * For example, if database A is a replica copy of database B, database A contains a note with a particular UNID, and database B contains a
 * note with the same UNID, then the replicator concludes that these two notes are replica copies of one another. On the other hand, if
 * database A contains a note with a particular UNID but database B does not, then the replicator will create a copy of that note and add it
 * to database B.
 * </p>
 * 
 * <p>
 * One database must never contain two notes with the same UNID. If the replicator finds two notes with the same UNID in the same database,
 * it generates an error message in the log and does not replicate the document.
 * </p>
 * 
 * <p>
 * The "File" member of the UNID contains a number derived in different ways depending on the release of Domino or Notes. Pre- 2.1 versions
 * of Notes set the "File" member to the creation timedate of the NSF file in which the note is created. Notes 2.1 sets the "File" member to
 * a user-unique identifier, derived in part from information in the ID of the user creating the note, and in part from the database where
 * the note is created. Notes 3.0 sets the "File" member to a random number generated at the time the note is created.
 * </p>
 * 
 * <p>
 * The "Note" member of the UNID contains the date/time when the very first copy of the note was stored into the first NSF (Note: date/time
 * from $CREATED item, if exists, takes precedence).
 * </p>
 * 
 * <p>
 * (nsfdata.h)
 * </p>
 *
 */
public class UNIVERSALNOTEID extends AbstractStruct {
	public static final int SIZE = TIMEDATE.SIZE * 2;

	static {
		addFixed("File", Long.class);
		addFixed("Note", Long.class);
	}

	public static UNIVERSALNOTEID fromString(final String hexString) {
		if (hexString == null) {
			byte[] nullData = new byte[16];
			return new UNIVERSALNOTEID(ByteBuffer.wrap(nullData).order(ByteOrder.LITTLE_ENDIAN));
		}

		int len = hexString.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
		}
		// This is big endian because Java is internally, apparently
		ByteBuffer longBuffer = ByteBuffer.wrap(data).order(ByteOrder.BIG_ENDIAN);

		long[] longs = new long[2];
		longs[0] = longBuffer.getLong();
		longs[1] = longBuffer.getLong();
		return fromLongs(longs);
	}

	public static UNIVERSALNOTEID fromLongs(final long[] value) {
		if (value == null) {
			throw new IllegalArgumentException("value cannot be null");
		}
		if (value.length != 2) {
			throw new IllegalArgumentException("value length must be 2");
		}
		byte[] newData = new byte[16];
		ByteBuffer data = ByteBuffer.wrap(newData).order(ByteOrder.LITTLE_ENDIAN);
		for (int i = 0; i < value.length; i++) {
			data.putLong(value[i]);
		}
		data.position(0);
		return new UNIVERSALNOTEID(data);
	}

	public UNIVERSALNOTEID() {
		super();
	}

	public UNIVERSALNOTEID(final ByteBuffer data) {
		super(data);
	}

	@Override
	public long getStructSize() {
		return SIZE;
	}

	public void setFile(final long value) {
		setStructElement("File", value);
	}

	public void setNote(final long value) {
		setStructElement("Note", value);
	}

	public String getStringValue() {
		Formatter formatter = new Formatter();
		//		byte[] data = new byte[getData().limit() - getData().position()];
		ByteBuffer data = getData().duplicate().order(ByteOrder.LITTLE_ENDIAN);
		formatter.format("%16x", data.getLong());
		formatter.format("%16x", data.getLong());
		String result = formatter.toString();
		formatter.close();

		return result.toUpperCase();
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": " + getStringValue() + "]";
	}
}
