/**
 * 
 */
package org.openntf.domino.design.impl;

import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.design.Folder;
import org.openntf.domino.utils.xml.XMLNode;

/**
 * @author jgallagher
 * 
 */
public abstract class AbstractFolder extends AbstractDesignBaseNamed implements Folder {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(AbstractFolder.class.getName());
	private static final long serialVersionUID = 1L;

	/**
	 * @param document
	 */
	protected AbstractFolder(final Document document) {
		super(document);
	}

	protected AbstractFolder(final Database database) {
		super(database);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.Folder#getColumns()
	 */
	@Override
	public DesignColumnList getColumns() {
		return new DesignColumnList(this, "//column");
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
		XMLNode node;
		XMLNode item = getDxl().selectSingleNode("//item");
		if (item != null) {
			node = getDocumentElement().insertChildElementBefore("column", item);
		} else {
			node = getDocumentElement().addChildElement("column");
		}

		node.setAttribute("hidedetailrows", "false");
		node.setAttribute("width", "10");
		node.setAttribute("resizable", "true");
		node.setAttribute("separatemultiplevalues", "false");
		node.setAttribute("sortnoaccent", "false");
		node.setAttribute("sortnocase", "true");
		node.setAttribute("showaslinks", "false");

		return new DesignColumn(node);
	}
}
