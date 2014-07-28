package org.openntf.domino.nsfdata.impldxl.item;

import java.util.Arrays;
import java.util.List;

import org.openntf.domino.utils.xml.XMLNode;
import org.openntf.domino.utils.xml.XMLNodeList;

public class DXLItemTextList extends AbstractDXLItem {
	private static final long serialVersionUID = 1L;

	private final String[] value_;

	protected DXLItemTextList(final XMLNode node, final int dupItemId) {
		super(node, dupItemId);

		XMLNodeList dataNodes = node.selectNodes("./textlist/text");
		value_ = new String[dataNodes.size()];
		for(int i = 0; i < dataNodes.size(); i++) {
			value_[i] = dataNodes.get(i).getText();
		}
	}

	@Override
	public List<String> getValue() {
		return Arrays.asList(value_);
	}

	@Override
	public Type getType() {
		return Type.TEXT_LIST;
	}

	@Override
	public byte[] getBytes() {
		throw new UnsupportedOperationException();
	}

}
