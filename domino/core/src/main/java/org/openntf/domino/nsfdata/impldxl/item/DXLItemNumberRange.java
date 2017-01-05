package org.openntf.domino.nsfdata.impldxl.item;

import java.util.Arrays;
import java.util.List;

import org.openntf.domino.utils.xml.XMLNode;
import org.openntf.domino.utils.xml.XMLNodeList;

public class DXLItemNumberRange extends AbstractDXLItem {
	private static final long serialVersionUID = 1L;

	Double[] value_;

	protected DXLItemNumberRange(final XMLNode node, final int dupItemId) {
		super(node, dupItemId);

		// Luckily, number ranges can't be stored in an NSF, so we'll never encounter them here
		XMLNodeList dataNodes = node.selectNodes("./numberlist/number");
		value_ = new Double[dataNodes.size()];
		for(int i = 0; i < dataNodes.size(); i++) {
			value_[i] = Double.valueOf(dataNodes.get(i).getText());
		}
	}

	@Override
	public List<Double> getValue() {
		return Arrays.asList(value_);
	}

	@Override
	public Type getType() {
		return Type.NUMBER_RANGE;
	}

	@Override
	public byte[] getBytes() {
		throw new UnsupportedOperationException();
	}

}
