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

	public Item appendItemValue(String name, Object value, boolean unique);

	public String toJson(boolean compact);

	/**
	 * Gets the created date.
	 * 
	 * @return the created date
	 */
	public Date getCreatedDate();

	public String getFormName();

	public boolean hasReaders();

	public org.openntf.domino.Form getForm();

	/**
	 * Gets the initially modified date.
	 * 
	 * @return the initially modified date
	 */
	public Date getInitiallyModifiedDate();

	/**
	 * Gets the last accessed date.
	 * 
	 * @return the last accessed date
	 */
	public Date getLastAccessedDate();

	/**
	 * Gets the last modified date.
	 * 
	 * @return the last modified date
	 */
	public Date getLastModifiedDate();

	/**
	 * @return parent document for responses / response-to-responses
	 */
	public Document getParentDocument();

	/**
	 * @return whether or not the document has been changed, used for transactions
	 */
	public boolean isDirty();

	public MIMEEntity testMIMEEntity(final String name);

	public <T> T getItemValue(final String name, final Class<?> T) throws ItemNotFoundException, DataNotCompatibleException;

	public Item replaceItemValue(final String name, final Object value, final boolean isSummary);

	public boolean containsValue(final Object value, final String[] itemnames);

	public boolean containsValue(final Object value, final Collection<String> itemnames);

	public boolean containsValues(final Map<String, Object> filterMap);

	public String getMetaversalID();

	public String getMetaversalID(String serverName);

	// public <T> T getItemValue(String name, Class<?> T, ClassLoader loader) throws ItemNotFoundException, DataNotCompatibleException;

	public boolean forceDelegateRemove();

	public void rollback();

	public List<Item> getItems(org.openntf.domino.Item.Type type);

	public List<Item> getItems(org.openntf.domino.Item.Flags flags);

	/**
	 * Replaces the itemvalue in a document.
	 * 
	 * @param itemName
	 * @param value
	 * @param isSummary
	 * @param autoBox
	 * @param returnItem
	 * @return
	 */
	Item replaceItemValue(String itemName, Object value, Boolean isSummary, final boolean boxCompatibleOnly, boolean returnItem);

	public AutoMime getAutoMime();

	public void setAutoMime(final AutoMime value);

	public void markDirty();

	public Item getFirstItem(final String name, final boolean returnMime);
}
