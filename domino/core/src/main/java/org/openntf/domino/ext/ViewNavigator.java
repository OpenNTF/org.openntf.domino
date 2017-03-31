/**
 * 
 */
package org.openntf.domino.ext;

import org.openntf.domino.ViewEntry;

/**
 * @author withersp
 * 
 *         OpenNTF extension to ViewNavigator
 */
public interface ViewNavigator {
	public ViewEntry getNthDocument(final int nth);
}
