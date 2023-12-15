/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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

	/**
	 * {@link org.openntf.domino.Agent#getName()} is an absolute mess. If an agent has a trigger of "Agent list selection", the core method
	 * is inconsistent, depending on whether or not there's an alias. This method, instead, goes to the Note and gets its $TITLE field,
	 * which is correct
	 *
	 * @return actual name from the $TITLE field
	 */
	public String getActualName();

	/**
	 * Extracts the alias from the name - {@link org.openntf.domino.Agent#getName()} contains alias as well
	 *
	 * @return alias
	 */
	public String getAlias();
}
