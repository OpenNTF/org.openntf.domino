/**
 * 
 */
package org.openntf.domino.ext;

import java.util.Collection;

import org.openntf.domino.Replication;

/**
 * @author withersp
 * 
 */
public interface ReplicationEntry {

	/**
	 * @return get parent Replication
	 */
	public Replication getParent();

	/**
	 * @param views
	 *            collection of views
	 */
	public void setViews(Collection<String> views);

}
