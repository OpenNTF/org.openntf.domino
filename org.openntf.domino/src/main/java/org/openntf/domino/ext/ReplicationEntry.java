/**
 * 
 */
package org.openntf.domino.ext;

import java.util.Collection;

import org.openntf.domino.Replication;

/**
 * @author withersp
 * 
 *         OpenNTF extensions to ReplicationEntry class
 */
public interface ReplicationEntry {

	/**
	 * Gets the parent Replication object of the entry
	 * 
	 * @return Replication parent
	 * @since org.openntf.domino 1.0.0
	 */
	public Replication getParent();

	/**
	 * Loads a collection of Views to be applied to the ReplicationEntry
	 * 
	 * @param views
	 *            Collection<String> of view names or aliases
	 * @since org.openntf.domino 1.0.0
	 */
	public void setViews(final Collection<String> views);

}
