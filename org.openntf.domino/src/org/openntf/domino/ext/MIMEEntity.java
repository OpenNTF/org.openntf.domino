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
	 * Returns the according ItemName of this entity
	 * 
	 * @return
	 */
	public String getItemName();
}
