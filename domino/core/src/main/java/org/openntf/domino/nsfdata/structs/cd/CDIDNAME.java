package org.openntf.domino.nsfdata.structs.cd;

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

	public final WSIG Header = inner(new WSIG());
	public final Unsigned16 Length = new Unsigned16();
	public final Unsigned16 wClassLen = new Unsigned16();
	public final Unsigned16 wStyleLen = new Unsigned16();
	public final Unsigned16 wTitleLen = new Unsigned16();
	public final Unsigned16 wExtraLen = new Unsigned16();
	public final Unsigned16 wNameLen = new Unsigned16();
	public final Unsigned8[] reserved = array(new Unsigned8[10]);

	static {
		addVariableString("ID", "Length");
		addVariableString("Class", "wClassLen");
		addVariableString("Style", "wStyleLen");
		addVariableString("Title", "wTitleLen");
		addVariableString("Extra", "wExtraLen");
		addVariableString("Name", "wNameLen");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	public String getId() {
		return (String) getVariableElement("ID");
	}

	public String getStyleCLass() {
		return (String) getVariableElement("Class");
	}

	public String getStyle() {
		return (String) getVariableElement("Style");
	}

	public String getTitle() {
		return (String) getVariableElement("Title");
	}

	public String getExtra() {
		return (String) getVariableElement("Extra");
	}

	public String getName() {
		return (String) getVariableElement("Name");
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
