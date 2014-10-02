package org.openntf.domino.nsfdata.impldxl.item;

import org.openntf.domino.utils.xml.XMLNode;

@SuppressWarnings("serial")
public abstract class DXLItemObject extends AbstractDXLItem {

	protected static DXLItemObject create(final XMLNode node, final int dupItemId) {
		XMLNode objectNode = node.getFirstChildElement();
		XMLNode objectDataNode = objectNode.getFirstChildElement();
		String objectDataNodeName = objectDataNode.getNodeName();
		if("file".equals(objectDataNodeName)) {
			return new DXLItemObjectFile(node, dupItemId);
		} else {
			throw new UnsupportedOperationException("Unsupported object type " + objectDataNodeName);
		}
	}

	protected DXLItemObject(final XMLNode node, final int dupItemId) {
		super(node, dupItemId);
	}

	@Override
	public Type getType() {
		return Type.OBJECT;
	}
}
