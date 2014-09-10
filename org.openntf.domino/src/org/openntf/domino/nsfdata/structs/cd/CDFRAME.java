package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.COLOR_VALUE;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * Frame Name String and Target Name string follow the fixed portion of this CD Record. The strings are not null terminated. (fsods.h)
 * 
 * @since Lotus Notes/Domino 5.0.1
 *
 */
public class CDFRAME extends CDRecord {

	static {
		addFixed("Flags", Integer.class);
		addFixed("DataFlags", Short.class);
		addFixed("BorderEnable", Byte.class);
		addFixed("NoResize", Byte.class);
		addFixed("ScrollBarStyle", Short.class);
		addFixedUnsigned("MarginWidth", Byte.class);
		addFixedUnsigned("MarginHeight", Byte.class);
		addFixed("dwReserved", Integer.class);
		addFixedUnsigned("FrameNameLength", Short.class);
		addFixed("Reserved1", Short.class);
		addFixedUnsigned("FrameTargetLength", Short.class);
		addFixed("FrameBorderColor", COLOR_VALUE.class);
		addFixed("wReserved", Short.class);

		addVariableString("FrameName", "getFrameNameLength");
		addVariableString("FrameTarget", "getFrameTargetLength");

		// TODO add DataFlags extra data
	}

	public CDFRAME(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * Unused bits must be set to 0
	 * 
	 * @return fFRxxx
	 */
	public int getFlags() {
		// TODO make enum
		return (Integer) getStructElement("Flags");
	}

	/**
	 * In 6 this word is used to signify variable data follows the frame the data is in the order of the bits, i.e. 0x8000 is the first
	 * chunk of data the first word of each set of data is the size then the data
	 * 
	 * @return fFRNotesxxx
	 */
	public short getDataFlags() {
		// TODO make enum
		return (Short) getStructElement("DataFlags");
	}

	/**
	 * @return The FRAMEBORDER attribute for this Frame element
	 */
	public boolean getBorderEnable() {
		return (Byte) getStructElement("BorderEnable") != 0;
	}

	/**
	 * @return The NORESIZE attribute for this Frame element
	 */
	public boolean getNoResize() {
		return (Byte) getStructElement("NoResize") != 0;
	}

	/**
	 * @return The SCROLLING attribute for this frame element. Must be ALWAYS_ScrollStyle, NEVER_ScrollStyle or AUTO_ScrollStyle
	 */
	public short getScrollBarStyle() {
		// TODO make enum
		return (Short) getStructElement("ScrollBarStyle");
	}

	/**
	 * @return The MARGINWIDTH attribute for this frame element
	 */
	public short getMarginWidth() {
		return (Short) getStructElement("MarginWidth");
	}

	/**
	 * @return The MARGINHEIGHT attribute for this frame element
	 */
	public short getMarginHeight() {
		return (Short) getStructElement("MarginHeight");
	}

	/**
	 * Reserved for future use, must be 0
	 */
	public int getReserved1() {
		return (Integer) getStructElement("dwReserved");
	}

	/**
	 * @return Length of FrameName string that follows
	 */
	public int getFrameNameLength() {
		return (Integer) getStructElement("FrameNameLength");
	}

	public short getReserved2() {
		return (Short) getStructElement("Reserved1");
	}

	/**
	 * @return Length of default target frame name
	 */
	public int getFrameTargetLength() {
		return (Integer) getStructElement("FrameTargetLength");
	}

	/**
	 * @return The BORDERCOLOR attribute for this frame element
	 */
	public COLOR_VALUE getFrameBorderColor() {
		return (COLOR_VALUE) getStructElement("FrameBorderColor");
	}

	/**
	 * Reserved for future use, must be 0
	 */
	public short getReserved3() {
		return (Short) getStructElement("wReserved");
	}

	public String getFrameName() {
		return (String) getStructElement("FrameName");
	}

	public String getFrameTarget() {
		return (String) getStructElement("FrameTarget");
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Flags=" + getFlags() + ", DataFlags=" + getDataFlags() + ", BorderEnable="
				+ getBorderEnable() + ", NoResize=" + getNoResize() + ", ScrollBarStyle=" + getScrollBarStyle() + ", MarginWidth="
				+ getMarginWidth() + ", MarginHeight=" + getMarginHeight() + ", FrameBorderColor=" + getFrameBorderColor() + ", FrameName="
				+ getFrameName() + ", FrameTarget=" + getFrameTarget() + "]";
	}
}
