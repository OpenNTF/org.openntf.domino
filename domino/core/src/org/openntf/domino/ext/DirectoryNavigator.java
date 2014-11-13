/**
 * 
 */
package org.openntf.domino.ext;

import org.openntf.domino.Directory;

/**
 * @author withersp
 * 
 *         OpenNTF extensions to DirectoryNavigator class
 * 
 */
public interface DirectoryNavigator {

	/**
	 * Gets the parent Directory of the navigator
	 * 
	 * @return Directory parent
	 * @since org.openntf.domino 2.5.0
	 */
	public Directory getParent();

}
