package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.COLOR_VALUE;
import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * Frame Name String and Target Name string follow the fixed portion of this CD Record. The strings are not null terminated. (fsods.h)
 * 
 * @author jgallagher
 * @since Lotus Notes/Domino 5.0.1
 *
 */
public class CDFRAME extends CDRecord {

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
		return getData().getInt(getData().position() + 0);
	}

	/**
	 * In 6 this word is used to signify variable data follows the frame the data is in the order of the bits, i.e. 0x8000 is the first
	 * chunk of data the first word of each set of data is the size then the data
	 * 
	 * @return fFRNotesxxx
	 */
	public short getDataFlags() {
		// TODO make enum
		return getData().getShort(getData().position() + 4);
	}

	/**
	 * @return The FRAMEBORDER attribute for this Frame element
	 */
	public boolean getBorderEnable() {
		return getData().get(getData().position() + 6) != 0;
	}

	/**
	 * @return The NORESIZE attribute for this Frame element
	 */
	public boolean getNoResize() {
		return getData().get(getData().position() + 7) != 0;
	}

	/**
	 * @return The SCROLLING attribute for this frame element. Must be ALWAYS_ScrollStyle, NEVER_ScrollStyle or AUTO_ScrollStyle
	 */
	public short getScrollBarStyle() {
		// TODO make enum
		return getData().getShort(getData().position() + 8);
	}

	/**
	 * @return The MARGINWIDTH attribute for this frame element
	 */
	public int getMarginWidth() {
		return getData().getShort(getData().position() + 10) & 0xFFFF;
	}

	/**
	 * @return The MARGINHEIGHT attribute for this frame element
	 */
	public int getMarginHeight() {
		return getData().getShort(getData().position() + 12) & 0xFFFF;
	}

	/**
	 * Reserved for future use, must be 0
	 */
	public int getReserved1() {
		return getData().getInt(getData().position() + 14);
	}

	/**
	 * @return Length of FrameName string that follows
	 */
	public int getFrameNameLength() {
		return getData().getShort(getData().position() + 16) & 0xFFFF;
	}

	public short getReserved2() {
		return getData().getShort(getData().position() + 18);
	}

	/**
	 * @return Length of default target frame name
	 */
	public int getFrameTargetLength() {
		return getData().getShort(getData().position() + 20) & 0xFFFF;
	}

	/**
	 * @return The BORDERCOLOR attribute for this frame element
	 */
	public COLOR_VALUE getFrameBorderColor() {
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 22);
		data.limit(data.limit() + 6);
		return new COLOR_VALUE(data);
	}

	/**
	 * Reserved for future use, must be 0
	 */
	public short getReserved3() {
		return getData().getShort(getData().position() + 28);
	}

	public String getFrameName() {
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 30);
		data.limit(data.position() + getFrameNameLength());
		return ODSUtils.fromLMBCS(data);
	}

	public String getFrameTarget() {
		int preceding = getFrameNameLength();

		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 30 + preceding);
		data.limit(data.position() + getFrameTargetLength());
		return ODSUtils.fromLMBCS(data);
	}
}
