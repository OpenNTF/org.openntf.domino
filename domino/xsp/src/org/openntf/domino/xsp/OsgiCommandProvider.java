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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.openntf.domino.xots.XotsDaemon;
import org.openntf.domino.xsp.helpers.ModuleLoader;
import org.openntf.domino.xsp.xots.XotsDominoFutureTask;

import com.ibm.commons.util.StringUtil;
import com.ibm.domino.xsp.module.nsf.NSFComponentModule;

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
		addCommand("oda stop", "Stop the ODA-API", sb);
		addCommand("oda start", "Start the ODA-API", sb);
		addCommand("oda restart", "ReStart the ODA-API", sb);
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
				return false;
			}
		}
		return true;
	}

	public void _xots(final CommandInterpreter ci) {
		String cmd = ci.nextArgument();
		if (StringUtil.isEmpty(cmd)) {
			// TODO what does XOTS?
		} else if (cmp(cmd, "tasks", 1)) { // tasks
			ci.println("XOTS task list:");
			ci.println(XotsDaemon.getRunningTasks());
		} else if (cmp(cmd, "schedule", 1)) {
			ci.println("XOTS schedule list:");
		} else if (cmp(cmd, "run", 1)) {
			xotsRun(ci);
		}
	}

	public void _oda(final CommandInterpreter ci) {
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
	}

	private void xotsRun(final CommandInterpreter ci) {
		String moduleName = ci.nextArgument();
		final String className = ci.nextArgument();
		try {
			ci.println("DEBUG: Opening module: " + moduleName);
			NSFComponentModule module = ModuleLoader.loadModule(moduleName, true);

			Runnable runner = new XotsDominoFutureTask<Object>(module, new Runnable() {
				@Override
				public void run() {
					try {
						ci.println("DEBUG: loading class: " + className);
						ClassLoader mcl = Thread.currentThread().getContextClassLoader();
						Class cls = mcl.loadClass(className);
						ci.println("DEBUG: creating instance of: " + cls);
						Object obj = cls.newInstance();
						ci.println("Success: " + obj);
						if (obj instanceof Runnable) {
							//((Runnable) obj).run();
							XotsDaemon.queue((Runnable) obj);
						}
					} catch (Throwable e) {
						StringWriter errors = new StringWriter();
						e.printStackTrace(new PrintWriter(errors));
						ci.println(errors);
					}
				}
			}, null);
			runner.run();
			XotsDaemon.queue(runner);

		} catch (Throwable e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			ci.println(errors);
		}
	}
}
