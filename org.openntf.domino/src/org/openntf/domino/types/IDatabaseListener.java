/**
 * 
 */
package org.openntf.domino.types;

import org.openntf.domino.ext.Database.Events;

/**
 * @author nfreeman
 * 
 */
public interface IDatabaseListener {
	public boolean eventHappened(IDatabaseEvent event);

	public Events[] getEventTypes();
}
