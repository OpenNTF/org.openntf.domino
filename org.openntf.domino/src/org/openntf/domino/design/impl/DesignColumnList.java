/**
 * 
 */
package org.openntf.domino.design.impl;

import java.util.logging.Logger;

import javax.xml.xpath.XPathExpressionException;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.xml.XMLNode;

/**
 * @author jgallagher
 * 
 */
public class DesignColumnList extends AbstractDesignComponentList<org.openntf.domino.design.DesignColumn> implements
		org.openntf.domino.design.DesignColumnList {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(DesignColumnList.class.getName());
	private static final long serialVersionUID = 1L;

	protected DesignColumnList(final AbstractDesignBase parent, final String pattern) {
		super(parent, pattern);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractList#get(int)
	 */
	@Override
	public org.openntf.domino.design.DesignColumn get(final int index) {
		return new DesignColumn(getNodes().get(index));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(final Object o) {
		if (!(o instanceof DesignColumn || o instanceof String)) {
			throw new IllegalArgumentException();
		}
		String name = o instanceof String ? o.toString() : ((DesignColumn) o).getItemName();
		for (int i = 0; i < size(); i++) {
			if (name.equalsIgnoreCase(get(i).getItemName())) {
				remove(i);
				return true;
			}
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignColumnList#addColumn()
	 */
	@Override
	public DesignColumn addColumn() {
		// Create the column node and set the defaults
		// Make sure to add the node before any items
		try {
			XMLNode node;
			XMLNode item = getParent().getDxl().selectSingleNode("//item");
			if (item != null) {
				node = getParent().getDxl().insertChildElementBefore("column", item);
			} else {
				node = getParent().getDxl().addChildElement("column");
			}

			node.setAttribute("hidedetailrows", "false");
			node.setAttribute("width", "10");
			node.setAttribute("resizable", "true");
			node.setAttribute("separatemultiplevalues", "false");
			node.setAttribute("sortnoaccent", "false");
			node.setAttribute("sortnocase", "true");
			node.setAttribute("showaslinks", "false");

			refreshNodes();

			return new DesignColumn(node);
		} catch (XPathExpressionException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}
}
