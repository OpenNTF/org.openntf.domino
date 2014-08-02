package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * The designer of a form or view may define custom actions for that form or view. The attributes for the button bar are stored in the
 * CDACTIONBAR record in the $ACTIONS and/or $V5ACTIONS item for the design note describing the form or view. (actods.h)
 * 
 * @author jgallagher
 * @since Lotus Notes 4.5
 */
public class CDACTIONBAR extends CDRecord {

	public CDACTIONBAR(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return Background color index
	 */
	public short getBackColor() {
		// TODO map to color
		return getData().getShort(getData().position() + 0);
	}

	/**
	 * @return Line color index
	 */
	public short getLineColor() {
		// TODO map to color
		return getData().getShort(getData().position() + 2);
	}

	/**
	 * @return Style of line
	 */
	public short getLineStyle() {
		// TODO create enum
		return getData().getShort(getData().position() + 4);
	}

	/**
	 * @return Border style
	 */
	public short getBorderStyle() {
		// TODO create enum
		return getData().getShort(getData().position() + 6);
	}

	/**
	 * @return Border width (twips)
	 */
	public short getBorderWidth() {
		return getData().getShort(getData().position() + 8);
	}

	public int getFlags() {
		// TODO create enum
		return getData().getInt(getData().position() + 10);
	}

	/**
	 * @return ID of Shared Action
	 */
	public int getShareId() {
		return getData().getInt(getData().position() + 14);
	}

	public int getFontId() {
		// TODO map to font
		return getData().getInt(getData().position() + 18);
	}

	/**
	 * @return Height of the Button
	 */
	public short getBtnHeight() {
		return getData().getShort(getData().position() + 22);
	}

	/**
	 * @return Height spacing
	 */
	public short getHeightSpc() {
		return getData().getShort(getData().position() + 24);
	}
}
