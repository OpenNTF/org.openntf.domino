/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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

import java.util.Base64;

import org.openntf.domino.utils.xml.XMLNode;

public class DXLItemRaw extends AbstractDXLItem {
	private static final long serialVersionUID = 1L;

	protected static DXLItemRaw create(final XMLNode node, final int dupItemId) {
		XMLNode rawitemdata = node.selectSingleNode("rawitemdata"); //$NON-NLS-1$
		int typeCode = Integer.parseInt(rawitemdata.getAttribute("type"), 16); //$NON-NLS-1$
		Type type = Type.valueOf(typeCode);

		switch (type) {
		case COMPOSITE:
			return new DXLItemComposite(node, dupItemId);
		case NOTEREF_LIST:
			return new DXLItemRefList(node, dupItemId);
		case MIME_PART:
			return new DXLItemMIMEPart(node, dupItemId);
		case COLLATION:
			return new DXLItemCollation(node, dupItemId);
		case VIEW_FORMAT:
			return new DXLItemViewFormat(node, dupItemId);
		default:
			return new DXLItemRaw(node, dupItemId);
		}

	}

	private final Type type_;
	private byte[] data_;

	protected DXLItemRaw(final XMLNode node, final int dupItemId) {
		super(node, dupItemId);

		XMLNode rawitemdata = node.selectSingleNode("rawitemdata"); //$NON-NLS-1$
		int typeCode = Integer.parseInt(rawitemdata.getAttribute("type"), 16); //$NON-NLS-1$
		type_ = Type.valueOf(typeCode);

		String rawData = rawitemdata.getText();
		data_ = Base64.getDecoder().decode(rawData);
	}

	@Override
	public Type getType() {
		return type_;
	}

	@Override
	public byte[] getBytes() {
		return data_;
	}

	protected void setBytes(final byte[] data) {
		data_ = data;
	}

	@Override
	public Object getValue() {
		return getBytes();
	}
}
