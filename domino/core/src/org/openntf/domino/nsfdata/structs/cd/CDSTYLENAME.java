package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure stores the style name for a Paragraph Attributes Block (PAB). (editods.h)
 */
public class CDSTYLENAME extends CDRecord {

	public static final int MAX_STYLE_NAME = 35;

	// TODO make enum
	public final Unsigned32 Flags = new Unsigned32();
	public final Unsigned16 PABID = new Unsigned16();
	public final Unsigned8[] StyleName = array(new Unsigned8[MAX_STYLE_NAME + 1]);

	static {
		// TODO add oddball variable elements
		//		/* If STYLE_FLAG_FONTID, a FONTID follows this structure... */
		//		/* If STYLE_FLAG_PERMANENT, the structure is followed by:	*/
		//		/*		WORD	nameLen			Length of user name	*/
		//		/*		BYTE	userName [nameLen]	User name			*/
	}

	public CDSTYLENAME(final CDSignature cdSig) {
		super(cdSig);
	}

	public CDSTYLENAME(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public String getStyleName() {
		return ODSUtils.fromAscii(StyleName);
	}
}
