/**
 * 
 */
package org.openntf.domino.ext;

import java.util.Map;

import org.openntf.domino.DocumentCollection;

/**
 * @author withersp
 * 
 *         OpenNTF extensions to View class
 */
public interface View {

	/**
	 * Gets all documents from the View.
	 * 
	 * <p>
	 * NOTE: This doesn't take into account any searching done over the view. It also iterates the NoteIDs from the entries in the view and
	 * adds those documents to a collection. Although a convenience method, in all likelihood the recommended approach would be to use
	 * getAllEntries and then, while iterating the entries, call getDocument().
	 * </p>
	 * 
	 * <p>
	 * One use case for this is if you need to pass a DocumentCollection to a method, e.g.
	 * org.openntf.domino.helpers.DocumentSyncHelper.process(DocumentCollection)
	 * </p>
	 * 
	 * @return DocumentCollection
	 * @since org.openntf.domino 1.0.0
	 */
	public DocumentCollection getAllDocuments();

	/**
	 * Gets all documents from the View as a NoteCollection.
	 * 
	 * <p>
	 * NOTE: This may not be particularly performant at this time so has been deprecated.
	 * </p>
	 * 
	 * @return NoteCollection
	 * @since org.openntf.domino 3.0.0
	 */
	public NoteCollection getNoteCollection();

	/**
	 * Gets the XPage the view is designed to open using, on View Properties Advanced tab (beanie image)
	 * 
	 * @return String XPage name the View will launch with on web
	 * @since org.openntf.domino 4.5.0
	 */
	public String getXPageAlt();

	/**
	 * Checks whether or not the View has been indexed
	 * 
	 * @return boolean if indexed
	 * @since org.openntf.domino 4.5.0
	 */
	public boolean isIndexed();

	/**
	 * Checks whether the source document is unique within the view
	 * 
	 * @param srcDoc
	 *            Document the developer wants to check is unique
	 * @param key
	 *            Object to be checked against the sorted View
	 * @return whether the document is unique or not
	 * @since org.openntf.domino 5.0.0
	 */
	public boolean checkUnique(final Object key, final org.openntf.domino.Document srcDoc);

	public Map<String, org.openntf.domino.ViewColumn> getColumnMap();

}
