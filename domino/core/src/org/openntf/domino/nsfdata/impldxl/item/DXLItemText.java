package org.openntf.domino.nsfdata.impldxl.item;

import org.openntf.domino.utils.xml.XMLNode;

public class DXLItemText extends AbstractDXLItem {
	private static final long serialVersionUID = 1L;

	private final String value_;

	protected DXLItemText(final XMLNode node, final int dupItemId) {
		super(node, dupItemId);

		XMLNode dataNode = node.selectSingleNode("./text");
		value_ = dataNode.getText();
	}

	@Override
	public Type getType() {
		return Type.TEXT;
	}

	@Override
	public byte[] getBytes() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getValue() {
		return value_;
	}
}
