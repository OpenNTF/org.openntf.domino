/**
 * 
 */
package org.openntf.domino.ext;

import java.util.List;
import java.util.Map;

import org.openntf.domino.Session;

/**
 * @author withersp
 * 
 */
public interface AgentContext {

	/**
	 * @return The session the agent is a child of
	 */
	public Session getParentSession();

	/**
	 * @return The query string parameters as a Map of String Lists
	 */
	public Map<String, List<String>> getQueryStringParameters();

}
