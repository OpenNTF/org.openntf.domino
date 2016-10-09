package org.openntf.domino.nsfdata.impldxl.item;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.ViewFormat;
import org.openntf.domino.utils.xml.XMLNode;

public class DXLItemViewFormat extends DXLItemRaw {
	private static final long serialVersionUID = 1L;

	protected DXLItemViewFormat(final XMLNode node, final int dupItemId) {
		super(node, dupItemId);
	}

	@Override
	public ViewFormat getValue() {
		ByteBuffer data = ByteBuffer.wrap(getBytes());
		return new ViewFormat(data);
	}
}
