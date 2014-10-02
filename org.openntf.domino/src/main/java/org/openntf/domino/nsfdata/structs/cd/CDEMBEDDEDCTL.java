package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This CD record may further define attributes within a CDFIELD such as tab order.
 * 
 * @since Lotus Notes/Domino 5.0
 */
public class CDEMBEDDEDCTL extends CDRecord {

	static {
		addFixed("CtlStyle", Integer.class);
		addFixed("Flags", Short.class);
		addFixedUnsigned("Width", Short.class);
		addFixedUnsigned("Height", Short.class);
		addFixed("Version", Short.class);
		addFixed("CtlType", Short.class);
		addFixedUnsigned("MaxChars", Short.class);
		addFixedUnsigned("MaxLines", Short.class);
		addFixedUnsigned("Percentage", Short.class);
		addFixedArray("Spare", Integer.class, 3);
	}
	public static final int SIZE = getFixedStructSize();

	public CDEMBEDDEDCTL(final CDSignature cdSig) {
		super(new WSIG(cdSig, cdSig.getSize() + SIZE), ByteBuffer.wrap(new byte[SIZE]));
	}

	public CDEMBEDDEDCTL(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return Embedded control Style, see EC_STYLE_xxx
	 */
	public int getCtlStyle() {
		// TODO make enum
		return (Integer) getStructElement("CtlStyle");
	}

	/**
	 * @return Embedded control Flags, see EC_FLAG_xxx
	 */
	public short getFlags() {
		// TODO make enum
		return (Short) getStructElement("Flags");
	}

	/**
	 * @return Width of embedded control
	 */
	public int getWidth() {
		return (Integer) getStructElement("Width");
	}

	/**
	 * @return Height of embedded control
	 */
	public int getHeight() {
		return (Integer) getStructElement("Height");
	}

	/**
	 * @return Embedded control version, see EMBEDDEDCTL_VERSIONxxx
	 */
	public short getVersion() {
		// TODO make enum
		return (Short) getStructElement("Version");
	}

	/**
	 * @return Embedded control type, see EMBEDDEDCTL_xxx
	 */
	public short getCtlType() {
		// TODO make enum
		return (Short) getStructElement("CtlType");
	}

	public int getMaxChars() {
		return (Integer) getStructElement("MaxChars");
	}

	public int getMaxLines() {
		return (Integer) getStructElement("MaxLines");
	}

	public int getPercentage() {
		return (Integer) getStructElement("Percentage");
	}

	public int[] getSpare() {
		return (int[]) getStructElement("Spare");
	}
}
