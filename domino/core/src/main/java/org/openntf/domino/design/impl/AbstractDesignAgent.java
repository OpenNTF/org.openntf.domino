package org.openntf.domino.design.impl;

import java.util.ArrayList;
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
import com.ibm.icu.util.Calendar;

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
			XMLNode scheduleNode = getDxl().selectSingleNode("/agent/trigger/schedule");
			if (null == scheduleNode) {
				return false;
			} else {
				return true;
			}
		default:
			return getFlags().contains("S");
		}
	}

	@Override
	public ScheduleType getScheduleType() {
		XMLNode scheduleNode = getDxl().selectSingleNode("/agent/trigger/schedule");
		if (null == scheduleNode) {
			return ScheduleType.NONE;
		} else {
			switch (scheduleNode.getAttribute("type")) {
			case "byminutes":
				return ScheduleType.MORE_THAN_DAILY;
			case "daily":
				return ScheduleType.DAILY;
			case "weekly":
				return ScheduleType.WEEKLY;
			case "monthly":
				return ScheduleType.MONTHLY;
			case "never":
				return ScheduleType.NEVER;
			default:
				DominoUtils
						.handleException(new OpenNTFNotesException("Invalid Agent Schedule Type - " + scheduleNode.getAttribute("type")));
				return ScheduleType.NONE;
			}
		}
	}

	@Override
	public String getRunLocation() {
		XMLNode scheduleNode = getDxl().selectSingleNode("/agent/trigger/schedule");
		if (null == scheduleNode) {
			return null;
		} else {
			if ("choose".equals(scheduleNode.getAttribute("runlocation"))) {
				return "choose";
			} else {
				return scheduleNode.getAttribute("runserver");
			}
		}
	}

	@Override
	public String getStartDate() {
		XMLNode dateNode = getDxl().selectSingleNode("/agent/trigger/schedule/startdate/datetime");
		if (null == dateNode) {
			return "";
		} else {
			return dateNode.getText();
		}
	}

	@Override
	public String getEndDate() {
		XMLNode dateNode = getDxl().selectSingleNode("/agent/trigger/schedule/enddate/datetime");
		if (null == dateNode) {
			return "";
		} else {
			return dateNode.getText();
		}
	}

	@Override
	public String getStartTime() {
		XMLNode timeNode = getDxl().selectSingleNode("/agent/trigger/schedule/starttime/datetime");
		if (null == timeNode) {
			if (ScheduleType.MORE_THAN_DAILY.equals(getScheduleType())) {
				return "T000000,00";
			} else {
				return "";
			}
		} else {
			return timeNode.getText();
		}
	}

	@Override
	public String getEndTime() {
		XMLNode timeNode = getDxl().selectSingleNode("/agent/trigger/schedule/endtime/datetime");
		if (null == timeNode) {
			if (ScheduleType.MORE_THAN_DAILY.equals(getScheduleType())) {
				return "T000000,00";
			} else {
				return "";
			}
		} else {
			return timeNode.getText();
		}
	}

	@Override
	public boolean getRunOnWeekends() {
		XMLNode scheduleNode = getDxl().selectSingleNode("/agent/trigger/schedule");
		if (null != scheduleNode) {
			String onWeekends = scheduleNode.getAttribute("onweekends");
			if (StringUtil.equals("false", onWeekends)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int getDayOfWeek() {
		XMLNode scheduleNode = getDxl().selectSingleNode("/agent/trigger/schedule");
		if (null != scheduleNode) {
			String dateInMonth = scheduleNode.getAttribute("dayofweek");
			if (null != dateInMonth) {
				switch (dateInMonth) {
				case "sunday":
					return 1;
				case "monday":
					return 2;
				case "tuesday":
					return 3;
				case "wednesday":
					return 4;
				case "thursday":
					return 5;
				case "friday":
					return 6;
				case "saturday":
					return 7;
				}
			}
		}
		return 0;
	}

	@Override
	public int getDayOfMonth() {
		XMLNode scheduleNode = getDxl().selectSingleNode("/agent/trigger/schedule");
		if (null != scheduleNode) {
			String dateInMonth = scheduleNode.getAttribute("dateinmonth");
			if (null != dateInMonth) {
				return Integer.parseInt(dateInMonth);
			}
		}
		return 0;
	}

	@Override
	public int getIntervalHours() {
		XMLNode scheduleNode = getDxl().selectSingleNode("/agent/trigger/schedule");
		if (null != scheduleNode) {
			String hours = scheduleNode.getAttribute("hours");
			if (null != hours) {
				return Integer.parseInt(hours);
			}
		}
		return 0;
	}

	@Override
	public int getIntervalMinutes() {
		XMLNode scheduleNode = getDxl().selectSingleNode("/agent/trigger/schedule");
		if (null != scheduleNode) {
			String minutes = scheduleNode.getAttribute("minutes");
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
		XMLNode runLogNode = getDxl().selectSingleNode("/agent/rundata/runlog");
		if (null != runLogNode) {
			return runLogNode.getText();
		} else {
			return "";
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.design.DesignAgent#getRunLogAsList()
	 */
	@Override
	public List<String> getRunLogAsList() {
		String log = getRunLog();
		if ("".equals(log)) {
			return new ArrayList<String>();
		} else {
			return CollectionUtils.getListStrings(log.split(Strings.REGEX_NEWLINE));
		}
	}

	@Override
	public Long getLastRunDuration() {
		List<String> lastRunLog = getRunLogAsList();
		if (!lastRunLog.isEmpty()) {
			String strStart = "";
			String strEnd = "";
			try {
				for (String str : lastRunLog) {
					if (Strings.startsWithIgnoreCase(str, "Started running agent")) {
						strStart = str.substring(str.length() - 19, str.length());
						if (strStart.contains("AM") || strStart.contains("PM")) {
							strStart = str.substring(str.length() - 22, str.length() - 3);
						}
					} else if (Strings.startsWithIgnoreCase(str, "Done running agent")) {
						strEnd = str.substring(str.length() - 19, str.length());
						if (strEnd.contains("AM") || strEnd.contains("PM")) {
							strEnd = str.substring(str.length() - 22, str.length() - 3);
						}
					}
				}
				if (strStart == "" || strEnd == "") {
					return Long.MIN_VALUE;
				}
				// Adjust for pre-2000 dates, which don't have four-digit year
				if ("/9".equals(strStart.substring(7, 9))) {
					strStart = strStart.substring(2);
				}
				if ("/9".equals(strEnd.substring(7, 9))) {
					strEnd = strEnd.substring(2);
				}
				DateTime start = Factory.getSession(SessionType.CURRENT).createDateTime(strStart);
				DateTime end = Factory.getSession(SessionType.CURRENT).createDateTime(strEnd);
				Calendar thisCal = start.toJavaCal();
				Calendar thatCal = end.toJavaCal();
				return (thatCal.getTimeInMillis() - thisCal.getTimeInMillis()) / 1000;
			} catch (Exception e) {
				DominoUtils.handleException(e, "Error on " + getName() + " - start: " + strStart + ", end: " + strEnd);
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
				if (str.contains("ERROR: Agent execution time limit exceeded")) {
					retVal = true;
				}
			}
		}
		return retVal;
	}

}
