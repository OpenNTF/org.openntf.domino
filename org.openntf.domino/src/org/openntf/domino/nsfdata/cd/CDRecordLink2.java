package org.openntf.domino.nsfdata.cd;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.ODSUtils;

public class CDRecordLink2 extends CDRecord {
	private static final long serialVersionUID = 1L;

	public CDRecordLink2(final CDSignature signature, final ByteBuffer data, final int dataLength) {
		super(signature, data, dataLength);
	}

	public short getLinkId() {
		return getData().getShort(getData().position() + 0);
	}

	public String getComment() {
		ByteBuffer data = getData().duplicate();
		data.position(data.position()+2);
		// Now build an array until the first null byte
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte aByte;
		int breaker = 0;
		do {
			aByte = data.get();
			bos.write(aByte);
			if(breaker++ > 1000) {
				System.out.println("we dug too deep!");
				return "";
			}
		} while(aByte != 0);
		return ODSUtils.fromLMBCS(bos.toByteArray());
	}
	public String getHint() {
		// Seek to the first null byte (the end of Comment)
		ByteBuffer data = getData().duplicate();
		data.position(data.position()+2);
		byte aByte;
		int breaker = 0;
		do {
			aByte = data.get();
			if(breaker++ > 1000) {
				System.out.println("we dug too deep!");
				return "";
			}
		} while(aByte != 0);

		// Now we're past comment. Time to read in Hint
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		breaker = 0;
		do {
			aByte = data.get();
			bos.write(aByte);
			if(breaker++ > 1000) {
				System.out.println("we dug too deep!");
				return "";
			}
		} while(aByte != 0);
		return ODSUtils.fromLMBCS(bos.toByteArray());
	}
	public String getAnchor() {
		// TODO make this work - namely, figure out how to know when it's present
		// Seek to the first null byte (the end of Comment)
		ByteBuffer data = getData().duplicate();
		data.position(data.position()+2);
		byte aByte;
		int breaker = 0;
		do {
			aByte = data.get();
			if(breaker++ > 1000) {
				System.out.println("we dug too deep!");
				return "";
			}
		} while(aByte != 0);

		// Now seek to the second null byte (end of Hint)
		data.position(data.position()+2);
		breaker = 0;
		do {
			aByte = data.get();
			if(breaker++ > 1000) {
				System.out.println("we dug too deep!");
				return "";
			}
		} while(aByte != 0);

		// Now we're past Hint. Time to read in Anchor
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		breaker = 0;
		do {
			aByte = data.get();
			bos.write(aByte);
			if(breaker++ > 1000) {
				System.out.println("we dug too deep!");
				return "";
			}
		} while(aByte != 0);
		return ODSUtils.fromLMBCS(bos.toByteArray());
	}

	@Override
	public int getExtraLength() {
		return getDataLength() % 2;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ", Link ID: " + getLinkId() + ", Comment: " + getComment() + ", Hint: " + getHint() + ", Anchor: " + getAnchor() + "]";
	}
}
