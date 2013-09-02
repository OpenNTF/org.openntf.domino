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
public class ImageResource extends FileResource implements org.openntf.domino.design.ImageResource {
	private static final Logger log_ = Logger.getLogger(ImageResource.class.getName());

	/**
	 * @param document
	 */
	public ImageResource(final Document document) {
		super(document);
	}

}
