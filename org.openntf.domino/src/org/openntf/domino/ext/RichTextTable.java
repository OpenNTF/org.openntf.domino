/**
 * 
 */
package org.openntf.domino.ext;

import org.openntf.domino.RichTextItem;

/**
 * @author withersp
 * 
 *         OpenNTF extensions to RichTextTable class
 */
public interface RichTextTable {

	/**
	 * Gets the RichTextItem parent of the RichTextTable
	 * 
	 * @return parent RichTextItem
	 * @since org.openntf.domino 1.0.0
	 */
	public RichTextItem getParent();

}
