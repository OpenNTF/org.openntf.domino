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
public class Folder extends AbstractFolder implements org.openntf.domino.design.Folder {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(Folder.class.getName());

	/**
	 * @param document
	 */
	public Folder(final Document document) {
		super(document);
	}
}
