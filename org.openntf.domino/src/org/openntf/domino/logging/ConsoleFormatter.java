package org.openntf.domino.logging;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * ConsoleFormatter class
 * 
 * Handles formatting of output to Domino console
 * 
 * @author withersp
 * 
 */
public class ConsoleFormatter extends Formatter {

	/** The UT c_ format. */
	private boolean UTC_Format = false;

	/**
	 * Constructor
	 * 
	 * @since org.openntf.domino 1.0.0
	 */
	public ConsoleFormatter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Checks whether dates should be outputted in UTC format
	 * 
	 * @return boolean, if is UTC format
	 * @since org.openntf.domino 1.0.0
	 */
	public boolean isUTC_Format() {
		return UTC_Format;
	}

	/**
	 * Sets whether dates should be outputted in UTC format
	 * 
	 * @param uTC_Format
	 *            boolean, whether UTC format
	 * @since org.openntf.domino 1.0.0
	 */
	public void setUTC_Format(final boolean uTC_Format) {
		UTC_Format = uTC_Format;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
	 */
	@Override
	public String format(final LogRecord logRecord) {
		Date recordDate = new Date(logRecord.getMillis());
		StringBuffer sb = new StringBuffer();
		try {
			sb.append(LogUtils.dateToString(recordDate, UTC_Format));
			sb.append(" [");
			sb.append(logRecord.getLevel().getName());
			sb.append("]: ");
			Throwable t = logRecord.getThrown();
			StackTraceElement ste = null;
			if (t != null) {
				ste = t.getStackTrace()[0];
				sb.append(ste.getClassName() + "." + ste.getMethodName());
			}

			sb.append(" - ");
			sb.append(logRecord.getMessage());
			if (t != null) {
				sb.append("\n");
				sb.append("***See IBM_TECHNICAL_SUPPORT\\org.openntf.log.X.Y.txt for full stack trace***");
				sb.append("\n");
			}
		} catch (Throwable t) {
			System.out.println("Error trying to format output for error handling. Resorting to standard stack trace...");
			t.printStackTrace();
		}
		return sb.toString();
	}
}
