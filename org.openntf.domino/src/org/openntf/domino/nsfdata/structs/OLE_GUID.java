package org.openntf.domino.nsfdata.structs;

import java.nio.ByteBuffer;

/**
 * This is taken directly from OLE's compobj.h. The reason it's copied rather than included here is to eliminate inclusion of the OLE2
 * header files, which without great pain, only compile on OLE platforms. This header file is included on ALL Notes platforms, so we don't
 * want to mess with the whole of OLE just for the GUID typedef... (oleods.h)
 * 
 * @author jgallagher
 * @since Lotus Notes 4.1
 *
 */
public class OLE_GUID extends AbstractStruct {
	public static final int SIZE = 16;

	public OLE_GUID(final ByteBuffer data) {
		super(data);
	}

	@Override
	public int getStructSize() {
		return SIZE;
	}

	public long getData1() {
		return getData().getInt(getData().position() + 0) & 0xFFFFFFFF;
	}

	public int getData2() {
		return getData().getShort(getData().position() + 4) & 0xFFFF;
	}

	public int getData3() {
		return getData().getShort(getData().position() + 6) & 0xFFFF;
	}

	public byte[] getData4() {
		byte[] result = new byte[8];
		getData().duplicate().get(result, 8, 8);
		return result;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Data1=" + getData1() + ", Data2=" + getData2() + ", Data3=" + getData3() + "]";
	}
}
