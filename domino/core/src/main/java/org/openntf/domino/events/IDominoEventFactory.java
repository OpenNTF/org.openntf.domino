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
