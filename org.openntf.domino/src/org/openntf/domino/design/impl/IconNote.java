/**
 * 
 */
package org.openntf.domino.design.impl;

import java.util.logging.Logger;

import org.openntf.domino.Document;

/**
 * @author Administrator
 * 
 */
public class IconNote extends AbstractDesignBase implements org.openntf.domino.design.IconNote {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(IconNote.class.getName());
	private static final long serialVersionUID = 1L;

	/**
	 * @param document
	 */
	protected IconNote(Document document) {
		super(document);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setAlias(java.lang.String)
	 */
	@Override
	public void setAlias(final String alias) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setAliases(java.lang.Iterable)
	 */
	@Override
	public void setAliases(final Iterable<String> aliases) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setName(java.lang.String)
	 */
	@Override
	public void setName(final String name) {
		getDxlNode("/note/item[@name='$TITLE']/text").setTextContent(name);
	}
}
