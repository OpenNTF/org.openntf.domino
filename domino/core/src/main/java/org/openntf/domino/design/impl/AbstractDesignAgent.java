/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
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
package org.openntf.domino.design.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.openntf.domino.DateTime;
import org.openntf.domino.Document;
import org.openntf.domino.exceptions.OpenNTFNotesException;
import org.openntf.domino.utils.CollectionUtils;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.utils.Strings;
import org.openntf.domino.utils.xml.XMLNode;

import com.ibm.commons.util.StringUtil;

public abstract class AbstractDesignAgent extends AbstractDesignBaseNamed implements org.openntf.domino.design.DesignAgent {
	private static final long serialVersionUID = 1L;

	/**
	 * @param document
	 */
	protected AbstractDesignAgent(final Document document) {
		super(document);
	}

	@Override
	public boolean isScheduled() {
		switch (getDxlFormat(false)) {
		case DXL:
			XMLNode scheduleNode = getDxl().selectSingleNode("/agent/trigger/schedule"); //$NON-NLS-1$
			if (null == scheduleNode) {
				return false;
			} else {
				return true;
			}
		default:
			return getFlags().contains("S"); //$NON-NLS-1$
		}
	}

	@Override
	public ScheduleType getScheduleType() {
		XMLNode scheduleNode = getDxl().selectSingleNode("/agent/trigger/schedule"); //$NON-NLS-1$
		if (null == scheduleNode) {
			return ScheduleType.NONE;
		} else {
			switch (scheduleNode.getAttribute("type")) { //$NON-NLS-1$
			case "byminutes": //$NON-NLS-1$
				return ScheduleType.MORE_THAN_DAILY;
			case "daily": //$NON-NLS-1$
				return ScheduleType.DAILY;
			case "weekly": //$NON-NLS-1$
				return ScheduleType.WEEKLY;
			case "monthly": //$NON-NLS-1$
				return ScheduleType.MONTHLY;
			case "never": //$NON-NLS-1$
				return ScheduleType.NEVER;
			default:
				DominoUtils
						.handleException(new OpenNTFNotesException("Invalid Agent Schedule Type - " + scheduleNode.getAttribute("type"))); //$NON-NLS-1$ //$NON-NLS-2$
				return ScheduleType.NONE;
			}
		}
	}

	@Override
	public String getRunLocation() {
		XMLNode scheduleNode = getDxl().selectSingleNode("/agent/trigger/schedule"); //$NON-NLS-1$
		if (null == scheduleNode) {
			return null;
		} else {
			if ("choose".equals(scheduleNode.getAttribute("runlocation"))) { //$NON-NLS-1$ //$NON-NLS-2$
				return "choose"; //$NON-NLS-1$
			} else {
				return scheduleNode.getAttribute("runserver"); //$NON-NLS-1$
			}
		}
	}

	@Override
	public String getStartDate() {
		XMLNode dateNode = getDxl().selectSingleNode("/agent/trigger/schedule/startdate/datetime"); //$NON-NLS-1$
		if (null == dateNode) {
			return ""; //$NON-NLS-1$
		} else {
			return dateNode.getText();
		}
	}

	@Override
	public String getEndDate() {
		XMLNode dateNode = getDxl().selectSingleNode("/agent/trigger/schedule/enddate/datetime"); //$NON-NLS-1$
		if (null == dateNode) {
			return ""; //$NON-NLS-1$
		} else {
			return dateNode.getText();
		}
	}

	@Override
	public String getStartTime() {
		XMLNode timeNode = getDxl().selectSingleNode("/agent/trigger/schedule/starttime/datetime"); //$NON-NLS-1$
		if (null == timeNode) {
			if (ScheduleType.MORE_THAN_DAILY.equals(getScheduleType())) {
				return "T000000,00"; //$NON-NLS-1$
			} else {
				return ""; //$NON-NLS-1$
			}
		} else {
			return timeNode.getText();
		}
	}

	@Override
	public String getEndTime() {
		XMLNode timeNode = getDxl().selectSingleNode("/agent/trigger/schedule/endtime/datetime"); //$NON-NLS-1$
		if (null == timeNode) {
			if (ScheduleType.MORE_THAN_DAILY.equals(getScheduleType())) {
				return "T000000,00"; //$NON-NLS-1$
			} else {
				return ""; //$NON-NLS-1$
			}
		} else {
			return timeNode.getText();
		}
	}

	@Override
	public boolean getRunOnWeekends() {
		XMLNode scheduleNode = getDxl().selectSingleNode("/agent/trigger/schedule"); //$NON-NLS-1$
		if (null != scheduleNode) {
			String onWeekends = scheduleNode.getAttribute("onweekends"); //$NON-NLS-1$
			if (StringUtil.equals("false", onWeekends)) { //$NON-NLS-1$
				return false;
			}
		}
		return true;
	}

