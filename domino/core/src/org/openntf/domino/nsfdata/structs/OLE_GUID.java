package org.openntf.domino.nsfdata.structs;

import java.util.UUID;

/**
 * @since Lotus Notes 4.1
 *
 */
public class OLE_GUID extends AbstractStruct {

	public final Unsigned32 Data1 = new Unsigned32();
	public final Unsigned16 Data2 = new Unsigned16();
	public final Unsigned16 Data3 = new Unsigned16();
	public final Unsigned8[] Data4 = array(new Unsigned8[8]);

	public UUID getUUID() {
		return UUID.nameUUIDFromBytes(getBytes());
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Data1=" + Data1.get() + ", Data2=" + Data2.get() + ", Data3=" + Data3.get()
				+ ", Data4=" + Data4 + "]";
	}
}
