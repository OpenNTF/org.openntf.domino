package org.openntf.domino.nsfdata.impldxl.item;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;

import org.openntf.domino.utils.xml.XMLNode;

public class DXLItemRaw extends AbstractDXLItem {
	private static final long serialVersionUID = 1L;

	protected static DXLItemRaw create(final XMLNode node, final int dupItemId) {
		XMLNode rawitemdata = node.selectSingleNode("rawitemdata");
		int typeCode = Integer.parseInt(rawitemdata.getAttribute("type"), 16);
		Type type = Type.valueOf(typeCode);

		switch (type) {
		case COMPOSITE:
			return new DXLItemComposite(node, dupItemId);
		default:
			return new DXLItemRaw(node, dupItemId);
		}

	}

	private final Type type_;
	private final byte[] data_;

	protected DXLItemRaw(final XMLNode node, final int dupItemId) {
		super(node, dupItemId);

		XMLNode rawitemdata = node.selectSingleNode("rawitemdata");
		int typeCode = Integer.parseInt(rawitemdata.getAttribute("type"), 16);
		type_ = Type.valueOf(typeCode);

		String rawData = rawitemdata.getText();
		data_ = parseBase64Binary(rawData);
	}

	@Override
	public Type getType() {
		return type_;
	}

	@Override
	public byte[] getBytes() {
		return data_;
	}

	@Override
	public Object getValue() {
		return getBytes();
	}
}
