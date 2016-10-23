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

	/**
	 * Gets a map of the ViewColumn objects that comprise this view
	 * 
	 * @return Map of ViewColumn objects
	 * @since org.openntf.domino 5.0.0
	 */
	public Map<String, org.openntf.domino.ViewColumn> getColumnMap();

	/**
	 * Interrogates the flags in the design note to identify the index type for the View
	 * 
	 * @return IndexType for the view, e.g. IndexType.SHARED, IndexType.SHAREDPRIVATEONSERVER
	 * @since org.openntf.domino 5.0.0
	 */
	public IndexType getIndexType();

	/**
	 * Interrogates the design note for the $FormulaTV field. This exists if a time-specific formula is included in the view, i.e. "@Today"
	 * or "@Now". This severely impacts performance and developers using this should be re-educated on better practice alternatives
	 * 
	 * @return Whether the view includes any time-sensitive formulas
	 * @since org.openntf.domino 5.0.0
	 */
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

	/**
	 * Interrogates the design note's $Index field for the /L flag. This disables auto-update so users will need to manually refresh the
	 * view. This can be done via the refresh indicator in Notes Client, but a mechanism needs coding for XPages.
	 * 
	 * @return whether the view has auto update disabled
	 * @since org.openntf.domino 5.0.0
	 */
	public boolean isDisableAutoUpdate();

	/**
	 * Interrogates the design note's $Index field for the /C flag. This exists if "Hide empty categories" is switched on. This prevents
	 * categories being displayed if the user does not have access to any of the documents within that category.
	 * 
	 * @return whether empty categories are hidden when accessing via UI
	 * @since org.openntf.domino 5.0.0
	 */
	public boolean isHideEmptyCategories();

	/**
	 * Interrogates the design note's $Index field for the /T flag. This exists if the index should be discarded after each use.
	 * 
	 * @return whether the view index is discarded after each use
	 * @since org.openntf.domino 5.0.0
	 */
	public boolean isDiscardIndex();

	/**
	 * Interrogates the design note's $Index field for the /M flag. This exists if the view has to be manually refreshed. This can be done
	 * via the refresh indicator in Notes Client, but a mechanism needs coding for XPages.
	 * 
	 * @return whether manual refresh of the view is required
	 * @since org.openntf.domino 5.0.0
	 */
	public boolean isManualRefresh();

	/**
	 * Interrogates the design note's $Index field for the /O flag. This exists if the view is set to be automatically refreshed
	 * 
	 * @return whether automatic refresh of the view is in place
	 * @since org.openntf.domino 5.0.0
	 */
	public boolean isAutomaticRefresh();

	/**
	 * Interrogates the design note's $Index field to see if the view index is Auto after first use, the default.
	 * 
	 * @return whether auto after first use index is in place
	 * @since org.openntf.domino 5.0.0
	 */
	public boolean isAutoRefreshAfterFirstUse();

	/**
	 * Interrogates the design note's $Index field for the /R flag and gets the integer after the equals. The existence of the flag
	 * identifies the refresh setting as "Auto, at most every". The number is the amount of seconds between automatic refreshes of the view.
	 * 
	 * @return number of seconds between auto-refreshes
	 * @since org.openntf.domino 5.0.0
	 */
	public int getAutoRefreshSeconds();

	/**
	 * Interrogates the design note's $Index field for the /P flag and gets the integer after the equals. This is the number of hours before
	 * a view index is discarded.
	 * 
	 * @return number of hours until the index is discarded
	 * @since org.openntf.domino 5.0.0
	 */
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

	public ViewEntry getEntryAtPosition(final String position, final char separator);

	public ViewEntry getEntryAtPosition(final String position);

	public boolean containsDocument(Document doc);

	public boolean containsEntry(ViewEntry entry);

	public String getMetaversalID();

}
