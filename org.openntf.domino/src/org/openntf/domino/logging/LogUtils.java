package org.openntf.domino.logging;

import java.io.IOException;
import java.io.InputStream;
import java.security.AccessControlException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.openntf.domino.utils.DominoUtils;

/**
 * @author withersp
 * 
 */
public class LogUtils {

	private static SimpleDateFormat ISO8601_UTC = null;
	private static SimpleDateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

	public LogUtils() {

	}

	/**
	 * Retrieves default logging properties file from this package as InputStream. <br/>
	 * Called by getLogConfigFile(int, String) if no file passed to it or file not found.
	 * 
	 * @return InputStream of logging configuration using logging.properties in org.openntf.domino.logging
	 */
	public static InputStream getDefaultLogConfigFile() {
		try {
			return LogUtils.class.getResourceAsStream("logging.properties");
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
	 */
	public static InputStream getLogConfigFile(int fileType, String filePath) {
		InputStream is = null;
		try {
			if ("".equals(filePath)) {
				return getDefaultLogConfigFile();
			} else {
				is = DominoUtils.getDominoProps(fileType, filePath);
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
	 *            If <code>true</code>, format the time in UTC. If <code>false</code>, format the time in the local time zone.
	 * @return The ISO8601 string.
	 * @throws IOException
	 */
	public static String dateToString(Date value, boolean utc) {

		String result = null;

		if (utc) {
			if (ISO8601_UTC == null) {
				// Initialize the UTC formatter once
				TimeZone tz = TimeZone.getTimeZone("UTC");
				ISO8601_UTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); //$NON-NLS-1$
				ISO8601_UTC.setTimeZone(tz);
			}

			result = ISO8601_UTC.format(value);
		} else {
			result = ISO8601.format(value);
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
	 */
	static public void loadLoggerConfig(boolean relative, String filePath) {
		try {
			LogManager manager = LogManager.getLogManager();
			if ("".equals(filePath)) {
				manager.readConfiguration(getDefaultLogConfigFile());
			} else {
				InputStream is = null;
				if (relative) {
					is = getLogConfigFile(3, filePath);
				} else {
					is = getLogConfigFile(2, filePath);
				}
				manager.readConfiguration(is);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (AccessControlException e) {
			return;
		} catch (Throwable t) {
			t.printStackTrace();
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
	 */
	public static boolean setupLoggerEx(final String logName, final ArrayList<Handler> handlers, final boolean useParentHandler,
			final Level newSeverityLevel) {
		try {
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
		} catch (AccessControlException e) {
			return false;
		} catch (Throwable t) {
			t.printStackTrace();
			return false;
		}
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
	 */
	public static boolean hasAccessException(Logger log_) {
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
	 * Gets a logger from the LogManager
	 * 
	 * @param logName
	 *            name of the logger
	 * @return The Logger object
	 */
	public static Logger getLogger(final String logName) {
		LogManager manager = LogManager.getLogManager();
		Logger loggerToModify = manager.getLogger(logName);
		return loggerToModify;
	}

}
