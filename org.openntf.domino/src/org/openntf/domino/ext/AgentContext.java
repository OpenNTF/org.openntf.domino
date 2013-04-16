/**
 * 
 */
package org.openntf.domino.ext;

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

}
