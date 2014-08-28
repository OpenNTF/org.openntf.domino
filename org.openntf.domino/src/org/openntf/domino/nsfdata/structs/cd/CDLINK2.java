package org.openntf.domino.nsfdata.structs.cd;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure implements a document link in a rich text field. It contains an index into a Doc Link Reference List. A Doc Link Reference
 * (a NOTELINK structure) contains all the information necessary to open the specified document from any database on any server. (editods.h)
 *
 */
public class CDLINK2 extends CDRecord {

	static {
		addFixed("LinkID", Short.class);

		// TODO add null-terminated-string support
	}

	protected CDLINK2(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return ID of the link
	 */
	public short getLinkId() {
		return (Short) getStructElement("LinkID");
	}

	/**
	 * @return Display comment
	 */
	public String getComment() {
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 2);
		// Now build an array until the first null byte
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte aByte;
		int breaker = 0;
		do {
			aByte = data.get();
			bos.write(aByte);
			if (breaker++ > 1000) {
				System.out.println("we dug too deep!");
				return "";
			}
		} while (aByte != 0);
		return ODSUtils.fromLMBCS(bos.toByteArray());
	}

	/**
	 * @return Server "hint"
	 */
	public String getHint() {
		// Seek to the first null byte (the end of Comment)
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 2);
		byte aByte;
		int breaker = 0;
		do {
			aByte = data.get();
			if (breaker++ > 1000) {
				System.out.println("we dug too deep!");
				return "";
			}
		} while (aByte != 0);

		// Now we're past comment. Time to read in Hint
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		breaker = 0;
		do {
			aByte = data.get();
			bos.write(aByte);
			if (breaker++ > 1000) {
				System.out.println("we dug too deep!");
				return "";
			}
		} while (aByte != 0);
		return ODSUtils.fromLMBCS(bos.toByteArray());
	}

	/**
	 * @return Anchor text (optional)
	 */
	public String getAnchor() {
		// TODO make this work - namely, figure out how to know when it's present
		// Seek to the first null byte (the end of Comment)
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 2);
		byte aByte;
		int breaker = 0;
		do {
			aByte = data.get();
			if (breaker++ > 1000) {
				System.out.println("we dug too deep!");
				return "";
			}
		} while (aByte != 0);

		// Now seek to the second null byte (end of Hint)
		data.position(data.position() + 2);
		breaker = 0;
		do {
			aByte = data.get();
			if (breaker++ > 1000) {
				System.out.println("we dug too deep!");
				return "";
			}
		} while (aByte != 0);

		// Now we're past Hint. Time to read in Anchor
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		breaker = 0;
		do {
			aByte = data.get();
			bos.write(aByte);
			if (breaker++ > 1000) {
				System.out.println("we dug too deep!");
				return "";
			}
		} while (aByte != 0);
		return ODSUtils.fromLMBCS(bos.toByteArray());
	}

	@Override
	public int getExtraLength() {
		return (int) (getDataLength() % 2);
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ", Link ID: " + getLinkId() + ", Comment: " + getComment() + ", Hint: " + getHint()
				+ ", Anchor: " + getAnchor() + "]";
	}
}
