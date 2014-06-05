/**
 * 
 */
package org.openntf.domino.ext;

import org.openntf.domino.Session;

/**
 * @author withersp
 * 
 *         OpenNTF extensions to Stream class
 * 
 */
public interface Stream {

	/**
	 * Gets the Session that is the parent of the Stream
	 * 
	 * @return parent Session
	 * @since org.openntf.domino 1.0.0
	 */
	public Session getParent();

}
