package org.openntf.domino.nsfdata.impldxl.item;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.MIME_PART;
import org.openntf.domino.utils.xml.XMLNode;

public class DXLItemMIMEPart extends DXLItemRaw {
	private static final long serialVersionUID = 1L;

	protected DXLItemMIMEPart(final XMLNode node, final int dupItemId) {
		super(node, dupItemId);
	}

	@Override
	public MIME_PART getValue() {
		ByteBuffer data = ByteBuffer.wrap(getBytes());
		MIME_PART result = new MIME_PART();
		result.init(data);
		return result;
	}
}
