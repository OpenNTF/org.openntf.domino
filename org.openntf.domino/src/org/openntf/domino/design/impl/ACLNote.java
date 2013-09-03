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
public class ACLNote extends AbstractDesignBase implements org.openntf.domino.design.ACLNote {
	private static final Logger log_ = Logger.getLogger(ACLNote.class.getName());

	/**
	 * @param document
	 */
	protected ACLNote(final Document document) {
		super(document);
	}
}
