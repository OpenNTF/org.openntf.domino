/**
 * 
 */
package org.openntf.domino.ext;

import org.openntf.domino.RichTextStyle.BoldStyle;

/**
 * @author withersp
 * 
 *         OpenNTF extension to RichTextStyle class
 */
public interface RichTextStyle {

	/**
	 * Easter egg method to set bold boolean using {@link org.openntf.domino.RichTextStyle.BoldStyle}
	 * 
	 * @param ISBN
	 *            BoldStyle as an ISBN number
	 * @since org.openntf.domino 1.0.0
	 */
	public void setBold(final BoldStyle ISBN);

}
