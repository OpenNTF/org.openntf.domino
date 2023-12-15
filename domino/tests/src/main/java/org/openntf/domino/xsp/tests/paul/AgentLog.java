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
package org.openntf.domino.xsp.tests.paul;

import java.util.List;

import org.openntf.domino.Agent;
import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.design.DatabaseDesign;
import org.openntf.domino.design.DesignAgent;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public class AgentLog implements Runnable {

	public AgentLog() {

	}

	public static void main(final String[] args) {
		TestRunnerUtil.runAsDominoThread(new AgentLog(), TestRunnerUtil.NATIVE_SESSION);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {
			Session sess = Factory.getSession(SessionType.NATIVE);
			Database db = sess.getDatabase("PrivateTest.nsf");
			DatabaseDesign dbDesign = db.getDesign();
			StringBuilder sb = new StringBuilder();
			for (Agent agent : db.getAgents()) {
				if (null != agent.getLastRunDate()) {
					DesignAgent agentDesign = dbDesign.getAgent(agent.getActualName());
					sb.append(agentDesign.getRunLog());
					addNewLine(sb);
					List<String> strings = agentDesign.getRunLogAsList();
					sb.append("Lines = " + strings.size());
					addNewLine(sb);
					sb.append("Duration = " + agentDesign.getLastRunDuration() + " seconds");
					addNewLine(sb);
					sb.append("Exceeded timeout? " + agentDesign.isLastRunExceededTimeLimit());
					addNewLine(sb);
				}
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
