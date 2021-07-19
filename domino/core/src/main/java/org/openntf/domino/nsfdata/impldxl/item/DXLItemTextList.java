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

import java.util.Arrays;
import java.util.List;

import org.openntf.domino.utils.xml.XMLNode;
import org.openntf.domino.utils.xml.XMLNodeList;

public class DXLItemTextList extends AbstractDXLItem {
	private static final long serialVersionUID = 1L;

	private final String[] value_;

	protected DXLItemTextList(final XMLNode node, final int dupItemId) {
		super(node, dupItemId);

		XMLNodeList dataNodes = node.selectNodes("./textlist/text"); //$NON-NLS-1$
		value_ = new String[dataNodes.size()];
		for (int i = 0; i < dataNodes.size(); i++) {
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

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": " + getValue() + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}
