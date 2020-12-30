/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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
package org.openntf.domino.logging;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.openntf.domino.utils.DominoUtils;

/**
 * Utility methods.
 * 
 * @author withersp
 *
 */
@SuppressWarnings("nls")
public class LogUtils {

	/**
	 * Initializes a SimpleDateFormat for use if UTC is not enabled
	 *
	 * @since org.openntf.domino 1.0.0
	 */
	private static ThreadLocal<SimpleDateFormat> ISO_LOCAL = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); //$NON-NLS-1$
		}
	};

	/**
	 * Initializes a SimpleDateFormat for use if UTC is enabled
	 *
	 * @since org.openntf.domino 1.0.0
	 */
	private static ThreadLocal<SimpleDateFormat> ISO_UTC_LOCAL = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			SimpleDateFormat ISO8601_UTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); //$NON-NLS-1$;
			TimeZone tz = TimeZone.getTimeZone("UTC"); //$NON-NLS-1$
			ISO8601_UTC.setTimeZone(tz);
			return ISO8601_UTC;
		}
	};

	/**
	 * Instantiates a new log utils.
	 *
	 * @since org.openntf.domino 1.0.0
	 */
	public LogUtils() {

	}

	/**
	 * Parses logging.properties and replaces any instances of <notesdata> with the path for the domino\data folder
	 *
	 * @param is
	 *            InputStream comprising logging.properties
	 * @return InputStream replaced content from logging.properties
	 * @since org.openntf.domino 1.0.0
	 */
	private static InputStream parsePropertiesStream(final InputStream is) {
		//		System.out.println("Parsing properties stream...");
		Properties props = new Properties();
		try {
			props.load(is);
			String datapath = org.openntf.domino.utils.Factory.getDataPath() + "/";
			for (Object key : props.keySet()) {
				if (key instanceof String) {
					String keyName = (String) key;
					String value = props.getProperty(keyName);
					if (value.indexOf("<notesdata>") > -1) { //$NON-NLS-1$
						String result = value.replace("<notesdata>", datapath); //$NON-NLS-1$
						//						System.out.println("Found notesdata variable in properties value for " + keyName);
						//						System.out.println("Replacing with " + datapath + " to result in " + result);
						props.put(keyName, result);
					}
					//					value.replace("%notesprogram%", Factory.getProgramPath() + java.io.File.pathSeparator);

				}
			}
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			props.store(os, ""); //$NON-NLS-1$
			InputStream result = new ByteArrayInputStream(os.toByteArray());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return is;
		}

	}

	/**
	 * Retrieves default logging properties file from this package as InputStream. <br/>
	 * Called by getLogConfigFile(int, String) if no file passed to it or file not found.
	 *
	 * @return InputStream of logging configuration using logging.properties in org.openntf.domino.logging
	 * @since org.openntf.domino 1.0.0
	 */
	public static InputStream getDefaultLogConfigFile() {
		try {
			InputStream raw = LogUtils.class.getResourceAsStream("logging.properties"); //$NON-NLS-1$
			return parsePropertiesStream(raw);
		} catch (Throwable e) {
			System.out.println("SEVERE: Error getting default log config file");
			return null;
		}
	}

	/**
	 * Retrieves a log file or, if that cannot be retrieved, the default log file.
	 *
	 * @param fileType
	 *            int passed to switch statement in DominoUtils.getDominoProps. <br/>
	 *            See DominoUtils.getDominoProps for options available.
	 * @param filePath
	 *            String path of file to be used. Can be literal or relative to <data> directory
	 * @return InputStream content of properties file
	 * @since org.openntf.domino 1.0.0
	 */
	public static InputStream getLogConfigFile(final int fileType, final String filePath) {
		InputStream is = null;
		try {
			if ("".equals(filePath)) {
				return getDefaultLogConfigFile();
			} else {
				is = parsePropertiesStream(DominoUtils.getDominoProps(fileType, filePath));
				if (null == is) {
					is = getDefaultLogConfigFile();
				}
				return is;
			}
		} catch (Throwable e) {
			System.out.println("SEVERE: Error getting log config file: " + filePath + " (" + Integer.toString(fileType) + ")");
			return null;
		}
	}

	/**
	 * Converts a date to an ISO8601 string.
	 *
	 * @param value
	 *            The date.
	 * @param utc
	 *            Boolean, whether to format the time in UTC or the local time zone.
	 * @return The ISO8601 string.
	 * @since org.openntf.domino 1.0.0
	 */
	public static String dateToString(final Date value, final boolean utc) {

		String result = null;

		if (utc) {
			result = ISO_UTC_LOCAL.get().format(value);
		} else {
			result = ISO_LOCAL.get().format(value);
		}

		return result;
	}

	/**
	 * Initialise a logger based on a configuration file.
	 *
	 * @param relative
	 *            boolean whether filepath is relative to <data> folder
	 * @param filePath
	 *            String of filepath for logging properties file, or empty string to load the default org.openntf.domino logger
	 * @since org.openntf.domino 1.0.0
	 */
	static public void loadLoggerConfig(final boolean relative, final String filePath) {
		System.out.println("Loading Logger config...");
		try {
			AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
				@Override
				public Object run() throws Exception {
					LogManager manager = LogManager.getLogManager();
					if ("".equals(filePath)) { //$NON-NLS-1$
						InputStream is = getDefaultLogConfigFile();
						if (is != null) {
							manager.readConfiguration(is);
						}
					} else {
						InputStream is = null;
						if (relative) {
							is = getLogConfigFile(3, filePath);
						} else {
							is = getLogConfigFile(2, filePath);
						}
						manager.readConfiguration(is);
					}
					return null;
				}
			});
			System.out.println("Completed logger config.");
		} catch (AccessControlException e) {
			e.printStackTrace();
		} catch (PrivilegedActionException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Updates a specified Logger with settings passed in. Pass in an ArrayList of Handlers, whether or not to use the parent handler, and a
	 * new security level. Options for security level are:
	 *
	 * <ul>
	 * <li>Level.SEVERE</li>
	 * <li>Level.WARNING</li>
	 * <li>Level.INFO</li>
	 * <li>Level.CONFIG</li>
	 * <li>Level.FINE</li>
	 * <li>Level.FINER</li>
	 * <li>Level.FINEST</li>
	 * </ul>
	 *
	 * @param logName
	 *            The name of the logger to modify
	 * @param handlers
	 *            an array of Handler objects to apply to the logger. Any pre-defined handlers will be removed. To pass in existing
	 *            handlers, just use myLogger.getHandlers().
	 * @param useParentHandler
	 *            boolean of whether or not to use parent handlers
	 * @param newSeverityLevel
	 *            the new severity level
	 * @return success or failure in updating the logger
	 * @since org.openntf.domino 1.0.0
	 */
	public static boolean setupLoggerEx(final String logName, final ArrayList<Handler> handlers, final boolean useParentHandler,
			final Level newSeverityLevel) {

		try {
			boolean result = (Boolean) AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
				@Override
				public Object run() throws Exception {
					Logger loggerToModify = getLogger(logName);
					for (Handler currHandlers : loggerToModify.getHandlers()) {
						loggerToModify.removeHandler(currHandlers);
					}
					for (Handler newHandler : handlers) {
						newHandler.setLevel(newSeverityLevel);
						loggerToModify.addHandler(newHandler);
					}
					loggerToModify.setUseParentHandlers(useParentHandler);
					if (null != newSeverityLevel) {
						loggerToModify.setLevel(newSeverityLevel);
					}
					return true;
				}
			});
			return result;
		} catch (AccessControlException e) {
			e.printStackTrace();
		} catch (PrivilegedActionException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Check whether you can change a logger property. XPages and Websphere security is draconian and doesn't allow modifying loggers. One
	 * option is set java.policy to grant all, but that's not good. This will see whether they work or not. <br/>
	 * <br/>
	 * In case of failure, you can either use another, non-standard logging mechanism like OpenLog, or log using com.ibm.xsp.domino and
	 * Level.SEVERE - that's the only level logged by that logger.
	 *
	 * @param log_
	 *            Logger to try and change
	 * @return success or failure.
	 * @since org.openntf.domino 1.0.0
	 */
	public static boolean hasAccessException(final Logger log_) {
		try {
			log_.setLevel(log_.getLevel());
			return false;
		} catch (AccessControlException e) {
			return true;
		} catch (Throwable t) {
			return true;
		}
	}

	/**
	 * Gets a logger from the LogManager.
	 *
	 * @param logName
	 *            name of the logger
	 * @return The Logger object
	 * @since org.openntf.domino 1.0.0
	 */
	public static Logger getLogger(final String logName) {
		LogManager manager = LogManager.getLogManager();
		Logger loggerToModify = manager.getLogger(logName);
		return loggerToModify;
	}

}
