package org.openntf.domino.nsfdata.impldxl.item;

import org.openntf.domino.utils.xml.XMLNode;

public class DXLItemNumber extends AbstractDXLItem {
	private static final long serialVersionUID = 1L;

	private final double value_;

	protected DXLItemNumber(final XMLNode node, final int dupItemId) {
		super(node, dupItemId);

		XMLNode dataNode = node.selectSingleNode("./number");
		value_ = Double.parseDouble(dataNode.getText());
	}

	@Override
	public Double getValue() {
		return value_;
	}

	@Override
	public Type getType() {
		return Type.NUMBER;
	}

	@Override
	public byte[] getBytes() {
		throw new UnsupportedOperationException();
	}

}
