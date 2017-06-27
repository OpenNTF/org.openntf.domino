/**
 *
 */
package org.openntf.domino.ext;

import java.util.Date;

import org.openntf.domino.Agent.Target;
import org.openntf.domino.Agent.Trigger;

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
	 * @since 4.1.0
	 */
	public boolean isProfiled();

	/**
	 * Gets the Profile Results document, or null if not profiled or not available
	 *
	 * @return profile document or null
	 * @since 4.1.0
	 */
	public org.openntf.domino.Document getProfileResults();

	/**
	 * Gets the target as a {@link org.openntf.domino.Agent.Target}
	 *
	 * @return Target enum
	 * @since 4.1.0
	 */
	public Target getTargetEx();

	/**
	 * Gets the trigger as a {@link org.openntf.domino.Agent.Trigger}
	 *
	 * @return Trigger enum
	 * @since 4.1.0
	 */
	public Trigger getTriggerEx();

	/**
	 * Gets the last run time as a Date. If it has not run, it will be null
	 *
	 * @return date and time the agent last ran
	 * @since 4.1.0
	 */
	public Date getLastRunDate();
}