	@Override
	public int getDayOfWeek() {
		XMLNode scheduleNode = getDxl().selectSingleNode("/agent/trigger/schedule"); //$NON-NLS-1$
		if (null != scheduleNode) {
			String dateInMonth = scheduleNode.getAttribute("dayofweek"); //$NON-NLS-1$
			if (null != dateInMonth) {
				switch (dateInMonth) {
				case "sunday": //$NON-NLS-1$
					return 1;
				case "monday": //$NON-NLS-1$
					return 2;
				case "tuesday": //$NON-NLS-1$
					return 3;
				case "wednesday": //$NON-NLS-1$
					return 4;
				case "thursday": //$NON-NLS-1$
					return 5;
				case "friday": //$NON-NLS-1$
					return 6;
				case "saturday": //$NON-NLS-1$
					return 7;
				}
			}
		}
		return 0;
	}

	@Override
	public int getDayOfMonth() {
		XMLNode scheduleNode = getDxl().selectSingleNode("/agent/trigger/schedule"); //$NON-NLS-1$
		if (null != scheduleNode) {
			String dateInMonth = scheduleNode.getAttribute("dateinmonth"); //$NON-NLS-1$
			if (null != dateInMonth) {
				return Integer.parseInt(dateInMonth);
			}
		}
		return 0;
	}

	@Override
	public int getIntervalHours() {
		XMLNode scheduleNode = getDxl().selectSingleNode("/agent/trigger/schedule"); //$NON-NLS-1$
		if (null != scheduleNode) {
			String hours = scheduleNode.getAttribute("hours"); //$NON-NLS-1$
			if (null != hours) {
				return Integer.parseInt(hours);
			}
		}
		return 0;
	}

	@Override
	public int getIntervalMinutes() {
		XMLNode scheduleNode = getDxl().selectSingleNode("/agent/trigger/schedule"); //$NON-NLS-1$
		if (null != scheduleNode) {
			String minutes = scheduleNode.getAttribute("minutes"); //$NON-NLS-1$
			if (null != minutes) {
				return Integer.parseInt(minutes);
			}
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.DesignAgent#getRunLog()
	 */
	@Override
	public String getRunLog() {
		XMLNode runLogNode = getDxl().selectSingleNode("/agent/rundata/runlog"); //$NON-NLS-1$
		if (null != runLogNode) {
			return runLogNode.getText();
		} else {
			return ""; //$NON-NLS-1$
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.DesignAgent#getRunLogAsList()
	 */
	@Override
	public List<String> getRunLogAsList() {
		String log = getRunLog();
		if ("".equals(log)) { //$NON-NLS-1$
			return new ArrayList<String>();
		} else {
			return CollectionUtils.getListStrings(log.split(Strings.REGEX_NEWLINE));
		}
	}

	@Override
	public Long getLastRunDuration() {
		List<String> lastRunLog = getRunLogAsList();
		if (!lastRunLog.isEmpty()) {
			String strStart = ""; //$NON-NLS-1$
			String strEnd = ""; //$NON-NLS-1$
			try {
				for (String str : lastRunLog) {
					if (Strings.startsWithIgnoreCase(str, "Started running agent")) { //$NON-NLS-1$
						strStart = str.substring(str.length() - 19, str.length());
						if (strStart.contains("AM") || strStart.contains("PM")) { //$NON-NLS-1$ //$NON-NLS-2$
							strStart = str.substring(str.length() - 22, str.length() - 3);
						}
					} else if (Strings.startsWithIgnoreCase(str, "Done running agent")) { //$NON-NLS-1$
						strEnd = str.substring(str.length() - 19, str.length());
						if (strEnd.contains("AM") || strEnd.contains("PM")) { //$NON-NLS-1$ //$NON-NLS-2$
							strEnd = str.substring(str.length() - 22, str.length() - 3);
						}
					}
				}
				if (strStart == "" || strEnd == "") { //$NON-NLS-1$ //$NON-NLS-2$
					return Long.MIN_VALUE;
				}
				// Adjust for pre-2000 dates, which don't have four-digit year
				if ("/9".equals(strStart.substring(7, 9))) { //$NON-NLS-1$
					strStart = strStart.substring(2);
				}
				if ("/9".equals(strEnd.substring(7, 9))) { //$NON-NLS-1$
					strEnd = strEnd.substring(2);
				}
				DateTime start = Factory.getSession(SessionType.CURRENT).createDateTime(strStart);
				DateTime end = Factory.getSession(SessionType.CURRENT).createDateTime(strEnd);
				Calendar thisCal = start.toJavaCal();
				Calendar thatCal = end.toJavaCal();
				return (thatCal.getTimeInMillis() - thisCal.getTimeInMillis()) / 1000;
			} catch (Exception e) {
				DominoUtils.handleException(e, "Error on " + getName() + " - start: " + strStart + ", end: " + strEnd); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
		}
		return Long.MIN_VALUE;
	}

	@Override
	public boolean isLastRunExceededTimeLimit() {
		boolean retVal = false;
		List<String> lastRunLog = getRunLogAsList();
		if (!lastRunLog.isEmpty()) {
			for (String str : lastRunLog) {
				if (str.contains("ERROR: Agent execution time limit exceeded")) { //$NON-NLS-1$
					retVal = true;
				}
			}
		}
		return retVal;
	}

}
