package org.openntf.domino.nsfdata.impldxl.item;

import org.openntf.domino.nsfdata.cd.CData;
import org.openntf.domino.utils.xml.XMLNode;

public class DXLItemComposite extends DXLItemRaw {
	private static final long serialVersionUID = 1L;

	protected DXLItemComposite(final XMLNode node, final int dupItemId) {
		super(node, dupItemId);
	}

	@Override
	public CData getValue() {
		return new CData(getBytes());
	}
}
