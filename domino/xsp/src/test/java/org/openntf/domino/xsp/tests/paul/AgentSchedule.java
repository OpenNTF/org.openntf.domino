package org.openntf.domino.xsp.tests.paul;

import org.openntf.domino.Agent;
import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.design.DesignAgent;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public class AgentSchedule implements Runnable {

	public AgentSchedule() {

	}

	public static void main(final String[] args) {
		TestRunnerUtil.runAsDominoThread(new AgentSchedule(), TestRunnerUtil.NATIVE_SESSION);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {
			Session sess = Factory.getSession(SessionType.NATIVE);
			Database db = sess.getDatabase("PrivateTest.nsf");
			StringBuilder sb = new StringBuilder();
			for (Agent agent : db.getAgents()) {
				sb.append("============");
				addNewLine(sb);
				DesignAgent designAgent = db.getDesign().getAgent(agent.getActualName());
				if (!designAgent.isScheduled()) {
					sb.append("Agent " + agent.getActualName() + " is not scheduled");
				} else {
					sb.append("Outputting schedule for agent " + agent.getActualName());
					addNewLine(sb);
					sb.append("Set to run on: " + designAgent.getRunLocation());
					addNewLine(sb);
					sb.append("Schedule type: " + designAgent.getScheduleType().name());
					addNewLine(sb);
					switch (designAgent.getScheduleType()) {
					case MORE_THAN_DAILY:
						sb.append("Set to run every " + designAgent.getIntervalHours() + " hours, every " + designAgent.getIntervalMinutes()
								+ " minutes");
						addNewLine(sb);
						sb.append("Starting at " + designAgent.getStartTime() + ", ending at " + designAgent.getEndTime());
						addNewLine(sb);
						if (designAgent.getRunOnWeekends()) {
							sb.append("Agent set to run on weekends");
						} else {
							sb.append("Agent not set to run on weekends");
						}
						break;
					case DAILY:
						sb.append("Run daily at " + designAgent.getStartTime());
						addNewLine(sb);
						if (designAgent.getRunOnWeekends()) {
							sb.append("Agent set to run on weekends");
						} else {
							sb.append("Agent not set to run on weekends");
						}
						break;
					case WEEKLY:
						sb.append("Run every " + designAgent.getDayOfWeek() + " at " + designAgent.getStartTime());
						break;
					case MONTHLY:
						sb.append("Run every month on day " + designAgent.getDayOfMonth() + " at " + designAgent.getStartTime());
						break;
					default:

					}
					addNewLine(sb);
					sb.append("Agent scheduled to run from " + designAgent.getStartDate() + " to " + designAgent.getEndDate());
				}
				addNewLine(sb);
				sb.append("============");
				addNewLine(sb);
			}
			System.out.println(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private StringBuilder addNewLine(final StringBuilder sb) {
		sb.append("\r\n");
		return sb;
	}

}
