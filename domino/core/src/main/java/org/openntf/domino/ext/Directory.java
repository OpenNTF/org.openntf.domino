/**
 *
 */
package org.openntf.domino.ext;

import org.openntf.domino.Session;

/**
 * OpenNTF extensions to Directory class
 *
 * @author withersp
 *
 */
public interface Directory {

	/**
	 * Gets the parent session for the Directory
	 *
	 * @return Session parent
	 * @since org.openntf.domino 1.0.0
	 */
	public Session getParent();

}
