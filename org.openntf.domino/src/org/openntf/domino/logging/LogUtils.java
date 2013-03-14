package org.openntf.domino.logging;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.LogManager;

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

	static public void setupLogger(boolean relative, String filePath) {
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
				if (null == is) {
					manager.readConfiguration(getDefaultLogConfigFile());
				} else {
					manager.readConfiguration(is);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
