/**
 * 
 */
package org.openntf.domino.design.impl;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.openntf.domino.Document;
import org.openntf.domino.utils.DominoUtils;

/**
 * @author jgallagher
 * 
 */
public class ImageResource extends AbstractDesignBaseNamed implements org.openntf.domino.design.ImageResource {
	private static final Logger log_ = Logger.getLogger(ImageResource.class.getName());

	/**
	 * @param document
	 */
	public ImageResource(final Document document) {
		super(document);
	}

	@Override
	public byte[] getImageData() {
		return parseBase64Binary(getDxl().selectSingleNode("//jpeg").getText());
	}

	@Override
	public BufferedImage getImage() {
		try {
			return ImageIO.read(new ByteArrayInputStream(getImageData()));
		} catch (IOException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}
}
