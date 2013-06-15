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
public class UsingDocument extends AbstractDesignBase implements org.openntf.domino.design.UsingDocument {
	/**
	 * @param document
	 */
	protected UsingDocument(Document document) {
		super(document);
	}

	private static final Logger log_ = Logger.getLogger(UsingDocument.class.getName());
	private static final long serialVersionUID = 1L;

}
