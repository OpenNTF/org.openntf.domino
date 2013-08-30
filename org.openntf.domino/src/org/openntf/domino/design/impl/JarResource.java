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
public class JarResource extends FileResource implements org.openntf.domino.design.JarResource {
	private static final Logger log_ = Logger.getLogger(JarResource.class.getName());
	private static final long serialVersionUID = 1L;

	protected JarResource(final Document document) {
		super(document);
	}
}
