/**
 *
 */
package org.openntf.domino.ext;

/**
 * OpenNTF Domino extensions to Agent class
 *
 * @author withersp
 *
 *
 */
public interface Agent {
	public String getMetaversalID();

	/**
	 * Checks for existence of $FlagsExt containing "F"
	 *
	 * @return whether or not the agent is enabled for profiling
	 */
	public boolean isProfiled();
}
