/**
 * 
 */
package org.openntf.domino.ext;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openntf.domino.AutoMime;
import org.openntf.domino.Item;
import org.openntf.domino.exceptions.DataNotCompatibleException;
import org.openntf.domino.exceptions.ItemNotFoundException;

/**
 * @author withersp
 * 
 */
public interface Document {

	/**
	 * Appends a value to an existing Item, with an option to only add it if the value does not already exist in the Item.<br/>
	 * The method is only useful if a single value is being appended, because it use containsValue(value) This allows the developer to avoid
	 * duplicate values but also avoid unnecessary saves with transactional processing
	 * 
	 * @param name
	 *            String Item name
	 * @param value
	 *            Object value
	 * @param unique
	 *            Boolean whether to only add the value if the Item does not already hold the value
	 * @return Item being updated
	 * @since org.openntf.domino 5.0.0
	 */
	public Item appendItemValue(String name, Object value, boolean unique);

	/**
	 * Converts the Document to JSON, with whether or not to compact the resulting JSON, removing spaces that would make the output more
	 * readable
	 * 
	 * @param compact
	 *            Boolean whether or not to compact
	 * @return String JSON output of the Document
	 * @since org.openntf.domino 2.5.0
	 */
	public String toJson(boolean compact);

	/**
	 * Gets the created date of the Document, returning as a Java Date instead of a DateTime
	 * 
	 * @return Date the document was created
	 * @since org.openntf.domino 1.0.0
	 */
	public Date getCreatedDate();

	/**
	 * Gets the Form name the Document is based upon
	 * 
	 * @return String form name
	 * @since org.openntf.domino 4.5.0
	 */
	public String getFormName();

	/**
	 * Returns whether the Document is restricted to specific Readers or visible to anyone who has access to the database.
	 * 
	 * @return Boolean whether or not Readers restrictions apply
	 * @since org.openntf.domino 5.0.0
	 */
	public boolean hasReaders();

	/**
	 * Gets the Form design element the Document is based upon
	 * 
	 * @return Form design element
	 * @since org.openntf.domino 4.5.0
	 */
	public org.openntf.domino.Form getForm();

	/**
	 * Gets the initially modified date as a Java Date rather than a DateTime
	 * 
	 * @return the initially modified date
	 * @since org.openntf.domino 1.0.0
	 */
	public Date getInitiallyModifiedDate();

	/**
	 * Gets the last accessed date as a Java Date rather than a DateTime
	 * 
	 * @return Date the document was last accessed
	 * @since org.openntf.domino 1.0.0
	 */
	public Date getLastAccessedDate();

	/**
	 * Gets the last modified date.
	 * 
	 * @return Date the document was last modified
	 */
	public Date getLastModifiedDate();

	/**
	 * Gets the parent Document if the current Document is a response / response to response / conflict
	 * 
	 * @return Document that is the parent
	 * @since org.openntf.domino 2.5.0
	 */
	public Document getParentDocument();

	/**
	 * Identifies whether any Item on the Document has been updated, used by transactional processing to avoid unnecessary saves.
	 * 
	 * @return Boolean whether or not the document has been changed
	 * @since org.openntf.domino 2.5.0
	 */
	public boolean isDirty();

	/**
	 * Tests whether a MIMEEntity exists on the Document and, if so, returns it. This exists because you have to switch off auto-conversion
	 * of MIME in order for hasMIMEEntity() to return true.
	 * 
	 * @return MIMEEntity or null, if no MIMEEntity exists
	 * @since org.openntf.domino 5.0.0
	 * @deprecated pending update from NTF
	 */
	@Deprecated
	public MIMEEntity testMIMEEntity(final String name);

	/**
	 * Gets an Item value, casting it to a specific class, e.g. java.util.ArrayList.class
	 * 
	 * @param name
	 *            Item name to retrieve the value from
	 * @param T
	 *            Java class to cast value to
	 * @return Java object of type T, containing the values from the object
	 * @throws ItemNotFoundException
	 *             if the Item does not exist
	 * @throws DataNotCompatibleException
	 *             if the values cannot be cast to the Java class T
	 * @since org.openntf.domino 2.5.0
	 */
	public <T> T getItemValue(final String name, final Class<?> T) throws ItemNotFoundException, DataNotCompatibleException;

	/**
	 * Replaces an item value, setting the Item as Summary type or not
	 * 
	 * @param name
	 *            Item to replace the value of
	 * @param value
	 *            Object
	 * @param isSummary
	 * @return
	 */
	public Item replaceItemValue(final String name, final Object value, final boolean isSummary);

	/**
	 * Checks whether or not a Document contains a specific value, restricting the search to specific Item names
	 * 
	 * @param value
	 *            Object value to check for
	 * @param itemnames
	 *            String[] items to look through
	 * @return Boolean whether or not one of the Items contains the value
	 * @since org.openntf.domino 4.5.0
	 */
	public boolean containsValue(final Object value, final String[] itemnames);

