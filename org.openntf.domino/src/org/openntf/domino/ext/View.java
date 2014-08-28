/**
 * 
 */
package org.openntf.domino.ext;

import java.util.Map;
import java.util.Vector;

import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.View.IndexType;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.annotations.Legacy;

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

	public IndexType getIndexType();

	public boolean isTimeSensitive();

	/*
	'/P=' + the number of hours until discarding of the view index. 
	'/T' Discard view index after each use. 
	'/M' Manual refresh. 
	'/O' Automatic refresh. 
	'/R=' + the number of seconds between automatically refresh of view.
	'/C' Don't show empty categories
	'/L' Disable auto-update
	*/

	public boolean isDisableAutoUpdate();

	public boolean isHideEmptyCategories();

	public boolean isDiscardIndex();

	public boolean isManualRefresh();

	public boolean isAutomaticRefresh();

	public int getAutoRefreshSeconds();

	public int getDiscardHours();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getDocumentByKey(java.lang.Object)
	 * The original method is poorly named, as it doesn't indicate what happens when more than one
	 * Document matches the key.
	 */
	public Document getFirstDocumentByKey(final Object key);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getDocumentByKey(java.lang.Object, boolean)
	 * The original method is poorly named, as it doesn't indicate what happens when more than one
	 * Document matches the key.
	 */
	public Document getFirstDocumentByKey(final Object key, final boolean exact);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getDocumentByKey(java.util.Vector)
	 * The original method is poorly named, as it doesn't indicate what happens when more than one
	 * Document matches the key.
	 */
	@SuppressWarnings("rawtypes")
	@Legacy({ Legacy.GENERICS_WARNING })
	public Document getFirstDocumentByKey(final Vector keys);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getDocumentByKey(java.util.Vector, boolean)
	 * The original method is poorly named, as it doesn't indicate what happens when more than one
	 * Document matches the key.
	 */
	@SuppressWarnings("rawtypes")
	@Legacy({ Legacy.GENERICS_WARNING })
	public Document getFirstDocumentByKey(final Vector keys, final boolean exact);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getEntryByKey(java.lang.Object)
	 * The original method is poorly named, as it doesn't indicate what happens when more than one
	 * Document matches the key.
	 */
	public ViewEntry getFirstEntryByKey(final Object key);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getEntryByKey(java.lang.Object, boolean)
	 * The original method is poorly named, as it doesn't indicate what happens when more than one
	 * ViewEntry matches the key.
	 */
	public ViewEntry getFirstEntryByKey(final Object key, final boolean exact);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getEntryByKey(java.util.Vector)
	 * The original method is poorly named, as it doesn't indicate what happens when more than one
	 * ViewEntry matches the key.
	 */
	@SuppressWarnings("rawtypes")
	@Legacy(Legacy.GENERICS_WARNING)
	public ViewEntry getFirstEntryByKey(final Vector keys);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getEntryByKey(java.util.Vector, boolean)
	 * The original method is poorly named, as it doesn't indicate what happens when more than one
	 * ViewEntry matches the key.

	 */
	@SuppressWarnings("rawtypes")
	@Legacy(Legacy.GENERICS_WARNING)
	public ViewEntry getFirstEntryByKey(final Vector keys, final boolean exact);

}
