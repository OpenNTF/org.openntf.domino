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
/**
 * 
 */
package org.openntf.domino.utils.xml;

import java.util.ArrayList;

import org.w3c.dom.NodeList;

/**
 * @author jgallagher
 * 
 */
public class XMLNodeList extends ArrayList<XMLNode> {
	private static final long serialVersionUID = -5345253808779456477L;

	public XMLNodeList() {
		super();
	}

	public XMLNodeList(final int initialCapacity) {
		super(initialCapacity);
	}

	public XMLNodeList(final NodeList nodeList) {
		super(nodeList.getLength());

		for (int i = 0; i < nodeList.getLength(); i++) {
			this.add(new XMLNode(nodeList.item(i)));
		}
	}

	@Override
	public XMLNode remove(final int i) {
		XMLNode result = super.remove(i);
		if (result != null) {
			result.getParentNode().removeChild(result);
		}
		return result;
	}

	public void swap(final int indexA, final int indexB) {
		XMLNode a = this.get(indexA);
		XMLNode b = this.get(indexB);
		swap(a, b);
	}

	public void swap(final XMLNode a, final XMLNode b) {
		XMLNode parentA = a.getParentNode();
		XMLNode siblingA = a.getNextSibling();

		XMLNode parentB = b.getParentNode();
		XMLNode siblingB = b.getNextSibling();

		if (siblingA == null) {
			parentA.appendChild(b);
		} else {
			parentA.insertBefore(b, siblingA);
		}
		if (siblingB == null) {
			parentB.appendChild(a);
		} else {
			parentB.insertBefore(a, siblingB);
		}
	}
}