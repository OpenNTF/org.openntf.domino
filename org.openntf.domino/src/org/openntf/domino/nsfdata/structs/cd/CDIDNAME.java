package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This CD record describes the HTML field properties, ID, Class, Style, Title, Other and Name associated for any given field defined within
 * a Domino Form. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDIDNAME extends CDRecord {

	static {
		addFixedUnsigned("Length", Short.class);
		addFixedUnsigned("wClassLen", Short.class);
		addFixedUnsigned("wStyleLen", Short.class);
		addFixedUnsigned("wTitleLen", Short.class);
		addFixedUnsigned("wExtraLen", Short.class);
		addFixedUnsigned("wNameLen", Short.class);
		addFixedArray("reserved", Byte.class, 10);

		addVariableString("ID", "Length");
		addVariableString("Class", "wClassLen");
		addVariableString("Style", "wStyleLen");
		addVariableString("Title", "wTitleLen");
		addVariableString("Extra", "wExtraLen");
		addVariableString("Name", "wNameLen");
	}

	public static final int SIZE = getFixedStructSize();

	public CDIDNAME(final CDSignature cdSig) {
		super(new WSIG(cdSig, cdSig.getSize() + SIZE), ByteBuffer.wrap(new byte[SIZE]));
	}

	public CDIDNAME(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public String getId() {
		return (String) getStructElement("ID");
	}

	public String getStyleCLass() {
		return (String) getStructElement("Class");
	}

	public String getStyle() {
		return (String) getStructElement("Style");
	}

	public String getTitle() {
		return (String) getStructElement("Title");
	}

	public String getExtra() {
		return (String) getStructElement("Extra");
	}

	public String getName() {
		return (String) getStructElement("Name");
	}

	//	@Override
	//	public int getExtraLength() {
	//		int length = (Integer) getStructElement("Length");
	//		int classLen = (Integer) getStructElement("wClassLen");
	//		int styleLen = (Integer) getStructElement("wStyleLen");
	//		int titleLen = (Integer) getStructElement("wTitleLen");
	//		int extraLen = (Integer) getStructElement("wExtraLen");
	//		int nameLen = (Integer) getStructElement("wNameLen");
	//
	//		return length % 2 + classLen % 2 + styleLen % 2 + titleLen % 2 + extraLen % 2 + nameLen % 2;
	//	}
}
