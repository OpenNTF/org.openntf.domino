/**
 * 
 */
package org.openntf.domino.events;

import java.util.logging.Logger;

/**
 * @author nfreeman
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
	 * 
	 */
	public AbstractDominoEvent(final EnumEvent event, final org.openntf.domino.Base source, final org.openntf.domino.Base target,
			final Object payload) {
		event_ = event;
		source_ = source;
		target_ = target;
		payload_ = payload;
	}

	public EnumEvent getEvent() {
		return event_;
	}

	public org.openntf.domino.Base getSource() {
		return source_;
	}

	public org.openntf.domino.Base getTarget() {
		return target_;
	}

	public Object getPayload() {
		return payload_;
	}

}
