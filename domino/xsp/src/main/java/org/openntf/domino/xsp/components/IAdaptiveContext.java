/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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
package org.openntf.domino.xsp.components;

/**
 * @author Nathan T. Freeman
 * 
 * Interface for managing functionality adapting to client type, not implemented yet
 */
/**
 * @author withersp
 * 
 */
public interface IAdaptiveContext {
	/**
	 * Enum to allow capturing client type
	 * 
	 * @since org.openntf.domino 4.5.0
	 */
	public static enum ClientType {
		BROWSER, MOBILE, TABLET, XPINC, API
	}

	/**
	 * Retrieves the relevant controller to use
	 * 
	 * @return IAdaptiveController instance
	 * @since org.openntf.domino 4.5.0
	 */
	public IAdaptiveController getController();

	/**
	 * Sets the controller to use for the relevant client type
	 * 
	 * @param controller
	 *            IAdaptiveController instance
	 * @since org.openntf.domino 4.5.0
	 */
	public void setController(IAdaptiveController controller);

	/**
	 * Gets the relevant client type in use
	 * 
	 * @return ClientType enum instance
	 * @since org.openntf.domino 4.5.0
	 */
	public ClientType getClientType();

	/**
	 * Setys the client type to use
	 * 
	 * @param type
	 *            ClientType enum instance
	 * @since org.openntf.domino 4.5.0
	 */
	public void setClientType(ClientType type);

}
