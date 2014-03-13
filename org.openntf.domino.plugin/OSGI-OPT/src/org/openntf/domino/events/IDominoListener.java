/**
 * 
 */
package org.openntf.domino.events;

import java.util.List;

/**
 * @author nfreeman
 * 
 */
public interface IDominoListener {
	public boolean eventHappened(IDominoEvent event);

	public List<EnumEvent> getEventTypes();
}
