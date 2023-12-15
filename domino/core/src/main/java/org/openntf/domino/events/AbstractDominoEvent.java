/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 *
 */
package org.openntf.domino.events;

import java.util.logging.Logger;

/**
 * Abstract implementation of an IDominoEvent used by Listeners within the API
 * 
 * @author nfreeman
 *
 *
 */
public abstract class AbstractDominoEvent implements IDominoEvent {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(AbstractDominoEvent.class.getName());

	private final EnumEvent event_;

	private final org.openntf.domino.Base<?> source_;
	private final org.openntf.domino.Base<?> target_;
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
	public AbstractDominoEvent(final EnumEvent event, final org.openntf.domino.Base<?> source, final org.openntf.domino.Base<?> target,
			final Object payload) {
		event_ = event;
		source_ = source;
		target_ = target;
		payload_ = payload;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.events.IDominoEvent#getEvent()
	 */
	@Override
	public EnumEvent getEvent() {
		return event_;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.events.IDominoEvent#getSource()
	 */
	@Override
	public org.openntf.domino.Base<?> getSource() {
		return source_;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.events.IDominoEvent#getTarget()
	 */
	@Override
	public org.openntf.domino.Base<?> getTarget() {
		return target_;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.events.IDominoEvent#getPayload()
	 */
	@Override
	public Object getPayload() {
		return payload_;
	}

}
