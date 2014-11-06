/*
 * © Copyright Foconis AG, 2013
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
package org.openntf.domino.xsp;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.openntf.domino.xots.XotsDaemon;
import org.openntf.domino.xsp.adapter.OpenntfHttpService;

import com.ibm.commons.util.StringUtil;

/**
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public class OsgiCommandProvider implements CommandProvider {
	private static final String tab = "\t";
	private static final String newline = "\r\n";
	private static final Logger log_ = Logger.getLogger(OsgiCommandProvider.class.getName());

	private Map<String, Logger> configuredLoggers = new HashMap<String, Logger>();

	private void addHeader(final String title, final StringBuffer sb) {
		sb.append("---");
		sb.append(title);
		sb.append("---");
		sb.append(newline);
	}

	private void addCommand(final String cmd, final String desc, final StringBuffer sb) {
		sb.append(tab);
		sb.append(cmd);
		sb.append(" - ");
		sb.append(desc);
		sb.append(newline);
	}

	private void addCommand(final String cmd, final String params, final String desc, final StringBuffer sb) {
		sb.append(tab);
		sb.append(cmd);
		sb.append(" ");
		sb.append(params);
		sb.append(" - ");
		sb.append(desc);
		sb.append(newline);
	}

	/**
	 * Returns all available commands
	 */
	@Override
	public String getHelp() {
		StringBuffer sb = new StringBuffer(1024);
		sb.append(newline); // the previous call forgot newline
		addHeader("XOTS commands", sb);
		addCommand("xots tasks", "(filter)", "Show currently running tasks", sb);
		addCommand("xots schedule", "(filter)", "Show all scheduled tasks", sb);
		return sb.toString();
	}

	/**
	 * 
	 * @param inp
	 * @param cmd
	 * @param len
	 */
	protected boolean cmp(final String inp, final String cmd, final int len) {
		if (inp.length() < len || cmd.length() < len)
			return false;
		for (int i = 0; i < inp.length(); i++) {
			if (i < cmd.length()) {
				if (inp.charAt(i) != cmd.charAt(i))
					return false;
			} else {
				return (i + 1 >= len);
			}
		}
		return false;
	}

	public void _xots(final CommandInterpreter ci) {
		String cmd = ci.nextArgument();
		if (StringUtil.isEmpty(cmd)) {
			// TODO what does XOTS?
		} else if (cmp(cmd, "tasks", 1)) { // tasks
			ci.println("XOTS task list:");
			ci.println(XotsDaemon.getInstance().getRunningTasks());
		} else if (cmp(cmd, "schedule", 1)) {
			ci.println("XOTS schedule list:");
		} else if (cmp(cmd, "run", 1)) {
			xotsRun(ci);
		}
	}

	private void xotsRun(final CommandInterpreter ci) {
		String moduleName = ci.nextArgument();

		try {
			OpenntfHttpService.sGetNsfService().loadModule(moduleName);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
