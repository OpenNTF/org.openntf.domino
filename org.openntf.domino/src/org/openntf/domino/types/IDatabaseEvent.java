/**
 * 
 */
package org.openntf.domino.types;

import org.openntf.domino.Database;
import org.openntf.domino.ext.Database.Events;

/**
 * @author nfreeman
 * 
 */
public interface IDatabaseEvent {
	public Database getDatabase();

	public Object getSource();

	public Events getEvent();

	public Object getTarget();
}
