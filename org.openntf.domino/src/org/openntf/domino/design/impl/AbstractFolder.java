/**
 * 
 */
package org.openntf.domino.design.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.xpath.XPathExpressionException;

import org.openntf.domino.Document;
import org.openntf.domino.design.Folder;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.xml.XMLNode;
import org.openntf.domino.utils.xml.XMLNodeList;

/**
 * @author jgallagher
 * 
 */
public class AbstractFolder extends AbstractDesignBaseNamed implements Folder {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(AbstractFolder.class.getName());
	private static final long serialVersionUID = 1L;

	/**
	 * @param document
	 */
	public AbstractFolder(Document document) {
		super(document);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.Folder#addColumn()
	 */
	@Override
	public DesignViewColumn addColumn() {
		// Create the column node and set the defaults
		// Make sure to add the node before any items
		try {
			XMLNode node;
			XMLNode item = getDxl().selectSingleNode("//item");
			if (item != null) {
				node = getDxl().insertChildElementBefore("column", item);
			} else {
				node = getDxl().addChildElement("column");
			}

			node.setAttribute("hidedetailrows", "false");
			node.setAttribute("width", "10");
			node.setAttribute("resizable", "true");
			node.setAttribute("separatemultiplevalues", "false");
			node.setAttribute("sortnoaccent", "false");
			node.setAttribute("sortnocase", "true");
			node.setAttribute("showaslinks", "false");

			return new DesignViewColumn(node);
		} catch (XPathExpressionException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.Folder#getColumns()
	 */
	@Override
	public List<org.openntf.domino.design.DesignViewColumn> getColumns() {
		// TODO Make this a live view on the list of columns
		try {
			List<XMLNode> columnNodes = getDxl().selectNodes("//column");
			List<org.openntf.domino.design.DesignViewColumn> result = new ArrayList<org.openntf.domino.design.DesignViewColumn>(columnNodes
					.size());
			for (XMLNode columnNode : columnNodes) {
				result.add(new DesignViewColumn(columnNode));
			}
			return Collections.unmodifiableList(result);
		} catch (XPathExpressionException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.Folder#removeColumn(int)
	 */
	@Override
	public void removeColumn(int index) {
		try {
			List<XMLNode> columnNodes = getDxl().selectNodes("//column");
			columnNodes.remove(index);
		} catch (XPathExpressionException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.Folder#swapColumns(int, int)
	 */
	@Override
	public void swapColumns(final int a, final int b) {
		try {
			XMLNodeList columnNodes = (XMLNodeList) getDxl().selectNodes("//column");
			columnNodes.swap(a, b);
		} catch (XPathExpressionException e) {
			DominoUtils.handleException(e);
		}
	}
}