	/**
	 * Checks whether or not a Document contains a specific value, restricting the search to specific Item names
	 * 
	 * @param value
	 *            Object value to check for
	 * @param itemnames
	 *            Collection<String> items to look through
	 * @return Boolean whether or not one of the Items contains the value
	 * @since org.openntf.domino 4.5.0
	 */
	public boolean containsValue(final Object value, final Collection<String> itemnames);

	/**
	 * Receives a Map where the key is an Item name and the value is a value to look for in the relevant Item. If one key/value pair matches
	 * (so one of the Items contains the value to look for), the method returns true.<br/>
	 * <br/>
	 * <code>
	 * 	HashMap<String,Object> check = new Map<String,Object>();
	 *  check.put("Form","Person");
	 *  check.put("FirstName","Fred");
	 *  return doc.containsValues(check);
	 * </code> Returns true if the relevant Document's Form item contains "Person" or the Document's FirstName item contains "Fred".<br/>
	 * Returns false if the relevant Document's Form does not contain "Person" and the Document's FirstName item does not contain "Fred".
	 * 
	 * @param filterMap
	 *            Map<String,Object> to check
	 * @return Boolean whether one of the items contains a value
	 * @since org.openntf.domino 4.5.0
	 */
	public boolean containsValues(final Map<String, Object> filterMap);

	/**
	 * Gets the metaversal ID, which appends replicaID + documentID
	 * 
	 * @return String metaversal ID
	 * @since org.openntf.domino 5.0.0
	 */
	public String getMetaversalID();

	/**
	 * Gets the metaversal ID specific to a server, so appending serverName + "!!" + replicaID + documentID
	 * 
	 * @param serverName
	 *            String server name to add to metaversal ID
	 * @return String server-specific metaversal ID
	 * @since org.openntf.domino 5.0.0
	 */
	public String getMetaversalID(String serverName);

	// public <T> T getItemValue(String name, Class<?> T, ClassLoader loader) throws ItemNotFoundException, DataNotCompatibleException;

	/**
	 * Attempts to force deletion of the document, using soft deletion, if enabled
	 * 
	 * @return Boolean success or failure of attempt to remove the Document
	 * @since org.openntf.domino 5.0.0
	 */
	public boolean forceDelegateRemove();

	/**
	 * Rolls back any changes to the Document
	 * 
	 * @since org.openntf.domino 5.0.0
	 */
	public void rollback();

	/**
	 * Gets a list of all Items on the Document matching a specific Type, using {@link org.openntf.domino.Item.Type}
	 * 
	 * @param type
	 *            Item.Type to look for
	 * @return List<Item> matching the specific Item.Type
	 * @since org.openntf.domino 5.0.0
	 */
	public List<Item> getItems(org.openntf.domino.Item.Type type);

	/**
	 * Gets a list of all Items on the Document matching a specific Flag, using {@link org.openntf.domino.Item.Flags}
	 * 
	 * @param type
	 *            Item.Flags to look for
	 * @return List<Item> matching the specific Item.Flags
	 * @since org.openntf.domino 5.0.0
	 */
	public List<Item> getItems(org.openntf.domino.Item.Flags flags);

	/**
	 * Replaces the item value in a document, deciding whether the Item should have the Summary flag set, whether the value should be
	 * auto-boxed, and whether to return the Item
	 * 
	 * @param itemName
	 *            String Item name
	 * @param value
	 *            Object value to set
	 * @param isSummary
	 *            Boolean whether the Item should be set to Summary
	 * @param autoBox
	 *            Boolean whether the value should be autoboxed
	 * @param returnItem
	 *            Boolean whether the Item should be returned
	 * @return Item or null, if final parameter is false
	 * @since org.openntf.domino 5.0.0
	 */
	Item replaceItemValue(String itemName, Object value, Boolean isSummary, final boolean boxCompatibleOnly, boolean returnItem);

	/**
	 * Retrieves whether or not the session is auto-mime-enabled
	 * 
	 * @return AutoMime enum
	 * @since org.openntf.domino 5.0.0
	 */
	public AutoMime getAutoMime();

	/**
	 * Sets whether the session should be auto-mime-enabled, using {@link org.openntf.domino.AutoMime}
	 * 
	 * @param value
	 *            AutoMime whether mime should be enabled or not
	 * @since org.openntf.domino 5.0.0
	 */
	public void setAutoMime(final AutoMime value);

	/**
	 * Mark the document as dirty
	 * 
	 * @since org.openntf.domino 5.0.0
	 */
	public void markDirty();

	/**
	 * Gets an Item, determining whether or not AutoMime should be switched back on at the end of the process
	 * 
	 * @param name
	 *            String Item name
	 * @param returnMime
	 *            Boolean whether AutoMime should always be on
	 * @return Item if found on the Document
	 * @since org.openntf.domino 5.0.0
	 */
	public Item getFirstItem(final String name, final boolean returnMime);
}
