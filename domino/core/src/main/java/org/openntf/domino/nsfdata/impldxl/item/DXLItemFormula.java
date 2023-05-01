/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.nsfdata.impldxl.item;

import java.util.Base64;

import org.openntf.domino.utils.xml.XMLNode;

public class DXLItemFormula extends AbstractDXLItem {
	private static final long serialVersionUID = 1L;

	private final String formula_;
	private final byte[] compiledFormula_;

	protected DXLItemFormula(final XMLNode node, final int dupItemId) {
		super(node, dupItemId);

		XMLNode dataNode = node.getFirstChildElement();
		if("true".equals(dataNode.getAttribute("compiled"))) { //$NON-NLS-1$ //$NON-NLS-2$
			formula_ = null;
			compiledFormula_ = Base64.getDecoder().decode(dataNode.getText());
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
