package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure stores the style name for a Paragraph Attributes Block (PAB). (editods.h)
 */
public class CDSTYLENAME extends CDRecord {

	public static final int MAX_STYLE_NAME = 35;

	static {
		addFixed("Flags", Integer.class);
		addFixed("PABID", Short.class);
		addFixedArray("StyleName", Byte.class, MAX_STYLE_NAME + 1);

		// TODO add oddball variable elements
		//		/* If STYLE_FLAG_FONTID, a FONTID follows this structure... */
		//		/* If STYLE_FLAG_PERMANENT, the structure is followed by:	*/
		//		/*		WORD	nameLen			Length of user name	*/
		//		/*		BYTE	userName [nameLen]	User name			*/
	}

	public CDSTYLENAME(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public int getFlags() {
		// TODO make enum
		return (Integer) getStructElement("Flags");
	}

	public short getPABID() {
		return (Short) getStructElement("PABID");
	}

	public String getStyleName() {
		return new String((byte[]) getStructElement("StyleName"));
	}
}
