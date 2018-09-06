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
