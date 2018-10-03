/**
 *
 */
package org.openntf.domino.ext;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openntf.domino.AutoMime;
import org.openntf.domino.DateTime;
import org.openntf.domino.Item;
import org.openntf.domino.big.NoteCoordinate;
import org.openntf.domino.exceptions.DataNotCompatibleException;
import org.openntf.domino.exceptions.ItemNotFoundException;
import org.openntf.domino.types.Design;

/**
 * OpenNTF extensions to Document class
 *
 * @author withersp
 *
 *
 */
public interface Document {

	/**
	 * Appends a value to an existing Item, with an option to only add it if the value does not already exist in the Item.
	 *
	 * <p>
	 * The method is only useful if a single value is being appended, because it use containsValue(value) This allows the developer to avoid
	 * duplicate values but also avoid unnecessary saves with transactional processing.
	 * </p>
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
	 * readable.
	 * <p>
	 * Result contains a @unid property with document's Universal ID. Multivalue items are output as one property with string value with a
	 * semicolon as a separator.
	 * </p>
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
	 *
	 * @return A List of the attachments from the document's Rich Text items, as EmbeddedObjects
	 */
	public List<org.openntf.domino.EmbeddedObject> getAttachments();

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
	public org.openntf.domino.Document getParentDocument();

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
	public <T> T getItemValue(final String name, final Class<T> type) throws ItemNotFoundException, DataNotCompatibleException;

	/**
	 * Gets an Item value as list, Elements in list are casted it to a specific class, e.g. java.lang.String.class
	 *
	 * @param name
	 *            Item name to retrieve the value from
	 * @param T
	 *            Java class to cast value to
	 * @return Java object of type List<T>, containing the values from the object
	 * @throws ItemNotFoundException
	 *             if the Item does not exist
	 * @throws DataNotCompatibleException
	 *             if the values cannot be cast to the Java class T
	 * @since org.openntf.domino 2.5.0
	 */
	public <T> List<T> getItemValues(final String name, final Class<T> type) throws ItemNotFoundException, DataNotCompatibleException;

	/**
	 * Replaces an item value, setting the Item as Summary type or not
	 *
	 * @param name
	 *            Item to replace the value of
	 * @param value
	 *            Object
	 * @param isSummary
	 *            Passed to the Item's setSummary method directly
	 * @return An Item object for the resultant set item.
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
	 * Receives a Map where the key is an Item name and the value is a value to look for in the relevant Item. If all key/value pairs match
	 * (so all of the Items contain the value to look for), the method returns true.
	 *
	 * <pre>
	 * HashMap<String, Object> check = new Map<String, Object>();
	 * check.put("Form", "Person");
	 * check.put("FirstName", "Fred");
	 * return doc.containsValues(check);
	 * </pre>
	 *
	 * <p>
	 * Returns true if the relevant Document's Form item contains "Person" and the Document's FirstName item contains "Fred".<br/>
	 * Returns false if the relevant Document's Form does not contain "Person" or the Document's FirstName item does not contain "Fred".
	 * </p>
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

	public NoteCoordinate getNoteCoordinate();

	/**
	 * Gets the metaversal ID specific to a server, so appending serverName + "!!" + replicaID + documentID
	 *
	 * @param serverName
	 *            String server name to add to metaversal ID
	 * @return String server-specific metaversal ID
	 * @since org.openntf.domino 5.0.0
	 */
	public String getMetaversalID(String serverName);

	// public <T> T getItemValue(String name, Class<T> type, ClassLoader loader) throws ItemNotFoundException, DataNotCompatibleException;

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

	//	public void writeBinaryChunk(String name, int chunk, byte[] data);

	/**
	 * Writes binary data divided to parts according to chunkSize. Use {@link Document#readBinary(String)} method to read the data back.
	 * <p>
	 * Data is written in blocks to items name, name$1, name$2..name$n.
	 * </p>
	 *
	 * @param name
	 *            Name of the item to write data to
	 * @param data
	 *            Binary data to write
	 * @param chunkSize
	 *            Size of the individual blocks in bytes. It is set to 64 kB if the chunkSize is less than 1024.
	 */
	public void writeBinary(String name, byte[] data, int chunkSize);

	/**
	 * Reads binary data from an item.
	 * <p>
	 * Data written using {@link #writeBinary(String, byte[], int)} is written to multiple items. This method allows to read one part of the
	 * binary data.
	 * </p>
	 *
	 * @param name
	 *            Name of the item
	 * @param chunk
	 *            Serial number of the block to read starting from 0
	 * @return Content of the item name$<chunk>
	 */
	public byte[] readBinaryChunk(String name, int chunk);

