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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import org.openntf.domino.thread.AbstractDominoExecutor.DominoFutureTask;
import org.openntf.domino.xots.Xots;
import org.osgi.framework.Bundle;

import com.ibm.commons.util.StringUtil;

/**
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
@SuppressWarnings("unused")
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
		addCommand("junit <package> <testclass>", "Run the JUnit runnable", sb);
		addCommand("oda stop", "Stop the ODA-API", sb);
		addCommand("oda start", "Start the ODA-API", sb);
		addCommand("oda restart", "ReStart the ODA-API", sb);
		return sb.toString();
	}

	private void printThrowable(final CommandInterpreter ci, final Throwable t) {
		StringWriter errors = new StringWriter();
		t.printStackTrace(new PrintWriter(errors));
		ci.println(errors.toString());
		log_.log(Level.SEVERE, t.getMessage(), t);

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
				return false;
			}
		}
		return true;
	}

	public void _xots(final CommandInterpreter ci) {
		try {
			String cmd = ci.nextArgument();
			if (StringUtil.isEmpty(cmd)) {
				// TODO what does XOTS?
				xotsTasks(ci);
			} else if (cmp(cmd, "tasks", 1)) { // tasks
				xotsTasks(ci);
			} else if (cmp(cmd, "schedule", 1)) {
				xotsSchedule(ci);
			} else if (cmp(cmd, "run", 1)) {
				xotsRun(ci);
			} else {
				ci.println("Unknown command: " + cmd);
			}
		} catch (Exception e) {
			printThrowable(ci, e);

		}
	}

	public void _junit(final CommandInterpreter ci) throws ClassNotFoundException {
		try {
			String bundleName = ci.nextArgument();
			String className = ci.nextArgument();

			final Bundle bundle = Platform.getBundle(bundleName);
			Class<?> testclass = bundle.loadClass(className);

			JUnitCore runner = new org.junit.runner.JUnitCore();
			RunListener listener = new TextListener(System.out);
			runner.addListener(listener);

			Result result = runner.run(testclass);
			if (result.wasSuccessful()) {
				ci.println("SUCCESS");
			} else {
				ci.print("FAILURE - " + result.getFailureCount() + " failed");
			}
		} catch (Exception e) {
			printThrowable(ci, e);
		}

	}

	public void _oda(final CommandInterpreter ci) {
		try {
			String cmd = ci.nextArgument();
			if (StringUtil.isEmpty(cmd)) {
				// TODO what does ODA?
			} else if (cmp(cmd, "stop", 3)) {
				ODAPlatform.stop();
			} else if (cmp(cmd, "start", 3)) {
				ODAPlatform.start();
			} else if (cmp(cmd, "restart", 1)) {
				ODAPlatform.stop();
				ODAPlatform.start();
			}
		} catch (Exception e) {
			printThrowable(ci, e);
		}
	}

	private void xotsSchedule(final CommandInterpreter ci) {
		//		String moduleName = ci.nextArgument();
		//		String className = ci.nextArgument();
		//		String cron = ci.nextArgument();
		//// 2014-11-24 RPr:Xots.registerTasklet(moduleName, className, cron);
	}

	private void xotsTasks(final CommandInterpreter ci) {
		ci.println("ID\tSTATE\tNEXT EXEC TIME");

		List<DominoFutureTask<?>> tasks = Xots.getTasks(null);
		for (DominoFutureTask<?> task : tasks) {
			ci.println(task.getId() + "\t" + // ID
					task.getState() + "\t" + // State
					convertTimeUnit(task.getNextExecutionTimeInMillis()));

		}
	}

	private String convertTimeUnit(final long millis) {
		if (millis < 0) {
			return "NOW!";
		}
		if (millis < 1000 * 240) {
			return "in " + (millis / 1000) + " seconds";
		}
		return "at " + new Date(millis);
	}

	private void xotsRun(final CommandInterpreter ci) {
		String moduleName = ci.nextArgument();
		String className = ci.nextArgument();

		List<String> args = new ArrayList<String>();

		String arg;
		while ((arg = ci.nextArgument()) != null) {
			args.add(arg);
		}
		Xots.getService().runTasklet(moduleName, className, args.toArray());

	}

}
