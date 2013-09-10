/*
 * © Copyright IBM Corp. 2012-2013
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

package com.ibm.commons.util;

import java.io.PrintStream;

import lotus.domino.util.Platform;

//import com.ibm.commons.Platform;
//import com.ibm.commons.log.CommonsLogger;

/**
 * Diagnotics helpers.
 * <p>
 * Easy to use log utilities.
 * </p>
 * 
 * @ibm-api
 */
public class TDiag {

	/**
	 * Method that trace to the console. This should be avoided unless the messages must actually go to the console
	 * 
	 * @param msg
	 *            the message to display
	 * @param params
	 *            the parameters used to format the message
	 * @ibm-api
	 */
	public static final void console(final String msg, final Object... params) {
		String text = StringUtil.format(msg, params);
		getOutputStream().println(text);
	}

	/**
	 * Method that trace an exception to the console. This should be avoided unless the messages must actually go to the console
	 * 
	 * @param ex
	 *            the exception to trace
	 * @ibm-api
	 */
	public static final void console(final Exception ex) {
		ex.printStackTrace(getOutputStream());
	}

	/**
	 * Method that trace an exception to the console. This should be avoided unless the messages must actually go to the console
	 * 
	 * @param ex
	 *            the exception to trace
	 * @param msg
	 *            the message to display
	 * @param params
	 *            the parameters used to format the message
	 * @ibm-api
	 */
	public static final void console(final Exception ex, final String msg, final Object... params) {
		String text = StringUtil.format(msg, params);
		getOutputStream().println(text);
		ex.printStackTrace(getOutputStream());
	}

	/**
	 * Common synchronization object when tracing to the output stream
	 * 
	 * @ibm-api
	 */
	public static PrintStream getSyncObject() {
		return System.out;
	}

	/**
	 * Check if the common logger is enabled.
	 * 
	 * @ibm-api
	 */
	public static final void isEnabled() {
		CommonsLogger.STANDARD.isTraceDebugEnabled();
	}

	/**
	 * Trace to the common logger.
	 * 
	 * @ibm-api
	 */
	public static final void trace(final String msg, final Object... parameters) {
		if (CommonsLogger.STANDARD.isTraceDebugEnabled()) {
			CommonsLogger.STANDARD.traceDebug(msg, parameters);
		}
	}

	public static final void tracep(final Object clazz, final String methodName, final Throwable t, final String msg,
			final Object... parameters) {
		if (CommonsLogger.STANDARD.isTraceDebugEnabled()) {
			CommonsLogger.STANDARD.traceDebugp(clazz, methodName, t, msg, parameters);
		}
	}

	public static final void incIndent() {
		//CommonsLogger.STANDARD.incIndent();
	}

	public static final void decIndent() {
		//CommonsLogger.STANDARD.decIndent();
	}

	/**
	 * Trace an exception to the common logger.
	 * 
	 * @ibm-api
	 */
	public static final void exception(final Throwable t) {
		if (CommonsLogger.STANDARD.isErrorEnabled()) {
			// note, in domino a stack trace is never displayed
			// so some message should be provided
			String msg = null;
			try {
				msg = t.toString();
			} catch (Throwable loggingProblem) {
				// ignore logging problem
			}
			if (null == msg || msg.length() == 0) {
				try {
					msg = t.getClass().getName() + "@????"; //$NON-NLS-1$
				} catch (Throwable problem2) {
					// ignore logging problem
				}
				if (null == msg) {
					msg = "";
				}
			}
			CommonsLogger.STANDARD.error(t, msg);
		}
	}

	public static final void exception(final Throwable t, final String msg, final Object... parameters) {
		if (CommonsLogger.STANDARD.isErrorEnabled()) {
			CommonsLogger.STANDARD.error(t, msg, parameters);
		}
	}

	public static PrintStream getOutputStream() {
		return Platform.getInstance().getOutputStream();
	}
}
