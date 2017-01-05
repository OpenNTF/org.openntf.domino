/**
 * 
 */
package org.openntf.domino.events;

/**
 * @author nfreeman
 * 
 *         The OpenNTF IDominoEvent interface, the interface passed to the eventHappened method of a Listener
 */
public interface IDominoEvent {
	/**
	 * Gets the EnumEvent being triggered
	 * 
	 * @return EnumEvent, options for which are currently listed in {@link org.openntf.domino.ext.Database}
	 * 
	 * @since openntf.domino 3.0.0
	 */
	public EnumEvent getEvent();

	/**
	 * Gets the source object triggering the event. For e.g. AFTER_UPDATE_DOCUMENT the source is the Document being updated.
	 * 
	 * @return Base Domino object
	 * 
	 * @since openntf.domino 3.0.0
	 */
	public org.openntf.domino.Base<?> getSource();

	/**
	 * Gets the target object for the event. For e.g. AFTER_UPDATE_DOCUMENT the target is the database where the Document is being updated.
	 * 
	 * @return Based Domino object
	 * 
	 * @since openntf.domino 3.0.0
	 */
	public org.openntf.domino.Base<?> getTarget();

	/**
	 * Gets the payload being passed by the event.
	 * 
	 * @return Object that is the payload of the event
	 * 
	 * @since openntf.domino 3.0.0
	 */
	public Object getPayload();
}
