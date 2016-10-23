package org.openntf.domino.nsfdata.impldxl.item;

import org.openntf.domino.nsfdata.NSFDateTime;
import org.openntf.domino.utils.xml.XMLNode;

public class DXLItemTime extends AbstractDXLItem {
	private static final long serialVersionUID = 1L;

	private final NSFDateTime value_;

	protected DXLItemTime(final XMLNode node, final int dupItemId) {
		super(node, dupItemId);

		XMLNode dataNode = node.getFirstChildElement();
		value_ = DXLItemFactory.createDateTime(dataNode);
	}

	@Override
	public NSFDateTime getValue() {
		return value_;
	}

	@Override
	public Type getType() {
		return Type.TIME;
	}

	@Override
	public byte[] getBytes() {
		throw new UnsupportedOperationException();
	}
}
