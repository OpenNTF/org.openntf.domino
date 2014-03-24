/**
 * 
 */
package org.openntf.domino.events;

import java.util.logging.Logger;

/**
 * @author nfreeman
 * 
 *         Abstract implementation of an IDominoEvent used by Listeners within the API
 * 
 */
public abstract class AbstractDominoEvent implements IDominoEvent {
	private static final Logger log_ = Logger.getLogger(AbstractDominoEvent.class.getName());
	private static final long serialVersionUID = 1L;

	private final EnumEvent event_;

	private final org.openntf.domino.Base source_;
	private final org.openntf.domino.Base target_;
	private final Object payload_;

	/**
	 * Constructor passing all the variables used by the AbstractDominoEvent
	 * 
	 * @param event
	 *            EnumEvent this AbstractDominoEvent triggers
	 * @param source
	 *            Base Domino object that is the source of the event
	 * @param target
	 *            Base Domino object that is the target of the event
	 * @param payload
	 *            Object being passed as the payload for the event
	 * 
	 * @since openntf.domino 3.0.0
	 */
	public AbstractDominoEvent(final EnumEvent event, final org.openntf.domino.Base source, final org.openntf.domino.Base target,
			final Object payload) {
		event_ = event;
		source_ = source;
		target_ = target;
		payload_ = payload;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.events.IDominoEvent#getEvent()
	 */
	public EnumEvent getEvent() {
		return event_;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.events.IDominoEvent#getSource()
	 */
	public org.openntf.domino.Base getSource() {
		return source_;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.events.IDominoEvent#getTarget()
	 */
	public org.openntf.domino.Base getTarget() {
		return target_;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.events.IDominoEvent#getPayload()
	 */
	public Object getPayload() {
		return payload_;
	}

}
