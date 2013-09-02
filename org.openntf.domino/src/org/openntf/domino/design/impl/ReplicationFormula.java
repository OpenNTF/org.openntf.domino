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
public class ReplicationFormula extends AbstractDesignBase implements org.openntf.domino.design.ReplicationFormula {
	private static final Logger log_ = Logger.getLogger(ReplicationFormula.class.getName());

	/**
	 * @param document
	 */
	protected ReplicationFormula(final Document document) {
		super(document);
	}

}
