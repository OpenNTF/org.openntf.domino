/**
 * 
 */
package org.openntf.domino.ext;

import org.openntf.domino.RichTextParagraphStyle.Align;
import org.openntf.domino.Session;

/**
 * @author withersp
 * 
 */
public interface RichTextParagraphStyle {

	/**
	 * @return parent session
	 */
	public Session getParent();

	/**
	 * @param value
	 *            alignment
	 */
	public void setAlignment(Align value);

}
