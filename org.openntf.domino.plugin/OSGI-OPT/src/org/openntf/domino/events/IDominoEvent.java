/**
 * 
 */
package org.openntf.domino.events;

/**
 * @author nfreeman
 * 
 */
public interface IDominoEvent {
	public EnumEvent getEvent();

	public org.openntf.domino.Base getSource();

	public org.openntf.domino.Base getTarget();

	public Object getPayload();
}