	/**
	 * Reads binary data from an item. Handles data written in chunks written previously with {@link #writeBinary(String, byte[], int)}.
	 *
	 * @param name
	 *            Name of the item with binary data to read.
	 * @return Contents of the item with name 'name'
	 */
	public byte[] readBinary(String name);

	/**
	 * Retrieves values of multiple items with a numbered suffix.
	 * <p>
	 * When there are items with the same prefix and a numbered suffix like CoDebtorName_0, CoDebtorName_1, CoDebtorName_2 and so on use
	 * this method to retrieve values of all such items at once.<br/>
	 * Suffixes must be separated by an underscore character ("_") and they must form a continuous sequence of numbers. They cannot be
	 * formatted in any way like 00, 01, 02 and so on.
	 * </p>
	 * <p>
	 * <code>List coDebtors = document.getItemSeriesValues("CoDebtorName")</code> - will retrieve values from above mentioned items<br/>
	 * <code>List coDebtors = document.getItemSeriesValues("CoDebtor_Name")</code> - underscore is also possible in the item's name<br/>
	 * <code>List coDebtors = document.getItemSeriesValues("CoDebtor_Name_")</code> - will look for items CoDebtor_Name__0, CoDebtor_Name__1
	 * (with two underscores actually)
	 * </p>
	 *
	 * @param name
	 *            Prefix of the series name. Can contain an underscore character as part of the item's name.
	 * @return List of values from items named as &ltname&gt;_0..&lt;name&gt;_n
	 */
	public List<?> getItemSeriesValues(CharSequence name);

	/**
	 * Retrieves values of multiple items with a numbered suffix casted to a specific object type.
	 *
	 * @param name
	 *            Prefix of the series name. Can contain an underscore character as part of the item's name.
	 * @param type
	 *            Class to which the values will be cast
	 * @return List of values from items named as &ltname&gt;_0..&lt;name&gt;_n as instance of <code>type</code>
	 * @see Document#getItemSeriesValues(CharSequence)
	 */
	public <T> T getItemSeriesValues(CharSequence name, Class<T> type);

	/**
	 * When there are multivalue items which together form a table, use this method to retrieve all item values <b>organized by columns</b>.
	 * <p>
	 * This technique is sometimes used when you need to dynamically store multiple different information instead of saving it in separate
	 * documents. All items have the same number of values.
	 * </p>
	 * Returned Map contains item names as keys (the columns) and items values as a value (the rows)
	 * <p>
	 * Consider a situation where there are co-debtors in a loan application. For each co-debtor you need to store a name, registry ID and a
	 * UNID. You would then have three multivalue items CoDebtorName, CoDebtorID, CoDebtorUNID, each co-debtor with a corresponding value in
	 * each of the items.
	 * <table>
	 * <tr>
	 * <th>CoDebtorName</th>
	 * <th>CoDebtorID</th>
	 * <th>CoDebtorUNID</th>
	 * </tr>
	 * <tr>
	 * <td>Company A</td>
	 * <td>111111</td>
	 * <td>838B9552F45B3B89882580D50026A72F</td>
	 * </tr>
	 * <tr>
	 * <td>Company B</td>
	 * <td>222222</td>
	 * <td>435B9F52F45B3B89882580D50026B83E</td>
	 * </tr>
	 * <tr>
	 * <td>Company C</td>
	 * <td>333333</td>
	 * <td>633B9F43245B3B89882580D50049BC3A</td>
	 * </tr>
	 * </table>
	 * </p>
	 * calling getItemTable() will then return this map:<br/>
	 *
	 * <pre>
	 * {
	 * 	CoDebtorName=[Company A, Company B, Company C],
	 * 	CoDebtorID=[111111, 222222, 333333],
	 * 	CoDebtorUNID=[838B9552F45B3B89882580D50026A72F, 435B9F52F45B3B89882580D50026B83E, 633B9F43245B3B89882580D50049BC3A]
	 * }
	 * </pre>
	 *
	 * @param itemnames
	 *            Variable number of item names (the columns) to be retrieved
	 * @return Map of values for given item names
	 */
	public Map<String, List<Object>> getItemTable(CharSequence... itemnames);

