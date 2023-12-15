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

import org.openntf.domino.Base;

/**
 * Used internally when generating events.
 * 
 * @author nfreeman
 *
 */
public class GenericDominoEventFactory implements IDominoEventFactory {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(GenericDominoEventFactory.class.getName());

	/**
	 * Extension of AbstractDominoObject being used by the GenericDominoEventFactory. No additional code currently
	 *
	 * @author withersp
	 *
	 *
	 * @since openntf.domino 3.0.0
	 */
	public static class GenericDominoEvent extends AbstractDominoEvent {

		/**
		 * Constructor passing all variables used by the GenericDominoEvent
		 *
		 * @param event
		 *            EnumEvent this GenericDominoEvent triggers
		 * @param source
		 *            Base Domino object that is the source of the event
		 * @param target
		 *            Base Domino object that is the target of the event
		 * @param payload
		 *            Object being passed as the payload for the event
		 *
		 * @since openntf.domino 3.0.0
		 */
		public GenericDominoEvent(final EnumEvent event, final Base<?> source, final Base<?> target, final Object payload) {
			super(event, source, target, payload);
		}

	}

	/**
	 * Constructor
	 */
	public GenericDominoEventFactory() {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.events.IDominoEventFactory#wrap(org.openntf.domino.events.IDominoEvent)
	 */
	@Override
	public IDominoEvent wrap(final IDominoEvent event) {
		return event;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.events.IDominoEventFactory#generate(java.lang.Object[])
	 */
	@Override
	public IDominoEvent generate(final EnumEvent event, final org.openntf.domino.Base<?> source, final org.openntf.domino.Base<?> target,
			final Object payload) {
		return new GenericDominoEvent(event, source, target, payload);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.events.IDominoEventFactory#initialize()
	 */
	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.events.IDominoEventFactory#terminate()
	 */
	@Override
	public void terminate() {
		// TODO Auto-generated method stub

	}
}
