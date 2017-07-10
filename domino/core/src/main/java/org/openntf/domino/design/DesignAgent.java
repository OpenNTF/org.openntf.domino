/*
 * Copyright 2013
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.openntf.domino.design;

/**
 * @author Roland Praml
 * @author Paul Withers
 *
 */
public interface DesignAgent extends DesignBaseNamed {

	/**
	 * Options for agent scheduling, NONE = not a scheduled agent
	 *
	 * @author Paul Withers
	 * @since ODA 4.1.0
	 *
	 */
	public enum ScheduleType {
		NONE, MORE_THAN_DAILY, DAILY, WEEKLY, MONTHLY, NEVER;
	}

	/**
	 * Check whether agent has schedule info in DXL
	 *
	 * @return whether or not scheduled (regardless of enabled / disabled)
	 * @since ODA 4.1.0
	 */
	public boolean isScheduled();

	/**
	 * Schedule type for the agent, NONE = not scheduled
	 *
	 * @return SchedulType
	 */
	public ScheduleType getScheduleType();

	/**
	 * Gets the server to run on
	 *
	 * @return Server name or "choose" if set to select at enable time
	 */
	public String getRunLocation();

	/**
	 * Gets the start date for the server, if set
	 *
	 * @return date in CCYYMMDD format
	 */
	public String getStartDate();

	/**
	 * Gets the end date for the server, if set
	 *
	 * @return date in YYYYMMDD format
	 */
	public String getEndDate();

	/**
	 * Gets the start time for the agent, if set. NOTE: Agents set to run more than once a day and all day don't have a start time or end
	 * time in DXL. This API returns what you see in Agent UI, which is midnight to midnight, so return T000000,00 for both.
	 *
	 * @return time in THHmm00,00 format
	 */
	public String getStartTime();

	/**
	 * Gets the end time for the agent, if set. NOTE: Agents set to run more than once a day and all day don't have a start time or end time
	 * in DXL. This API returns what you see in Agent UI, which is midnight to midnight, so return T000000,00 for both.
	 *
	 *
	 * @return time in THHmm00,00 format
	 */
	public String getEndTime();

	/**
	 * Whether or not the agent runs on weekends, only relevant for DAILY or MORE_THAN_DAILY
	 *
	 * @return whether it runs on weekends. WEEKLY or MONTHLY returns true
	 */
	public boolean getRunOnWeekends();

	/**
	 * Day of week, converted using Calendar.SUNDAY etc, so SUNDAY = 1, MONDAY = 2 etc
	 *
	 * @return int day of week
	 */
	public int getDayOfWeek();

	/**
	 * Day of month for a monthly scheduled agent, starting at 1
	 *
	 * @return int day of month
	 */
	public int getDayOfMonth();

	/**
	 * For agents set to run MORE_THAN_DAILY, the number of hours for the "Run agent every" setting
	 *
	 * @return number of hours (0 - 23) agent should run every, or 0 if scheduleType is not MORE_THAN_DAILY
	 */
	public int getIntervalHours();

	/**
	 * For agents set to run MORE_THAN_DAILY, the number of minutes for the "Run agent every" setting. <br>
	 * <br>
	 * Agent scheduling scroller only allows scrolling to 15 minutes minimum. Edit Box in the GUI displays "5" as minimum, but can store
	 * less. However, if you open the GUI again and click OK, the minimum displayed (5) wil be stored.<br>
	 * <br>
	 * NOTE: Agent Manager takes the end time of an agent, adds the number of minutes, and rounds to the next minute based on the number of
	 * seconds the agent started at. E.g. agent starts at 10:20:26 and is set to run every 1 minute; it ends at 10:23:30; adding 1 minute
	 * goes to 10:24:30; rounding to seconds takes it to 10:25:26, which is the next time it will start
	 *
	 * @return number of minutes (5 - 59) agent should run every, or 0 if scheduleType is not MORE_THAN_DAILY
	 */
	public int getIntervalMinutes();

}
