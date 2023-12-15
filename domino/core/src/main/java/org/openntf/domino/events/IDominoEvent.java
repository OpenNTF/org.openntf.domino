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

/**
 * The OpenNTF IDominoEvent interface, the interface passed to the eventHappened method of a Listener
 * 
 * @author nfreeman
 *
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