	/**
	 * When there are multivalue items which together form a table, use this method to retrieve all item values <b>organized by rows</b>.
	 * Unlike the {@link #getItemTable(CharSequence...) getItemTable} which organizes values by columns (meaning individual items) this
	 * method creates a list of maps. Each map in the list is basically a row in the table. Values in the map are keyed by column (item)
	 * name. Using the same example from {@link #getItemTable(CharSequence...) getItemTable} calling this method returns
	 *
	 * <pre>
	 *	{CoDebtorName=Company A, CoDebtorID=111111, CoDebtorUNID=838B9552F45B3B89882580D50026A72F}
	 *	{CoDebtorName=Company B, CoDebtorID=222222, CoDebtorUNID=435B9F52F45B3B89882580D50026B83E}
	 *	{CoDebtorName=Company C, CoDebtorID=333333, CoDebtorUNID=633B9F43245B3B89882580D50049BC3A}
	 * </pre>
	 *
	 * @param itemnames
	 *            Variable number of item names (the columns) to be retrieved
	 * @return List of maps with item values organized by rows. First map in the list contains first values from all items, second map
	 *         contains second values and so on
	 * @see #getItemTable(CharSequence...)
	 */
	public List<Map<String, Object>> getItemTablePivot(final CharSequence... itemnames);

	/**
	 * Replace multiple items at once. Opposite operation to {@link #getItemTable(CharSequence...) getItemTable}. Keys in map are name of
	 * the items. Current values will be overwritten.
	 * <p>
	 * Use {@link #getItemTable(CharSequence...) getItemTable} method to read the table first, modify it and write it back using this
	 * method.
	 * </p>
	 *
	 * @param table
	 *            Map of values keyed by item names
	 * @see #getItemTable(CharSequence...)
	 */
	public void setItemTable(final Map<String, List<Object>> table);

	/**
	 * Replace multiple items at once. Opposite operation to {@link #getItemTablePivot(CharSequence...) getItemTablePivot}. Keys in map are
	 * name of the items. Current values will be overwritten.
	 *
	 * <h5>Code Snippet</h5>
	 *
	 * <pre>
	 * List&lt;Map&lt;String, Object&gt;&gt; pivot = doc.getItemTablePivot("CoDebtorName", "CoDebtorID", "CoDebtorUNID");
	 * Map&lt;String, Object&gt; newValue = new HashMap&lt;String, Object&gt;
	 * newValue.put("CoDebtorName", "Company D");
	 * newValue.put("CoDebtorID", "44444444");
	 * newValue.put("CoDebtorUNID", "XXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
	 * pivot.add(newValue);
	 *
	 * doc.setItemTablePivot(pivot);
	 * doc.save(true, false);
	 * </pre>
	 *
	 * @param pivot
	 *            List of maps with values keyed by item name. Values should be single values.
	 * @see #getItemTablePivot(CharSequence...)
	 */
	public void setItemTablePivot(final List<Map<String, Object>> pivot);

	/**
	 * Returns item's value as an instance of a Name object.
	 *
	 * @param itemName
	 *            Name of the item with a single Notes Name value
	 * @return Instance of a Name object representing the name
	 */
	public Name getItemValueName(String itemName);

	/**
	 * Returns a document type to distinguish whether the document represents an actual document or a form, view, agent and so on. Classes
	 * implementing the {@link Design} interface implement the {@link Design#getDocument()} method to return the design element as a
	 * document (so you can read design information as item values). This method allows to identify the element type.
	 *
	 * @return type of the document
	 * @see NoteClass enumeration for possible values
	 */
	public NoteClass getNoteClass();

	/**
	 * If the instance represents a design element, this method determines whether it is the default element of its class. For example one
	 * form can be marked as "Default database form", View can be set as "Default when database is first opened" and so on.
	 *
	 * @return true if the design element this document represents is a default element for its type
	 */
	boolean isDefault();

	/**
	 * If the instance represents a design element, this method determines whether it is private.
	 *
	 * @return true if the the design element this document represents is private.
	 */
	boolean isPrivate();

	/**
	 * Add a separate $REF field to display a document in a hierarchical view. NOTE: you must add code to the select formula in the view
	 * that copies the value of <code>itemName</code> (=$REFxxx) back to original $REF field for these documents. See this example:
	 *
	 * <pre>
	 *  &#64;If(Form="YourForm"; FIELD $REF := $REFxxx; "");
	 *  SELECT Form = ...
	 * </pre>
	 */
	public void makeResponse(final lotus.domino.Document doc, String itemName);

	/**
	 * When setting the universal id, also include a $Created item with the current date/time so that
	 *
	 * <pre>
	 * &#64;Created
	 * </pre>
	 *
	 * works correctly
	 */
	public void setUniversalID(final CharSequence unid, boolean includeCreated);

	public boolean isEditable();

	public List<Item> getItemsModifiedSince(final DateTime datetime);
}
