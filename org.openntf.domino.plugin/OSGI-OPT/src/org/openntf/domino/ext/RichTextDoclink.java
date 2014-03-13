/**
 * 
 */
package org.openntf.domino.ext;

/**
 * @author withersp
 * 
 */
public interface RichTextDoclink {
	/**
	 * @return the Database referenced by this doclink
	 */
	public org.openntf.domino.Database getDatabase();

	/**
	 * @return the Document referenced by this doclink, if applicable; null otherwise
	 */
	public org.openntf.domino.Document getDocument();

	/**
	 * @return the View referenced by this doclink, if applicable; null otherwise
	 */
	public org.openntf.domino.View getView();
}
