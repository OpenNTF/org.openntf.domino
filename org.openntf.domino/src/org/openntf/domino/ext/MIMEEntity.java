/**
 * 
 */
package org.openntf.domino.ext;

/**
 * @author withersp
 * 
 *         OpenNTF extensions to MIMEEntity class
 */
public interface MIMEEntity {

	/**
	 * Sets the underlying document dirty.
	 */
	public void markDirty();

	/**
	 * This method initializes the name of this MimeEntity, so that the entity knwos it's field name. Should NOT be called in your code!
	 * 
	 * @param itemName
	 */
	public void initItemName(String itemName);

	/**
	 * Returns the according ItemName of this entity
	 * 
	 * @return
	 */
	public String getItemName();
}
