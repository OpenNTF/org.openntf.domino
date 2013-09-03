/**
 * 
 */
package org.openntf.domino.design;

import java.awt.image.BufferedImage;

/**
 * @author jgallagher
 * 
 */
public interface ImageResource extends DesignBaseNamed {
	public byte[] getImageData();

	public BufferedImage getImage();
}
