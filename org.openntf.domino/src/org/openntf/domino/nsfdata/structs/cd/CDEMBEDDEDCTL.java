package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD record may further define attributes within a CDFIELD such as tab order.
 * 
 * @since Lotus Notes/Domino 5.0
 */
public class CDEMBEDDEDCTL extends CDRecord {

	public CDEMBEDDEDCTL(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return Embedded control Style, see EC_STYLE_xxx
	 */
	public int getCtlStyle() {
		// TODO make enum
		return getData().getInt(getData().position() + 0);
	}

	/**
	 * @return Embedded control Flags, see EC_FLAG_xxx
	 */
	public short getFlags() {
		// TODO make enum
		return getData().getShort(getData().position() + 4);
	}

	/**
	 * @return Width of embedded control
	 */
	public short getWidth() {
		return getData().getShort(getData().position() + 6);
	}

	/**
	 * @return Height of embedded control
	 */
	public short getHeight() {
		return getData().getShort(getData().position() + 8);
	}

	/**
	 * @return Embedded control version, see EMBEDDEDCTL_VERSIONxxx
	 */
	public short getVersion() {
		// TODO make enum
		return getData().getShort(getData().position() + 10);
	}

	/**
	 * @return Embedded control type, see EMBEDDEDCTL_xxx
	 */
	public short getCtlType() {
		// TODO make enum
		return getData().getShort(getData().position() + 12);
	}

	public short getMaxChars() {
		return getData().getShort(getData().position() + 14);
	}

	public short getMaxLines() {
		return getData().getShort(getData().position() + 16);
	}

	public short getPercentage() {
		return getData().getShort(getData().position() + 18);
	}

	public int[] getSpare() {
		int[] result = new int[3];
		result[0] = getData().getInt(getData().position() + 20);
		result[1] = getData().getInt(getData().position() + 24);
		result[2] = getData().getInt(getData().position() + 28);
		return result;
	}
}
