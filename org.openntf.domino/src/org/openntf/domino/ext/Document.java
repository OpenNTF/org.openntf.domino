/**
 * 
 */
package org.openntf.domino.ext;

import java.util.Date;

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

}
