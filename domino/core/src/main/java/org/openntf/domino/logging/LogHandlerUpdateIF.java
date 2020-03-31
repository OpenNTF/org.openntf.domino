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
package org.openntf.domino.logging;

import java.util.logging.Formatter;

/**
 * Implemented by handlers to enable them to be dynamically attached to a new log configuration.
 */
public interface LogHandlerUpdateIF {
	/**
	 * Indicates whether the handler is able to update itself with the new handlerConfig
	 *
	 * @param newHandlerConfig
	 *            new Config
	 * @param oldHandlerConfig
	 *            old Config
	 * @return true if the handler is able
	 */
	public boolean mayUpdateYourself(LogHandlerConfigIF newHandlerConfig, LogHandlerConfigIF oldHandlerConfig);

	/**
	 * Tells the handler to update itself; mustn't throw any Exceptions
	 *
	 * @param newhandlerConfig
	 *            new Config
	 * @param oldHandlerConfig
	 *            old Config
	 * @param useDefaultFormatter
	 *            if the handler should use its default formatter
	 * @param newFormatter
	 *            the formatter the handler should set (only != null, if really changed)
	 */
	public void doUpdateYourself(LogHandlerConfigIF newhandlerConfig, LogHandlerConfigIF oldHandlerConfig, boolean useDefaultFormatter,
			Formatter newFormatter);
}
