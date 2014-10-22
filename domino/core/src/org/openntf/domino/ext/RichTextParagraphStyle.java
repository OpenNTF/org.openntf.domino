/**
 * 
 */
package org.openntf.domino.ext;

import org.openntf.domino.RichTextParagraphStyle.Align;
import org.openntf.domino.Session;

/**
 * @author withersp
 * 
 *         OpenNTF extensions for RichTextParagraphStyle class
 */
public interface RichTextParagraphStyle {

	/**
	 * Gets the parent Session of the RichTextParagraphStyle
	 * 
	 * @return parent Session
	 * @since org.openntf.domino 1.0.0
	 */
	public Session getParent();

	/**
	 * Sets the alignment using {@link org.openntf.domino.RichTextParagraphStyle.Align}
	 * 
	 * @param value
	 *            Align alignment
	 * @since org.openntf.domino 1.0.0
	 */
	public void setAlignment(final Align value);

}
