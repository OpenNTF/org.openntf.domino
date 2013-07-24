/**
 * 
 */
package org.openntf.domino.ext;

import java.util.Date;

import org.openntf.domino.exceptions.DataNotCompatibleException;
import org.openntf.domino.exceptions.ItemNotFoundException;

/**
 * @author withersp
 * 
 */
public interface Document {

	/**
	 * Gets the created date.
	 * 
	 * @return the created date
	 */
	public Date getCreatedDate();

	public String getFormName();

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

	public boolean hasMIMEEntity(final String name);

	public <T> T getItemValue(final String name, final Class<?> T) throws ItemNotFoundException, DataNotCompatibleException;

	// public <T> T getItemValue(String name, Class<?> T, ClassLoader loader) throws ItemNotFoundException, DataNotCompatibleException;

}
