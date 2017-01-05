/**
 * 
 */
package org.openntf.domino.ext;

/**
 * @author withersp
 * 
 *         OpenNTF extensions to RichTextDoclink class
 */
public interface RichTextDoclink {
	/**
	 * Gets the Database ob ject referenced by this doclink
	 * 
	 * @return Database referenced by this doclink
	 * @since org.openntf.domino 4.5.0
	 */
	public org.openntf.domino.Database getDatabase();

	/**
	 * Gets the Document referenced by this doclink, if applicable; null otherwise
	 * 
	 * @return Document or null
	 * @since org.openntf.domino 4.5.0
	 */
	public org.openntf.domino.Document getDocument();

	/**
	 * Gets the View referenced by this doclink, if applicable; null otherwise
	 * 
	 * @return View or null
	 * @since org.openntf.domino 5.0.0
	 */
	public org.openntf.domino.View getView();
}
