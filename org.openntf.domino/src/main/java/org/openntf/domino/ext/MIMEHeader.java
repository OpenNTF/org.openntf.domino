/**
 * 
 */
package org.openntf.domino.ext;

import org.openntf.domino.MIMEEntity;

/**
 * @author withersp
 * 
 *         OpenNTF extensions to MIMEHeader class
 */
public interface MIMEHeader {

	/**
	 * Gets the MIMEEntity that is the parent of this MIMEHeader
	 * 
	 * @return parent MIMEEntity
	 * @since org.openntf.domino 1.0.0
	 */
	public MIMEEntity getParent();

}
