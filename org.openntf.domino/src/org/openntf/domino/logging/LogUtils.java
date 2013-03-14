package org.openntf.domino.logging;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.openntf.domino.utils.DominoUtils;

public class LogUtils {

	private static SimpleDateFormat ISO8601_UTC = null;
	private static SimpleDateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

	public LogUtils() {

	}

	public static InputStream getDefaultLogConfigFile() {
		try {
			return LogUtils.class.getResourceAsStream("logging.properties");
		} catch (Throwable e) {
			System.out.println("SEVERE: Error getting default log config file");
			return null;
		}
	}

	public static InputStream getLogConfigFile(int fileType, String filePath) {
		InputStream is = null;
		try {
			is = DominoUtils.getDominoProps(fileType, filePath);
			if (null == is) {
				is = getDefaultLogConfigFile();
			}
			return is;
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

}
