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
	/**
	 * @param document
	 */
	protected ACLNote(final Document document) {
		super(document);
	}

	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(ACLNote.class.getName());
	private static final long serialVersionUID = 1L;
}
