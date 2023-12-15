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
/**
 * 
 */
package org.openntf.domino.xsp;

import java.util.logging.Level;

import org.openntf.domino.Document;
import org.openntf.domino.Session;

/**
 * @author withersp
 * 
 */
public class XspOpenLogUtil {

	private transient static XspOpenLogItem oli_;

	/**
	 * 
	 */
	public XspOpenLogUtil() {

	}

	/**
	 * Helper method to give easy access to the XspOpenLogItem
	 * 
	 * @return XspOpenLogItem
	 * @since org.openntf.domino 4.5.0
	 */
	public static XspOpenLogItem getXspOpenLogItem() {
		if (null == oli_) {
			oli_ = new XspOpenLogItem();
		}
		return oli_;
	}

	/**
	 * Logs an error / throwable
	 * 
	 * @param ee
	 *            Throwable holding the error
	 * @return String error message logged
	 * @since org.openntf.domino 4.5.0
	 */
	public static String logError(final Throwable ee) {
		return getXspOpenLogItem().logError(ee);
	}

	/**
	 * Logs an error / throwable for a specific Session
	 * 
	 * @param s
	 *            Session to log the eror for
	 * @param ee
	 *            Throwable holding the error
	 * @since org.openntf.domino 4.5.0
	 */
	public static void logError(final Session s, final Throwable ee) {
		getXspOpenLogItem().logError(s, ee);
	}

	/**
	 * Logs an error / throwable for a specific Session at a specific severity for a specific document
	 * 
	 * @param s
	 *            Session to log the eror for
	 * @param ee
	 *            Throwable holding the error
	 * @param msg
	 *            String message to log
	 * @param severityType
	 *            Level to log at
	 * @param doc
	 *            Document to log the error for or null
	 * @since org.openntf.domino 4.5.0
	 */
	public static void logError(final Session s, final Throwable ee, final String msg, final Level severityType, final Document doc) {
		getXspOpenLogItem().logError(s, ee, msg, severityType, doc);
	}

	/**
	 * Logs an error / throwable at a specific severity for a specific document
	 * 
	 * @param ee
	 *            Throwable holding the error
	 * @param msg
	 *            String message to log
	 * @param severityType
	 *            Level to log at
	 * @param doc
	 *            Document to log the error for or null
	 * @return String error message logged
	 * @since org.openntf.domino 4.5.0
	 */
	public static String logErrorEx(final Throwable ee, final String msg, final Level severityType, final Document doc) {
		return getXspOpenLogItem().logErrorEx(ee, msg, severityType, doc);
	}

	/**
	 * Logs an event for a specific Session at a specific severity for a specific document
	 * 
	 * @param s
	 *            Session to log the event for
	 * @param ee
	 *            Throwable - use <code>new Throwable()</code>
	 * @param msg
	 *            String message to log
	 * @param severityType
	 *            Level to log at
	 * @param doc
	 *            Document to log the event for or null
	 * @since org.openntf.domino 4.5.0
	 */
	public static void logEvent(final Session s, final Throwable ee, final String msg, final Level severityType, final Document doc) {
		getXspOpenLogItem().logEvent(s, ee, msg, severityType, doc);
	}

	/**
	 * Logs an event at a specific severity for a specific document
	 * 
	 * @param ee
	 *            Throwable - use <code>new Throwable()</code>
	 * @param msg
	 *            String message to log
	 * @param severityType
	 *            Level to log at
	 * @param doc
	 *            Document to log the event for or null
	 * @since org.openntf.domino 4.5.0
	 */
	public static String logEvent(final Throwable ee, final String msg, final Level severityType, final Document doc) {
		return getXspOpenLogItem().logEvent(ee, msg, severityType, doc);
	}

}
