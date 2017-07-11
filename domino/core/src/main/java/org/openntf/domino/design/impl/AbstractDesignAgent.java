package org.openntf.domino.design.impl;

import org.openntf.domino.Document;
import org.openntf.domino.exceptions.OpenNTFNotesException;
import org.openntf.domino.utils.DominoUtils;
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

}
