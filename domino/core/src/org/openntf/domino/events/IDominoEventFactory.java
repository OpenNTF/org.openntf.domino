/**
 * 
 */
package org.openntf.domino.events;

import java.io.Serializable;

/**
 * @author nfreeman
 * 
 */
public interface IDominoEventFactory extends Serializable {
	/**
	 * Returns the IDominoEvent wrapped by this EventFactory
	 * 
	 * @param event
	 *            IDominoEvent containing EnumEvent, source, target and payload
	 * @return the IDominoEvent passed into the method
	 * 
	 * @since openntf.domino 3.0.0
	 */
	public IDominoEvent wrap(IDominoEvent event);

	/**
	 * Creates a new IDominoEvent and returns it
	 * 
	 * @param event
	 *            EnumEvent this AbstractDominoEvent triggers
	 * @param source
	 *            Base Domino object that is the source of the event
	 * @param target
	 *            Base Domino object that is the target of the event
	 * @param payload
	 *            Object being passed as the payload for the event
	 * @return the IDominoEvent passed into the method
	 * 
	 * @since openntf.domino 3.0.0
	 */
	public IDominoEvent generate(EnumEvent event, org.openntf.domino.Base<?> source, org.openntf.domino.Base<?> target, Object payload);

	/**
	 * Initializes the IDominoFactory and allows code to be run while it loads
	 * 
	 * @since openntf.domino 3.0.0
	 */
	public void initialize();

	/**
	 * Terminate the IDominoFactory and allows code to be run while it unloads
	 * 
	 * @since openntf.domino 3.0.0
	 */
	public void terminate();
}
