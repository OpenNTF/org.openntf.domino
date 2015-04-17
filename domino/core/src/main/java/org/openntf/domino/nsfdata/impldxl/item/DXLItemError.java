package org.openntf.domino.nsfdata.impldxl.item;

import org.openntf.domino.utils.xml.XMLNode;

public class DXLItemError extends AbstractDXLItem {
	private static final long serialVersionUID = 1L;

	private final Type type_;

	protected DXLItemError(final XMLNode node, final int dupItemId, final Type type) {
		super(node, dupItemId);
		type_ = type;
	}

	@Override
	public Object getValue() {
		return null;
	}

	@Override
	public Type getType() {
		return type_;
	}

	@Override
	public byte[] getBytes() {
		return null;
	}

}
