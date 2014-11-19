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
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import org.openntf.domino.xots.Tasklet;
import org.openntf.domino.xots.XotsDaemon;
import org.openntf.domino.xsp.helpers.ModuleLoader;
import org.osgi.framework.Bundle;

import com.ibm.commons.util.StringUtil;
import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NotesContext;

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
		addCommand("junit <package> <testclass>", "Run the JUnit runnable", sb);
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
			ci.println(XotsDaemon.getTasks(false));
		} else if (cmp(cmd, "schedule", 1)) {
			ci.println("XOTS schedule list:");
			ci.println(XotsDaemon.getTasks(true));
		} else if (cmp(cmd, "run", 1)) {
			xotsRun(ci);
		}
	}

	public void _junit(final CommandInterpreter ci) throws ClassNotFoundException {
		final Bundle bundle = Platform.getBundle(ci.nextArgument());
		Class<?> testclass = bundle.loadClass(ci.nextArgument());

		JUnitCore runner = new org.junit.runner.JUnitCore();
		RunListener listener = new TextListener(System.out);
		runner.addListener(listener);

		Result result = runner.run(testclass);
		if (result.wasSuccessful()) {
			ci.println("SUCCESS");
		} else {
			ci.print("FAILURE - " + result.getFailureCount() + " failed");
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
		try {
			String moduleName = ci.nextArgument();
			String className = ci.nextArgument();

			Class<?> clazz = null;
			NSFComponentModule module = null;
			NotesContext ctx = null;
			if (moduleName.startsWith("bundle:")) {
				// -- Load the class from bundle
				String bundleName = moduleName.substring(7);
				final Bundle bundle = Platform.getBundle(bundleName);
				if (bundle == null) {
					ci.println("Could not find bundle " + bundleName);
					return;
				}

				try {
					clazz = bundle.loadClass(className);
				} catch (ClassNotFoundException e) {
				}

				if (clazz == null) {
					ci.println("Could not find class " + className + " in bundle " + bundleName);
					return;
				}
			} else {
				// -- Load the class from module
				module = ModuleLoader.loadModule(moduleName, true);
				ctx = new NotesContext(module);
				NotesContext.initThread(ctx);
				try {
					if (module == null) {
						ci.println("Could not find module " + moduleName);
						return;
					}
					try {
						clazz = module.getModuleClassLoader().loadClass(className);
					} catch (ClassNotFoundException e) {
					}

					if (clazz == null) {
						ci.println("Could not find class " + className);
						return;
					}
				} finally {
					NotesContext.termThread();
					ctx = null;
				}
			}

			Tasklet annot = clazz.getAnnotation(Tasklet.class);
			if (annot == null || !annot.isPublic()) {
				ci.println(className + " does not annotate @Tasklet(isPublic=true). Cannot run");
				return;
			}

			List<String> args = new ArrayList<String>();

			String arg;
			while ((arg = ci.nextArgument()) != null) {
				args.add(arg);
			}

			Class<?> ctorClasses[] = new Class<?>[args.size()];
			for (int i = 0; i < ctorClasses.length; i++) {
				ctorClasses[i] = String.class;
			}
			Object ctorArgs[] = new Object[0];

			Constructor<?> cTor = null;
			try {
				cTor = clazz.getConstructor(ctorClasses);
				ctorArgs = args.toArray();
			} catch (NoSuchMethodException nsme1) {
				try {
					cTor = clazz.getConstructor(new Class<?>[] { String[].class });
					ctorArgs = new Object[] { args.toArray() };
				} catch (NoSuchMethodException nsme2) {

				}
			}
			if (cTor == null) {
				ci.println(className + " has no constructor for " + ctorClasses.length + " String argument(s)");
				return;
			}

			Thread thread = Thread.currentThread();
			ClassLoader oldCl = thread.getContextClassLoader();
			try {
				if (clazz.getClassLoader() != null) {
					ci.println("Creating " + clazz.getSimpleName() + ". Switching classloader from " + oldCl + " to "
							+ clazz.getClassLoader());
					thread.setContextClassLoader(clazz.getClassLoader());
				}

				ctx = new NotesContext(module);
				NotesContext.initThread(ctx);
				try {
					if (Callable.class.isAssignableFrom(clazz)) {
						Callable<?> callable = (Callable<?>) cTor.newInstance(ctorArgs);
						XotsDaemon.queue(callable);
					} else if (Runnable.class.isAssignableFrom(clazz)) {
						Runnable runnable = (Runnable) cTor.newInstance(ctorArgs);
						XotsDaemon.queue(runnable);
					} else {
						ci.println("Could not run " + className + ", as this is no runnable or callable class");
					}
				} finally {
					NotesContext.termThread();
				}
			} finally {
				thread.setContextClassLoader(oldCl);
			}
		} catch (Exception e) {
			ci.printStackTrace(e);
		}
		try {
			//ci.println("DEBUG: Opening module: " + moduleName);
			//ModuleLoader.loadModule(moduleName, true);

			//			Runnable runner = new XotsDominoFutureTask<Object>(module, new Runnable() {
			//				@Override
			//				public void run() {
			//					try {
			//						ci.println("DEBUG: loading class: " + className);
			//						ClassLoader mcl = Thread.currentThread().getContextClassLoader();
			//						Class cls = mcl.loadClass(className);
			//						ci.println("DEBUG: creating instance of: " + cls);
			//						Object obj = cls.newInstance();
			//						ci.println("Success: " + obj);
			//						if (obj instanceof Runnable) {
			//							//((Runnable) obj).run();
			//							XotsDaemon.queue((Runnable) obj);
			//						}
			//					} catch (Throwable e) {
			//						StringWriter errors = new StringWriter();
			//						e.printStackTrace(new PrintWriter(errors));
			//						ci.println(errors);
			//					}
			//				}
			//			}, null);
			//			runner.run();
			//			XotsDaemon.queue(runner);

		} catch (Throwable e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			ci.println(errors);
		}
	}

	private Class<?> loadClassFromModule(final CommandInterpreter ci, final String bundleName, final String className) {

		final Bundle bundle = Platform.getBundle(bundleName);
		if (bundle == null) {
			ci.println("Could not find " + bundleName);
			return null;
		}

		Class<?> clazz = null;
		try {
			clazz = bundle.loadClass(className);
		} catch (ClassNotFoundException e) {
		}

		if (clazz == null) {
			ci.println("Could not find class " + className);
			return null;
		}
		return clazz;

	}
}
