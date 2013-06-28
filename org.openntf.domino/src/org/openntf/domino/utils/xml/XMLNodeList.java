/**
 * 
 */
package org.openntf.domino.utils.xml;

import java.util.ArrayList;

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