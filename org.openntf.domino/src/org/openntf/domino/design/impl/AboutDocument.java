/**
 * 
 */
package org.openntf.domino.design.impl;

import java.util.logging.Logger;

import org.openntf.domino.Document;

/**
 * @author jgallagher
 * 
 */
public class AboutDocument extends AbstractDesignBase implements org.openntf.domino.design.AboutDocument {
	/**
	 * @param document
	 */
	protected AboutDocument(final Document document) {
		super(document);
	}

	private static final Logger log_ = Logger.getLogger(AboutDocument.class.getName());
}
