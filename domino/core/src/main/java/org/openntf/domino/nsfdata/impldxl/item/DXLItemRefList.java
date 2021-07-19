/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.nsfdata.impldxl.item;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openntf.domino.nsfdata.structs.LIST;
import org.openntf.domino.nsfdata.structs.UNIVERSALNOTEID;
import org.openntf.domino.utils.xml.XMLNode;

@SuppressWarnings("nls")
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
		LIST list = new LIST();
		list.init(data);
		UNIVERSALNOTEID[] ids = new UNIVERSALNOTEID[list.ListEntries.get()];
		data.position((int) (data.position() + list.getStructSize()));
		for (int i = 0; i < ids.length; i++) {
			ByteBuffer idData = data.duplicate();
			idData.limit(idData.position() + 16);
			ids[i] = new UNIVERSALNOTEID();
			ids[i].init(idData);

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
		LIST list = new LIST();
		list.init(data);
		data.position((int) (list.getStructSize() + (16 * list.ListEntries.get())));
		list.ListEntries.set(list.ListEntries.get() + 1);
		data.put(value.getData());

		setBytes(data.array());
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": value=" + Arrays.asList(getValue()) + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}
