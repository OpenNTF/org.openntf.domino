package org.openntf.domino.nsfdata.impldxl.item;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;

import org.openntf.domino.utils.xml.XMLNode;

public class DXLItemFormula extends AbstractDXLItem {
	private static final long serialVersionUID = 1L;

	private final String formula_;
	private final byte[] compiledFormula_;

	protected DXLItemFormula(final XMLNode node, final int dupItemId) {
		super(node, dupItemId);

		XMLNode dataNode = node.getFirstChildElement();
		if("true".equals(dataNode.getAttribute("compiled"))) {
			formula_ = null;
			compiledFormula_ = parseBase64Binary(dataNode.getText());
		} else {
			formula_ = dataNode.getText();
			compiledFormula_ = null;
		}
	}

	@Override
	public Object getValue() {
		if(formula_ != null) {
			return formula_;
		} else {
			return compiledFormula_;
		}
	}

	@Override
	public Type getType() {
		return Type.FORMULA;
	}

	@Override
	public byte[] getBytes() {
		if(formula_ != null) {
			throw new UnsupportedOperationException();
		} else {
			return compiledFormula_;
		}
	}

	public boolean isCompiled() {
		return formula_ == null;
	}
}
