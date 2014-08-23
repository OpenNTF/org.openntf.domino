package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.COLOR_VALUE;
import org.openntf.domino.nsfdata.structs.FONTID;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This record was added because the Pre Table Begin Record can not be expanded and R6 required more data to be stored. (editods.h)
 * 
 * @since Lotus Notes/Domino 6.0
 *
 */
public class CDTABLEDATAEXTENSION extends CDRecord {

	static {
		addFixed("dwColumnSizeToFitBits1", Integer.class);
		addFixed("dwColumnSizeToFitBits2", Integer.class);
		addFixedUnsigned("wEqualSizeTabsWidthX", Short.class);
		addFixedUnsigned("wTabsIndentWidthX", Short.class);
		addFixed("wAvailable3", Short.class);
		addFixed("wAvailable4", Short.class);
		addFixed("dwAvailable5", Integer.class);
		addFixed("dwAvailable6", Integer.class);
		addFixed("dwAvailable7", Integer.class);
		addFixed("dwAvailable8", Integer.class);
		addFixed("dwAvailable9", Integer.class);

		addFixedUnsigned("wcTabLabelFont", Short.class);
		addFixedUnsigned("wAvailableLength11", Short.class);
		addFixedUnsigned("wAvailableLength12", Short.class);
		addFixedUnsigned("wExtension2Length", Short.class);

		addFixed("FontID", FONTID.class);
		addFixed("FontSpare", Integer.class);

		// The COLOR_VALUE is missing from data created by some R6 beta releases
		addVariableArray("FontColor", "getFontColorCount", COLOR_VALUE.class);

		addVariableData("Available11", "getAvailableLength11");
		addVariableData("Available12", "getAvailableLength12");
		addVariableData("Extension2", "getExtension2Length");
	}

	public CDTABLEDATAEXTENSION(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public int getColumnSizeToFitBits1() {
		return (Integer) getStructElement("dwColumnSizeToFitBits1");
	}

	public int getColumnSizeToFitBits2() {
		return (Integer) getStructElement("dwColumnSizeToFitBits2");
	}

	public int getEqualSizeTabsWidthX() {
		return (Integer) getStructElement("wEqualSizeTabsWidthX");
	}

	public int getTabsIndentWidthX() {
		return (Integer) getStructElement("wTabsIndentWidthX");
	}

	public short getAvailable3() {
		return (Short) getStructElement("wAvailable3");
	}

	public short getAvailable4() {
		return (Short) getStructElement("wAvailable4");
	}

	public int getAvailable5() {
		return (Integer) getStructElement("wAvailable5");
	}

	public int getAvailable6() {
		return (Integer) getStructElement("wAvailable6");
	}

	public int getAvailable7() {
		return (Integer) getStructElement("wAvailable7");
	}

	public int getAvailable8() {
		return (Integer) getStructElement("wAvailable8");
	}

	public int getAvailable9() {
		return (Integer) getStructElement("wAvailable9");
	}

	/**
	 * @return Length of Tabs Label Font
	 */
	public int getTabLabelFontLength() {
		return (Integer) getStructElement("wcTabLabelFont");
	}

	public int getAvailableLength11() {
		return (Integer) getStructElement("wAvailableLength11");
	}

	public int getAvailableLength12() {
		return (Integer) getStructElement("wAvailableLength12");
	}

	public int getExtension2Length() {
		return (Integer) getStructElement("wExtension2Length");
	}

	public FONTID getFontId() {
		return (FONTID) getStructElement("FontID");
	}

	public int getFontSpare() {
		return (Integer) getStructElement("FontSpare");
	}

	public int getFontColorCount() {
		return getTabLabelFontLength() > 8 ? 1 : 0;
	}

	public COLOR_VALUE getFontColor() {
		COLOR_VALUE[] fontColor = (COLOR_VALUE[]) getStructElement("FontColor");
		return fontColor.length > 0 ? fontColor[0] : null;
	}

	public byte[] getAvailable11() {
		return (byte[]) getStructElement("Available11");
	}

	public byte[] getAvailable12() {
		return (byte[]) getStructElement("Available12");
	}

	public byte[] getExtension2() {
		return (byte[]) getStructElement("Extension2");
	}
}
