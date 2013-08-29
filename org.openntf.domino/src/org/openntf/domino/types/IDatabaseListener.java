/**
 * 
 */
package org.openntf.domino.types;

import java.util.List;

import org.openntf.domino.ext.Database.Events;

/**
 * @author nfreeman
 * 
 */
public interface IDatabaseListener {
	public boolean eventHappened(IDatabaseEvent event);

	public List<Events> getEventTypes();
}
