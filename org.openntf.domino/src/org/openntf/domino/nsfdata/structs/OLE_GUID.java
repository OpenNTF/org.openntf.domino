package org.openntf.domino.nsfdata.structs;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * This is taken directly from OLE's compobj.h. The reason it's copied rather than included here is to eliminate inclusion of the OLE2
 * header files, which without great pain, only compile on OLE platforms. This header file is included on ALL Notes platforms, so we don't
 * want to mess with the whole of OLE just for the GUID typedef... (oleods.h)
 * 
 * @since Lotus Notes 4.1
 *
 */
public class OLE_GUID extends AbstractStruct {
	public static final int SIZE = 16;

	static {
		addFixedUnsigned("Data1", Integer.class);
		addFixedUnsigned("Data2", Short.class);
		addFixedUnsigned("Data3", Short.class);
		addFixedArray("Data4", Byte.class, 8);
	}

	public OLE_GUID() {
		super();
	}

	public OLE_GUID(final ByteBuffer data) {
		super(data);
	}

	@Override
	public long getStructSize() {
		return SIZE;
	}

	public long getData1() {
		return (Long) getStructElement("Data1");
	}

	public int getData2() {
		return (Integer) getStructElement("Data2");
	}

	public int getData3() {
		return (Integer) getStructElement("Data3");
	}

	public byte[] getData4() {
		return (byte[]) getStructElement("Data4");
	}

	public UUID getUUID() {
		return UUID.nameUUIDFromBytes(getBytes());
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Data1=" + getData1() + ", Data2=" + getData2() + ", Data3=" + getData3() + ", Data4="
				+ getData4() + "]";
	}
}
