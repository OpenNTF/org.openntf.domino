package org.openntf.domino.logging;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class ConsoleFormatter extends Formatter {

	/** The UT c_ format. */
	private boolean UTC_Format = false;

	public ConsoleFormatter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Checks if is uT c_ format.
	 * 
	 * @return true, if is uT c_ format
	 */
	public boolean isUTC_Format() {
		return UTC_Format;
	}

	/**
	 * Sets the uT c_ format.
	 * 
	 * @param uTC_Format
	 *            the new uT c_ format
	 */
	public void setUTC_Format(boolean uTC_Format) {
		UTC_Format = uTC_Format;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
	 */
	@Override
	public String format(LogRecord logRecord) {
		Date recordDate = new Date(logRecord.getMillis());
		StringBuffer sb = new StringBuffer();
		StackTraceElement ste = logRecord.getThrown().getStackTrace()[0];
		sb.append(LogUtils.dateToString(recordDate, UTC_Format));
		sb.append(" [");
		sb.append(logRecord.getLevel().getName());
		sb.append("]: ");
		if (null == ste) {
			sb.append("***NO STACK TRACE***");
		} else {
			sb.append(ste.getClassName() + "." + ste.getMethodName());
		}
		sb.append(" - ");
		sb.append(logRecord.getMessage());
		sb.append("\n");
		sb.append("***See IBM_TECHNICAL_SUPPORT\\org.openntf.log.X.Y.txt for full stack trace***");
		sb.append("\n");

		return sb.toString();
	}
}
