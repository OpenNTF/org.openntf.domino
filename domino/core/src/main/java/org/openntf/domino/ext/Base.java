/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
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
package org.openntf.domino.ext;

import java.util.List;

import org.openntf.domino.events.EnumEvent;
import org.openntf.domino.events.IDominoEvent;
import org.openntf.domino.events.IDominoListener;

/**
 * @author withersp
 * 
 *         OpenNTF Domino extensions to Base
 * 
 */
public interface Base {

	/**
	 * Adds an implementation of IDominoListener to the object. Listeners are used to run code when a certain event occurs.
	 * 
	 * @param listener
	 *            a class implementing IDominoListener and running code for an EnumEvent in the eventHappened method
	 * 
	 * @since org.openntf.domino 3.0.0
	 */
	public void addListener(IDominoListener listener);

	/**
	 * Removes an implementation of IDominoListener from the object, ensuring the listener will not trigger in the future
	 * 
	 * @param listener
	 *            a class implementing IDominoListener listening for an EnumEvent
	 * 
	 * @since org.openntf.domino 3.0.0
	 */
	public void removeListener(IDominoListener listener);

	/**
	 * Gets the list of implementations of IDominoListener assigned to the object
	 * 
	 * @return List<IDominoListener> a list of classes implementing IDominoListener
	 * 
	 * @since org.openntf.domino 3.0.0
	 */
	public List<IDominoListener> getListeners();

	/**
	 * Loops through all implementations of IDominoListener assigned to the object and creates a List of all that listen for a specific
	 * event, e.g. Events.AFTER_CREATE_DOCUMENT
	 * 
	 * @param event
	 *            EnumEvent to check for in each class implementing IDominoListener, calling their getEventTypes method
	 * @return List<IDominoListener> a list of class implementing IDominoListener that listen for the specific EnumEvent
	 * 
	 * @since org.openntf.domino 3.0.0
	 */
	public List<IDominoListener> getListeners(EnumEvent event);

	/**
	 * Loops through all listeners calling their eventHappened methods, passing the relevant event
	 * 
	 * @param event
	 *            IDominoEvent containing an EnumEvent, source, target and payload
	 * @return boolean success or failure of running the associated event code
	 * 
	 * @since org.openntf.domino 3.0.0
	 */
	public boolean fireListener(IDominoEvent event);

	/**
	 * @return whether we have any listeners
	 */
	boolean hasListeners();

}
