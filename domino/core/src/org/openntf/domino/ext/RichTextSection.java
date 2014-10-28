/**
 * 
 */
package org.openntf.domino.ext;

import org.openntf.domino.RichTextNavigator;

/**
 * @author withersp
 * 
 *         OpenNTF extensions to RichTextSection class
 */
public interface RichTextSection {

	/**
	 * Gets the parent RichTextItem of the RichTextSection
	 * 
	 * @return parent RichTextItem
	 * @since org.openntf.domino 1.0.0
	 */
	public RichTextNavigator getParent();

}
