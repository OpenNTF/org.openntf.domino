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
package org.openntf.junit4xpages;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NotesContext;

/**
 * 
 * @author Alexander Wagner, FOCONIS AG
 * 
 */
public class TestRunner {

	/**
	 * @param result
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings({ "nls", "unchecked", "rawtypes" })
	public void run(final Map<String, String> result, final String javaFile) throws ClassNotFoundException {
		OutputStream testDetail = new ByteArrayOutputStream();
		final OutputStream testOut = new ByteArrayOutputStream();
		final OutputStream testErr = new ByteArrayOutputStream();
		final PrintStream origOut = System.out;
		final PrintStream origErr = System.err;
		int i = javaFile.indexOf(".java");

		NotesContext ctx = NotesContext.getCurrent();
		NSFComponentModule moduleCurrent = ctx.getModule();

		final Class<?> testClass = Class.forName(javaFile.substring(0, i).replace("/", "."), true, moduleCurrent.getModuleClassLoader());

		try {
			System.out.println("Running " + testClass.getName());
			final JUnitCore runner = new JUnitCore();

			AccessController.doPrivileged(new PrivilegedAction() {
				@Override
				public Object run() {
					System.setOut(new PrintStream(testOut));
					System.setErr(new PrintStream(testErr));
					return null;
				}
			});

			// redirect system out + err
			// System.setOut(new PrintStream(testOut));
			// System.setErr(new PrintStream(testErr));
			PrintStream details = new PrintStream(testDetail);

			// result.put("success", "0"); // success = false/unknown
			Result r = AccessController.doPrivileged(new PrivilegedAction() {
				@Override
				public Object run() {
					Result r = runner.run(testClass);
					return r;
				}
			});

			if (r.getFailureCount() == 0) {
				// test run was successful
				// result.put("success", "1");
				result.put("result", "<font color='green'>OK - " + r.getRunCount() + " tests succeeded</font> (" + r.getIgnoreCount()
						+ " ignored)");
				details.print("OK");
				result.put("status", "OK");
			} else {
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>FAIL - ");
				sb.append(r.getFailureCount());
				sb.append(" Errors in ");
				sb.append(r.getRunCount());
				sb.append(" Tests .</font> (");
				sb.append(r.getIgnoreCount());
				sb.append(" ignored)");

				for (Failure each : r.getFailures()) {
					sb.append("<br/>");
					sb.append(each.getTestHeader());
				}
				result.put("result", sb.toString());

				// StackTraceElement current[] = Thread.currentThread().getStackTrace();
				for (Failure each : r.getFailures()) {
					Throwable t = each.getException();
					details.print("TEST FAILED: ");
					details.println(each.getTestHeader());
					details.println("---------------------------------------------");
					t.printStackTrace(details);
					details.println();
				}
				result.put("status", "FAIL");
			}
		} catch (Throwable t) {
			t.printStackTrace();
			result.put("status", "error");
			result.put("error", t.toString());
		} finally {
			AccessController.doPrivileged(new PrivilegedAction() {
				@Override
				public Object run() {
					System.setOut(origOut);
					System.setErr(origErr);
					return null;
				}
			});
		}
		result.put("detail", testDetail.toString());
		result.put("sysOut", testOut.toString());
		result.put("sysErr", testErr.toString());
	}
}
