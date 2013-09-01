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
public class StyleSheet extends FileResource implements org.openntf.domino.design.StyleSheet {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(StyleSheet.class.getName());

	/**
	 * @param document
	 */
	public StyleSheet(final Document document) {
		super(document);
	}

	/**
	 * @param database
	 */
	public StyleSheet(final Database database) {
		super(database);
	}
}
