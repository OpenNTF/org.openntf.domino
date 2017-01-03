/**
 * 
 */
package org.openntf.domino.ext;

import org.openntf.domino.EmbeddedObject;

/**
 * @author withersp
 * 
 *         OpenNTF extensions to RichTextItem class
 */
public interface RichTextItem {

	/**
	 * Sets the underlying document dirty.
	 */
	public void markDirty();

	public EmbeddedObject replaceAttachment(final String filename, final String sourcePath);

	public boolean removeAttachment(final String filename);
}
