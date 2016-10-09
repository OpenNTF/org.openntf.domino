package org.openntf.domino.nsfdata.impldxl.item;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.COLLATION;
import org.openntf.domino.utils.xml.XMLNode;

public class DXLItemCollation extends DXLItemRaw {
	private static final long serialVersionUID = 1L;

	public DXLItemCollation(final XMLNode node, final int dupItemId) {
		super(node, dupItemId);
	}

	@Override
	public COLLATION getValue() {
		ByteBuffer data = ByteBuffer.wrap(getBytes());
		COLLATION result = new COLLATION();
		result.init(data);
		return result;
	}
}
