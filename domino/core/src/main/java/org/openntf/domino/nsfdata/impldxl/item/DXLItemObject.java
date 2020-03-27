/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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
