/**
 * 
 */
package org.openntf.domino.ext;

import org.openntf.domino.DocumentCollection;

/**
 * @author withersp
 * 
 */
public interface View {

	/**
	 * Gets all documents from the View. NOTE: This doesn't take into account any searching done over the view. It also iterates the NoteIDs
	 * from the entries in the view and adds those documents to a collection. Although a convenience method, in all likelihood the
	 * recommended approach would be to use getAllEntries and then, while iterating the entries, call getDocument().
	 * 
	 * @return DocumentCollection
	 */
	public DocumentCollection getAllDocuments();

	/**
	 * Gets all documents from the View. NOTE: This doesn't take into account any searching done over the view.
	 * 
	 * @return NoteCollection
	 */
	public NoteCollection getNoteCollection();

	public String getXPageAlt();

	public boolean isIndexed();

	/**
	 * Checks whether the source document is unique within the view
	 * 
	 * @param srcDoc
	 *            Document the developer wants to check is unique
	 * @param key
	 *            Object to be checked against the sorted View
	 * @return whether the document is unique or not
	 */
	public boolean checkUnique(final Object key, final org.openntf.domino.Document srcDoc);

}
