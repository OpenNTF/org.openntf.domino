package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.LENGTH_VALUE;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This CD record contains position information for a layer box. (editods.h)
 * 
 * @since Lotus Notes/Domino 6.0
 *
 */
public class CDPOSITIONING extends CDRecord {

	public static enum Scheme {
		STATIC((byte) 0), ABSOLUTE((byte) 1), RELATIVE((byte) 2), FIXED((byte) 3);

		private final byte value_;

		private Scheme(final byte value) {
			value_ = value;
		}

		public byte getValue() {
			return value_;
		}

		public static Scheme valueOf(final byte typeCode) {
			for (Scheme type : values()) {
				if (type.getValue() == typeCode) {
					return type;
				}
			}
			throw new IllegalArgumentException("No matching Scheme found for type code " + typeCode);
		}
	}

	static {
		addFixed("Scheme", Byte.class);
		addFixed("bReserved", Byte.class);
		addFixed("ZIndex", Integer.class);
		addFixed("Top", LENGTH_VALUE.class);
		addFixed("Left", LENGTH_VALUE.class);
		addFixed("Bottom", LENGTH_VALUE.class);
		addFixed("Right", LENGTH_VALUE.class);
		addFixed("BrowserLeftOffset", Double.class);
		addFixed("BrowserRightOffset", Double.class);
	}

	public static final int SIZE = getFixedStructSize();

	public CDPOSITIONING(final CDSignature cdSig) {
		super(new WSIG(cdSig, cdSig.getSize() + SIZE), ByteBuffer.wrap(new byte[SIZE]));
	}

	public CDPOSITIONING(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public Scheme getScheme() {
		return Scheme.valueOf((Byte) getStructElement("Scheme"));
	}

	public int getZIndex() {
		return (Integer) getStructElement("ZIndex");
	}

	public LENGTH_VALUE getTop() {
		return (LENGTH_VALUE) getStructElement("Top");
	}

	public LENGTH_VALUE getLeft() {
		return (LENGTH_VALUE) getStructElement("Left");
	}

	public LENGTH_VALUE getBottom() {
		return (LENGTH_VALUE) getStructElement("Bottom");
	}

	public LENGTH_VALUE getRight() {
		return (LENGTH_VALUE) getStructElement("Right");
	}

	public double getBrowserLeftOffset() {
		return (Double) getStructElement("BrowserLeftOffset");
	}

	public double getBrowserRightOffset() {
		return (Double) getStructElement("BrowserRightOffset");
	}
}
