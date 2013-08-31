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
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(UsingDocument.class.getName());

	/**
	 * @param document
	 */
	protected UsingDocument(final Document document) {
		super(document);
	}

}
