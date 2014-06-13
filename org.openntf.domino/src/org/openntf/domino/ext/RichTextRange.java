/**
 * 
 */
package org.openntf.domino.ext;

import org.openntf.domino.RichTextItem;

/**
 * @author withersp
 * 
 *         OpenNTF extensions to RichTextRange class
 */
public interface RichTextRange {

	/**
	 * Gets the RichTextItem that is the parent of the RichTextRange
	 * 
	 * @return parent RichTextItem
	 * @since org.openntf.domino 1.0.0
	 */
	public RichTextItem getParent();

}
