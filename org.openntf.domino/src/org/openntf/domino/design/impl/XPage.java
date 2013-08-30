/**
 * 
 */
package org.openntf.domino.design.impl;

import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;

/**
 * @author jgallagher
 * 
 */
public class XPage extends JavaResource implements org.openntf.domino.design.XPage {
	private static final Logger log_ = Logger.getLogger(XPage.class.getName());
	private static final long serialVersionUID = 1L;

	protected XPage(final Document document) {
		super(document);
	}

	/**
	 * @param database
	 */
	protected XPage(final Database database) {
		super(database);
	}
}
