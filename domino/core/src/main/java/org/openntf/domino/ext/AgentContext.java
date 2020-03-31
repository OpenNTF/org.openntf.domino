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
package org.openntf.domino.ext;

import java.util.List;
import java.util.Map;

import org.openntf.domino.Session;

/**
 * OpenNTF Domino extensions to AgentContext class
 *
 * @author withersp
 *
 *
 */
public interface AgentContext {

	/**
	 * Gets the session the agent is running as part of
	 *
	 * @return Session the agent is a child of
	 *
	 * @since org.openntf.domino 1.0.0
	 */
	public Session getParentSession();

	/**
	 * Gets query string parameters as a Map of String Lists if this agent has been started via http protocol.
	 *
	 * @return Map<String, List<String>> query string parameters
	 *
	 * @since org.openntf.domino 4.0.0
	 */
	public Map<String, List<String>> getQueryStringParameters();

}
