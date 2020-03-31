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
package org.openntf.domino.xsp.helpers;

import org.openntf.domino.xsp.ODAPlatform;

/**
 * AbstractListener class
 */
public abstract class AbstractListener {
	private final static boolean _debug = ODAPlatform.isDebug();

	/**
	 * Prints a message to the server console
	 * 
	 * @param message
	 *            String to print
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	protected void _debugOut(final String message) {
		if (_debug)
			System.out.println(getClass().getName() + " " + message);
	}
}
