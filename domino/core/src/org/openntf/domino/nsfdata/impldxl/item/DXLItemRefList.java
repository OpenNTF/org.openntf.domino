package org.openntf.domino.nsfdata.impldxl.item;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openntf.domino.nsfdata.structs.LIST;
import org.openntf.domino.nsfdata.structs.UNIVERSALNOTEID;
import org.openntf.domino.utils.xml.XMLNode;

public class DXLItemRefList extends DXLItemRaw {
	private static final long serialVersionUID = 1L;

	public DXLItemRefList(final XMLNode node, final int dupItemId) {
		super(node, dupItemId);
	}

	@Override
	public UNIVERSALNOTEID[] getValue() {
		List<Short> rawData = new ArrayList<Short>(getBytes().length);
		for (byte aByte : this.getBytes()) {
			rawData.add((short) (aByte & 0xFF));
		}

		ByteBuffer data = ByteBuffer.wrap(this.getBytes());
		data.order(ByteOrder.nativeOrder());
		LIST list = new LIST(data);
		UNIVERSALNOTEID[] ids = new UNIVERSALNOTEID[list.ListEntries.get()];
		data.position((int) (data.position() + list.getStructSize()));
		for (int i = 0; i < ids.length; i++) {
			ByteBuffer idData = data.duplicate();
			idData.limit(idData.position() + 16);
			ids[i] = new UNIVERSALNOTEID(idData);

			data.position(data.position() + 16);
		}
		return ids;
	}

	public void addId(final UNIVERSALNOTEID value) {
		if (value == null) {
			throw new IllegalArgumentException("value cannot be null");
		}
		byte[] oldBytes = this.getBytes();
		byte[] newBytes = new byte[oldBytes.length + 16];
		System.arraycopy(oldBytes, 0, newBytes, 0, oldBytes.length);
		ByteBuffer data = ByteBuffer.wrap(newBytes);
		data.order(ByteOrder.LITTLE_ENDIAN);
		LIST list = new LIST(data);
		data.position((int) (list.getStructSize() + (16 * list.ListEntries.get())));
		list.ListEntries.set(list.ListEntries.get() + 1);
		data.put(value.getData());

		setBytes(data.array());
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": value=" + Arrays.asList(getValue()) + "]";
	}
}
