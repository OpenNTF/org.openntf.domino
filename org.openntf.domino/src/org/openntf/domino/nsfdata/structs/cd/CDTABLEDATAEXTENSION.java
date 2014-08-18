package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.COLOR_VALUE;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This record was added because the Pre Table Begin Record can not be expanded and R6 required more data to be stored. (editods.h)
 * 
 * @author jgallagher
 * @since Lotus Notes/Domino 6.0
 *
 */
public class CDTABLEDATAEXTENSION extends CDRecord {

	public CDTABLEDATAEXTENSION(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public int getColumnSizeToFitBits1() {
		return getData().getInt(getData().position() + 0);
	}

	public int getColumnSizeToFitBits2() {
		return getData().getInt(getData().position() + 4);
	}

	public short getEqualSizeTabsWidthX() {
		return getData().getShort(getData().position() + 8);
	}

	public short getTabsIndentWidthX() {
		return getData().getShort(getData().position() + 10);
	}

	public short getAvailable3() {
		return getData().getShort(getData().position() + 12);
	}

	public short getAvailable4() {
		return getData().getShort(getData().position() + 14);
	}

	public int getAvailable5() {
		return getData().getInt(getData().position() + 18);
	}

	public int getAvailable6() {
		return getData().getInt(getData().position() + 22);
	}

	public int getAvailable7() {
		return getData().getInt(getData().position() + 26);
	}

	public int getAvailable8() {
		return getData().getInt(getData().position() + 30);
	}

	public int getAvailable9() {
		return getData().getInt(getData().position() + 34);
	}

	/**
	 * @return Length of Tabs Label Font
	 */
	public short getTabLabelFontLength() {
		return getData().getShort(getData().position() + 38);
	}

	public short getAvailableLength11() {
		return getData().getShort(getData().position() + 40);
	}

	public short getAvailableLength12() {
		return getData().getShort(getData().position() + 42);
	}

	public short getExtension2Length() {
		return getData().getShort(getData().position() + 44);
	}

	public int getFontId() {
		// TODO map to fonts?
		return getData().getInt(getData().position() + 46);
	}

	public int getFontSpare() {
		return getData().getInt(getData().position() + 50);
	}

	public COLOR_VALUE getFontColor() {
		// See documentation - this may or may not be present
		if (getTabLabelFontLength() > 8) {
			ByteBuffer data = getData().duplicate();
			data.position(data.position() + 54);
			data.limit(data.position() + 6);
			return new COLOR_VALUE(data);
		}
		return null;
	}

	// There are available bytes after this based on the *Length fields at the end
}
