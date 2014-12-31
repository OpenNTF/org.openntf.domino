package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.FONTID;
import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * Contains the header or footer used in a document. (editods.h)
 * 
 * @since forever
 *
 */
public class CDHEADER extends CDRecord {

	public static final int MAXFACESIZE = 32;

	public final WSIG Header = inner(new WSIG());
	public final Unsigned8 FontPitchAndFamily = new Unsigned8();
	/**
	 * @deprecated Use {@link #getFontName} for access
	 */
	@Deprecated
	public final Unsigned8[] FontName = array(new Unsigned8[MAXFACESIZE]);
	public final FONTID Font = inner(new FONTID());
	public final Unsigned16 HeadLength = new Unsigned16();

	static {
		addVariableString("Text", "HeadLength");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	public String getFontName() {
		return ODSUtils.fromAscii(FontName);
	}

	public String getText() {
		return (String) getVariableElement("Text");
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": FontPitchAndFamily=" + FontPitchAndFamily.get() + ", FontName=" + getFontName()
				+ ", Font=" + Font + ", HeadLength=" + HeadLength.get() + ", Text=" + getText() + "]";
	}
}
