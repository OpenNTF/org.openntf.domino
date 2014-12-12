package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

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

	public static final int fFSBorderEnable = 0x00000001;
	public static final int fFSFrameBorderDims = 0x00000004;
	public static final int fFSFrameSpacingDims = 0x00000008;
	public static final int fFSFrameBorderColor = 0x00000040;

	// TODO make enum
	public final Unsigned32 Flags = new Unsigned32();
	public final Bool BorderEnable = new Bool();
	public final Unsigned8 byAvail1 = new Unsigned8();
	public final Unsigned16 Reserved1 = new Unsigned16();
	public final Unsigned16 Reserved2 = new Unsigned16();
	public final Unsigned16 FrameBorderWidth = new Unsigned16();
	public final Unsigned16 Reserved3 = new Unsigned16();
	public final Unsigned16 FrameSpacingWidth = new Unsigned16();
	public final Unsigned16 Reserved4 = new Unsigned16();
	public final COLOR_VALUE ReservedColor1 = inner(new COLOR_VALUE());
	public final COLOR_VALUE ReservedColor2 = inner(new COLOR_VALUE());
	public final Unsigned16 RowQty = new Unsigned16();
	public final Unsigned16 ColQty = new Unsigned16();
	public final Unsigned16 Reserved5 = new Unsigned16();
	public final Unsigned16 Reserved6 = new Unsigned16();
	public final COLOR_VALUE FrameBorderColor = inner(new COLOR_VALUE());
	// TODO make enum?
	public final Unsigned8 ThemeSetting = new Unsigned8();
	public final Unsigned8 Reserved7 = new Unsigned8();

	static {

		addVariableArray("Rows", "RowQty", FRAMESETLENGTH.class);
		addVariableArray("Cols", "ColQty", FRAMESETLENGTH.class);
	}

	public CDFRAMESET(final CDSignature cdSig) {
		super(cdSig);
	}

	public CDFRAMESET(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public FRAMESETLENGTH[] getRows() {
		return (FRAMESETLENGTH[]) getVariableElement("Rows");
	}

	public FRAMESETLENGTH[] getCols() {
		return (FRAMESETLENGTH[]) getVariableElement("Cols");
	}

	//	@Override
	//	public String toString() {
	//		return "[" + getClass().getSimpleName() + ", BorderEnable: " + getBorderEnable() + ", FrameBorderWidth: " + getFrameBorderWidth()
	//				+ ", FrameSpacingWidth: " + getFrameSpacingWidth() + ", FrameBorderColor: " + getFrameBorderColor() + ", ThemeSetting: "
	//				+ getThemeSetting() + ", RowQty: " + getRowQty() + ", ColQty: " + getColQty() + ", Rows: " + Arrays.asList(getRows())
	//				+ ", Cols: " + Arrays.asList(getCols()) + "]";
	//	}
}
