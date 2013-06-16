/**
 * 
 */
package org.openntf.domino.design.impl;

import java.util.logging.Logger;

import org.openntf.domino.Document;
import org.openntf.domino.design.Folder;

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
	public AbstractFolder(final Document document) {
		super(document);
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
}
