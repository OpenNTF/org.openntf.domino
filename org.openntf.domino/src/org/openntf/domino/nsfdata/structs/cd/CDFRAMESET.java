package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.openntf.domino.nsfdata.structs.COLOR_VALUE;
import org.openntf.domino.nsfdata.structs.FRAMESETLENGTH;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * A FRAMESETLENGTH structure will follow depending on the value found in either RowQty or ColQty. There could be multiple FRAMESETLENGTH
 * structures defining the RowQty and ColQty values. (fsods.h)
 * 
 * @since Lotus Notes/Domino 5.0.1
 *
 */
public class CDFRAMESET extends CDRecord {

	static {
		addFixed("Flags", Integer.class);
		addFixed("BorderEnable", Byte.class);
		addFixed("byAvail1", Byte.class);
		addFixed("Reserved1", Short.class);
		addFixed("Reserved2", Short.class);
		addFixedUnsigned("FrameBorderWidth", Short.class);
		addFixed("Reserved3", Short.class);
		addFixedUnsigned("FrameSpacingWidth", Short.class);
		addFixed("Reserved4", Short.class);
		addFixed("ReservedColor1", COLOR_VALUE.class);
		addFixed("ReservedColro2", COLOR_VALUE.class);
		addFixedUnsigned("RowQty", Short.class);
		addFixedUnsigned("ColQty", Short.class);
		addFixed("Reserved5", Short.class);
		addFixed("Reserved6", Short.class);
		addFixed("FrameBorderColor", COLOR_VALUE.class);
		addFixed("ThemeSetting", Byte.class);
		addFixed("Reserved7", Byte.class);

		addVariableArray("Rows", "getRowQty", FRAMESETLENGTH.class);
		addVariableArray("Cols", "getColQty", FRAMESETLENGTH.class);
	}

	public CDFRAMESET(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return fFSxxxxxxx as defined below. Unused bits must be set to 0
	 */
	public int getFlags() {
		// TODO make enum
		return (Integer) getStructElement("Flags");
	}

	/**
	 * @return HTML FRAMEBORDER attribute
	 */
	public boolean getBorderEnable() {
		return (Byte) getStructElement("BorderEnable") != 0;
	}

	/**
	 * Reserved, must be 0
	 */
	public byte getAvailable1() {
		return (Byte) getStructElement("byAvail1");
	}

	/**
	 * Reserved, must be 0
	 */
	public short getReserved1() {
		return (Short) getStructElement("Reserved1");
	}

	/**
	 * Reserved, must be 0
	 */
	public short getReserved2() {
		return (Short) getStructElement("Reserved2");
	}

	/**
	 * @return HTML BORDER attribute
	 */
	public int getFrameBorderWidth() {
		return (Integer) getStructElement("FrameBorderWidth");
	}

	/**
	 * Reserved, must be 0
	 */
	public short getReserved3() {
		return (Short) getStructElement("Reserved3");
	}

	/**
	 * @return HTML FRAMESPACING attribute
	 */
	public int getFrameSpacingWidth() {
		return (Integer) getStructElement("FrameSpacingWidth");
	}

	/**
	 * Reserved, must be 0
	 */
	public short getReserved4() {
		return (Short) getStructElement("Reserved4");
	}

	/**
	 * reserved for future use
	 */
	public COLOR_VALUE getReservedColor1() {
		return (COLOR_VALUE) getStructElement("ReservedColor1");
	}

	/**
	 * reserved for future use
	 */
	public COLOR_VALUE getReservedColor2() {
		return (COLOR_VALUE) getStructElement("ReservedColor2");
	}

	/**
	 * @return The number of FRAMESETLENGTH structures defining row information
	 */
	public int getRowQty() {
		return (Integer) getStructElement("RowQty");
	}

	/**
	 * @return The number of FRAMESETLENGTH structures defining column information
	 */
	public int getColQty() {
		return (Integer) getStructElement("ColQty");
	}

	/**
	 * Reserved, must be 0
	 */
	public short getReserved5() {
		return (Short) getStructElement("Reserved5");
	}

	/**
	 * Reserved, must be 0
	 */
	public short getReserved6() {
		return (Short) getStructElement("Reserved6");
	}

	/**
	 * @return HTML BORDERCOLOR attribute
	 */
	public COLOR_VALUE getFrameBorderColor() {
		return (COLOR_VALUE) getStructElement("FrameBorderColor");
	}

	/**
	 * @return Theme Setting
	 * @since IBM Notes/Domino 8.5
	 */
	public byte getThemeSetting() {
		// TODO make enum?
		return (Byte) getStructElement("ThemeSetting");
	}

	/**
	 * Reserved, must be 0
	 */
	public byte getReserved7() {
		return (Byte) getStructElement("Reserved7");
	}

	public FRAMESETLENGTH[] getRows() {
		return (FRAMESETLENGTH[]) getStructElement("Rows");
	}

	public FRAMESETLENGTH[] getCols() {
		return (FRAMESETLENGTH[]) getStructElement("Cols");
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ", BorderEnable: " + getBorderEnable() + ", FrameBorderWidth: " + getFrameBorderWidth()
				+ ", FrameSpacingWidth: " + getFrameSpacingWidth() + ", FrameBorderColor: " + getFrameBorderColor() + ", ThemeSetting: "
				+ getThemeSetting() + ", RowQty: " + getRowQty() + ", ColQty: " + getColQty() + ", Rows: " + Arrays.asList(getRows())
				+ ", Cols: " + Arrays.asList(getCols()) + "]";
	}
}
